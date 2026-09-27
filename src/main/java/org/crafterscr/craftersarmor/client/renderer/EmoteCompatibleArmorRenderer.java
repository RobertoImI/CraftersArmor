package org.crafterscr.craftersarmor.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import org.crafterscr.craftersarmor.client.compat.PlayerAnimatorCompat;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.cache.object.GeoVertex;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.RenderUtil;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Optional Player Animator / Emotecraft compatibility for GeckoLib armor.
 *
 * <p>The visible geo model is never split into rigid upper/lower pieces.
 * While an emote bend is active, the original GeckoLib faces are subdivided
 * only for that render frame and their vertices are deformed with Bendy-lib
 * style bend math. The source model and its UV layout remain intact.</p>
 */
public abstract class EmoteCompatibleArmorRenderer<T extends Item & GeoItem>
        extends GeoArmorRenderer<T> {

    private static final float PIXELS_PER_BLOCK = 16.0F;
    private static final float GEO_MODEL_HEIGHT = 24.0F / PIXELS_PER_BLOCK;
    private static final float BEND_EPSILON = 1.0E-4F;
    private static final int MAX_SUBDIVISIONS = 16;

    /**
     * Small render-only correction for GeckoLib coat sleeves while an
     * Emotecraft / Player Animator animation is active.
     *
     * <p>The widened coat sleeves are centered correctly in the normal pose,
     * but during animated arm transforms they sit a fraction of a pixel too
     * far away from the shoulder. Pull each sleeve 0.35 model pixels inward
     * without modifying the source geo model.</p>
     */
    private static final float EMOTE_ARM_INSET_PIXELS = 0.35F;

    private final Map<GeoBone, BendBounds> bendBoundsCache =
            new WeakHashMap<>();

    private BendContext activeBendContext;

    protected EmoteCompatibleArmorRenderer(GeoModel<T> model) {
        super(model);
    }

    @Override
    protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
        super.applyBaseTransformations(baseModel);

        copyScale(baseModel.head, this.head);
        copyScale(baseModel.body, this.body);
        copyScale(baseModel.rightArm, this.rightArm);
        copyScale(baseModel.leftArm, this.leftArm);
        copyScale(baseModel.rightLeg, this.rightLeg);
        copyScale(baseModel.leftLeg, this.leftLeg);
        copyScale(baseModel.rightLeg, this.rightBoot);
        copyScale(baseModel.leftLeg, this.leftBoot);

        if (PlayerAnimatorCompat.isActive(this.currentEntity)) {
            applyEmoteSleeveAlignment();
        }
    }

    private void applyEmoteSleeveAlignment() {
        if (this.rightArm != null) {
            this.rightArm.setPosX(
                    this.rightArm.getPosX()
                            + EMOTE_ARM_INSET_PIXELS
            );
        }

        if (this.leftArm != null) {
            this.leftArm.setPosX(
                    this.leftArm.getPosX()
                            - EMOTE_ARM_INSET_PIXELS
            );
        }
    }

    private static void copyScale(ModelPart source, GeoBone target) {
        if (target != null) {
            target.updateScale(
                    source.xScale,
                    source.yScale,
                    source.zScale
            );
        }
    }

    @Override
    public void renderRecursively(
            PoseStack poseStack,
            T animatable,
            GeoBone bone,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            boolean isReRender,
            float partialTick,
            int packedLight,
            int packedOverlay,
            int colour
    ) {
        BendContext previousContext = this.activeBendContext;
        this.activeBendContext = createLocalBendContext(bone);

        PlayerAnimatorCompat.Bend bodyBend =
                PlayerAnimatorCompat.getBend(
                        this.currentEntity,
                        "body"
                );

        boolean applyUpperBodyBend =
                isUpperBodyPart(bone.getName())
                        && bodyBend.isActive();

        if (applyUpperBodyBend) {
            poseStack.pushPose();
            applyPlayerAnimatorUpperBodyBend(
                    poseStack,
                    bodyBend
            );
        }

        try {
            super.renderRecursively(
                    poseStack,
                    animatable,
                    bone,
                    renderType,
                    bufferSource,
                    buffer,
                    isReRender,
                    partialTick,
                    packedLight,
                    packedOverlay,
                    colour
            );
        }
        finally {
            if (applyUpperBodyBend) {
                poseStack.popPose();
            }

            this.activeBendContext = previousContext;
        }
    }

    /**
     * Player Animator normally applies this transform inside HumanoidModel's
     * render path. GeoArmorRenderer replaces that render path, so without this
     * headgear and arm armor can detach during a body bend.
     */
    private static void applyPlayerAnimatorUpperBodyBend(
            PoseStack poseStack,
            PlayerAnimatorCompat.Bend bend
    ) {
        /*
         * Player Animator applies the upper-body bend in vanilla model space
         * around Y = 6 px (0.375 blocks):
         *
         *   T(0, 6/16, 0) * R * T(0, -6/16, 0)
         *
         * GeoArmorRenderer is already inside GeckoLib's armor transform:
         *
         *   translate(0, 24/16, 0) * scale(-1, -1, 1)
         *
         * before renderRecursively is reached. Applying Player Animator's
         * vanilla-space matrix directly here therefore bends the armor around
         * the wrong point, which makes hats/crowns and shoulders visibly
         * detach from the player.
         *
         * Convert the vanilla bend into GeckoLib model space instead:
         * vanilla Y=6 maps to geo Y=24-6=18 px, and the X/Y inversion changes
         * the rotation axis accordingly.
         */
        float vanillaPivotY = 0.375F;
        float geoPivotY = GEO_MODEL_HEIGHT - vanillaPivotY;

        float vanillaAxisAngle = -bend.axis();
        float geoAxisX = -(float)Math.cos(vanillaAxisAngle);
        float geoAxisZ = (float)Math.sin(vanillaAxisAngle);

        poseStack.translate(0.0F, geoPivotY, 0.0F);
        poseStack.mulPose(
                new Quaternionf().rotateAxis(
                        bend.amount(),
                        geoAxisX,
                        0.0F,
                        geoAxisZ
                )
        );
        poseStack.translate(0.0F, -geoPivotY, 0.0F);
    }

    private static boolean isUpperBodyPart(String boneName) {
        return "armorHead".equals(boneName)
                || "armorRightArm".equals(boneName)
                || "armorLeftArm".equals(boneName);
    }

    private BendContext createLocalBendContext(GeoBone bone) {
        String name = bone.getName();

        if ("armorBody".equals(name)) {
            PlayerAnimatorCompat.Bend torso =
                    PlayerAnimatorCompat.getBend(
                            this.currentEntity,
                            "torso"
                    );

            PlayerAnimatorCompat.Bend body =
                    PlayerAnimatorCompat.getBend(
                            this.currentEntity,
                            "body"
                    );

            PlayerAnimatorCompat.Bend combined =
                    PlayerAnimatorCompat.combine(
                            torso,
                            body
                    );

            return createBoundedContext(
                    bone,
                    combined,
                    Direction.DOWN
            );
        }

        if ("armorRightArm".equals(name)) {
            return createBoundedContext(
                    bone,
                    PlayerAnimatorCompat.getBend(
                            this.currentEntity,
                            "rightArm"
                    ),
                    Direction.UP
            );
        }

        if ("armorLeftArm".equals(name)) {
            return createBoundedContext(
                    bone,
                    PlayerAnimatorCompat.getBend(
                            this.currentEntity,
                            "leftArm"
                    ),
                    Direction.UP
            );
        }

        if ("armorRightLeg".equals(name)) {
            return createBoundedContext(
                    bone,
                    PlayerAnimatorCompat.getBend(
                            this.currentEntity,
                            "rightLeg"
                    ),
                    Direction.UP
            );
        }

        if ("armorLeftLeg".equals(name)) {
            return createBoundedContext(
                    bone,
                    PlayerAnimatorCompat.getBend(
                            this.currentEntity,
                            "leftLeg"
                    ),
                    Direction.UP
            );
        }

        if ("armorRightBoot".equals(name)) {
            PlayerAnimatorCompat.Bend bend =
                    PlayerAnimatorCompat.getBend(
                            this.currentEntity,
                            "rightLeg"
                    );

            return bend.isActive()
                    ? new BendContext(
                            bend,
                            Direction.UP,
                            createVirtualLegBounds(bone)
                    )
                    : null;
        }

        if ("armorLeftBoot".equals(name)) {
            PlayerAnimatorCompat.Bend bend =
                    PlayerAnimatorCompat.getBend(
                            this.currentEntity,
                            "leftLeg"
                    );

            return bend.isActive()
                    ? new BendContext(
                            bend,
                            Direction.UP,
                            createVirtualLegBounds(bone)
                    )
                    : null;
        }

        return null;
    }

    private BendContext createBoundedContext(
            GeoBone bone,
            PlayerAnimatorCompat.Bend bend,
            Direction direction
    ) {
        if (!bend.isActive()) {
            return null;
        }

        BendBounds bounds = findPrimaryCubeBounds(bone);

        return bounds == null
                ? null
                : new BendContext(
                        bend,
                        direction,
                        bounds
                );
    }

    /**
     * Shoes contain only the bottom of the leg. Their own cube center would
     * therefore put the bend inside the shoe. Use the normal humanoid 4x12x4
     * leg volume so the shoe rotates around the knee instead.
     */
    private static BendBounds createVirtualLegBounds(
            GeoBone bone
    ) {
        float centerX =
                bone.getPivotX() / PIXELS_PER_BLOCK;
        float centerZ =
                bone.getPivotZ() / PIXELS_PER_BLOCK;

        Vector3f cornerA = geoToVanillaRaw(
                new Vector3f(
                        centerX - 2.0F / PIXELS_PER_BLOCK,
                        0.0F,
                        centerZ - 2.0F / PIXELS_PER_BLOCK
                )
        );

        Vector3f cornerB = geoToVanillaRaw(
                new Vector3f(
                        centerX + 2.0F / PIXELS_PER_BLOCK,
                        12.0F / PIXELS_PER_BLOCK,
                        centerZ + 2.0F / PIXELS_PER_BLOCK
                )
        );

        return BendBounds.fromCorners(
                cornerA,
                cornerB
        );
    }

    private BendBounds findPrimaryCubeBounds(
            GeoBone bone
    ) {
        BendBounds cached =
                this.bendBoundsCache.get(bone);

        if (cached != null) {
            return cached;
        }

        GeoCube primary = null;
        double largestVolume = -1.0D;

        for (GeoCube cube : bone.getCubes()) {
            Vec3 size = cube.size();
            double volume = Math.abs(
                    size.x * size.y * size.z
            );

            if (volume > largestVolume) {
                largestVolume = volume;
                primary = cube;
            }
        }

        if (primary == null) {
            return null;
        }

        BendBounds bounds = boundsOf(primary);
        this.bendBoundsCache.put(
                bone,
                bounds
        );

        return bounds;
    }

    private static BendBounds boundsOf(GeoCube cube) {
        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float minZ = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;
        float maxZ = Float.NEGATIVE_INFINITY;

        for (GeoQuad quad : cube.quads()) {
            if (quad == null) {
                continue;
            }

            for (GeoVertex vertex : quad.vertices()) {
                Vector3f raw =
                        geoToVanillaRaw(
                                vertex.position()
                        );

                minX = Math.min(minX, raw.x);
                minY = Math.min(minY, raw.y);
                minZ = Math.min(minZ, raw.z);
                maxX = Math.max(maxX, raw.x);
                maxY = Math.max(maxY, raw.y);
                maxZ = Math.max(maxZ, raw.z);
            }
        }

        return new BendBounds(
                minX,
                minY,
                minZ,
                maxX,
                maxY,
                maxZ
        );
    }

    @Override
    public void renderCube(
            PoseStack poseStack,
            GeoCube cube,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            int colour
    ) {
        BendContext context =
                this.activeBendContext;

        if (context == null
                || !context.bend().isActive()) {
            super.renderCube(
                    poseStack,
                    cube,
                    buffer,
                    packedLight,
                    packedOverlay,
                    colour
            );
            return;
        }

        RenderUtil.translateToPivotPoint(
                poseStack,
                cube
        );
        RenderUtil.rotateMatrixAroundCube(
                poseStack,
                cube
        );
        RenderUtil.translateAwayFromPivotPoint(
                poseStack,
                cube
        );

        for (GeoQuad quad : cube.quads()) {
            if (quad != null) {
                renderBentQuad(
                        poseStack,
                        quad,
                        context,
                        buffer,
                        packedLight,
                        packedOverlay,
                        colour
                );
            }
        }
    }

    private static void renderBentQuad(
            PoseStack poseStack,
            GeoQuad quad,
            BendContext context,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            int colour
    ) {
        GeoVertex[] vertices = quad.vertices();

        int stepsS = subdivisionCount(
                vertices[0].position(),
                vertices[1].position()
        );

        int stepsT = subdivisionCount(
                vertices[0].position(),
                vertices[3].position()
        );

        Matrix4f poseMatrix =
                new Matrix4f(
                        poseStack.last().pose()
                );

        Matrix3f normalMatrix =
                poseStack.last().normal();

        for (int y = 0; y < stepsT; y++) {
            float t0 = y / (float)stepsT;
            float t1 = (y + 1) / (float)stepsT;

            for (int x = 0; x < stepsS; x++) {
                float s0 = x / (float)stepsS;
                float s1 = (x + 1) / (float)stepsS;

                Sample p00 = deform(
                        sample(vertices, s0, t0),
                        context
                );

                Sample p10 = deform(
                        sample(vertices, s1, t0),
                        context
                );

                Sample p11 = deform(
                        sample(vertices, s1, t1),
                        context
                );

                Sample p01 = deform(
                        sample(vertices, s0, t1),
                        context
                );

                Vector3f edgeA =
                        new Vector3f(p10.position())
                                .sub(p00.position());

                Vector3f edgeB =
                        new Vector3f(p01.position())
                                .sub(p00.position());

                Vector3f normal =
                        edgeA.cross(edgeB);

                if (normal.lengthSquared() < 1.0E-8F) {
                    normal.set(quad.normal());
                }
                else {
                    normal.normalize();

                    if (normal.dot(quad.normal()) < 0.0F) {
                        normal.negate();
                    }
                }

                normalMatrix.transform(normal);
                normal.normalize();

                emit(
                        buffer,
                        poseMatrix,
                        normal,
                        p00,
                        packedLight,
                        packedOverlay,
                        colour
                );

                emit(
                        buffer,
                        poseMatrix,
                        normal,
                        p10,
                        packedLight,
                        packedOverlay,
                        colour
                );

                emit(
                        buffer,
                        poseMatrix,
                        normal,
                        p11,
                        packedLight,
                        packedOverlay,
                        colour
                );

                emit(
                        buffer,
                        poseMatrix,
                        normal,
                        p01,
                        packedLight,
                        packedOverlay,
                        colour
                );
            }
        }
    }

    private static int subdivisionCount(
            Vector3f a,
            Vector3f b
    ) {
        int pixels = (int)Math.ceil(
                new Vector3f(b)
                        .sub(a)
                        .length()
                        * PIXELS_PER_BLOCK
        );

        return Math.max(
                1,
                Math.min(
                        MAX_SUBDIVISIONS,
                        pixels
                )
        );
    }

    private static Sample sample(
            GeoVertex[] vertices,
            float s,
            float t
    ) {
        Vector3f top =
                new Vector3f(vertices[0].position())
                        .lerp(
                                vertices[1].position(),
                                s
                        );

        Vector3f bottom =
                new Vector3f(vertices[3].position())
                        .lerp(
                                vertices[2].position(),
                                s
                        );

        Vector3f position =
                top.lerp(bottom, t);

        float topU = lerp(
                vertices[0].texU(),
                vertices[1].texU(),
                s
        );

        float topV = lerp(
                vertices[0].texV(),
                vertices[1].texV(),
                s
        );

        float bottomU = lerp(
                vertices[3].texU(),
                vertices[2].texU(),
                s
        );

        float bottomV = lerp(
                vertices[3].texV(),
                vertices[2].texV(),
                s
        );

        return new Sample(
                position,
                lerp(topU, bottomU, t),
                lerp(topV, bottomV, t)
        );
    }

    private static float lerp(
            float a,
            float b,
            float delta
    ) {
        return a + (b - a) * delta;
    }

    private static Sample deform(
            Sample sample,
            BendContext context
    ) {
        Vector3f raw =
                geoToVanillaRaw(
                        sample.position()
                );

        Vector3f bent =
                applyBendyMath(
                        raw,
                        context.bounds(),
                        context.direction(),
                        context.bend().axis(),
                        context.bend().amount()
                );

        return new Sample(
                vanillaRawToGeo(bent),
                sample.u(),
                sample.v()
        );
    }

    /**
     * Equivalent bend calculation to Bendy-lib, operating on a GeckoLib
     * tessellated face instead of a vanilla ModelPart.Cube.
     */
    private static Vector3f applyBendyMath(
            Vector3f original,
            BendBounds bounds,
            Direction direction,
            float bendAxis,
            float bendValue
    ) {
        if (Math.abs(bendValue) < BEND_EPSILON) {
            return new Vector3f(original);
        }

        Vector3f axis = new Vector3f(
                (float)Math.cos(bendAxis),
                0.0F,
                (float)Math.sin(bendAxis)
        );

        axis.mul(
                new Matrix3f().set(
                        direction.getRotation()
                )
        );

        Vector3f center = bounds.center();

        Matrix4f transformMatrix =
                new Matrix4f()
                        .translate(center)
                        .rotate(bendValue, axis)
                        .translate(
                                -center.x,
                                -center.y,
                                -center.z
                        );

        Vector3f vertex1 =
                bounds.minCorner();
        Vector3f vertex7 =
                bounds.maxCorner();

        Vector3f directionVector =
                new Vector3f(direction.step());

        Plane aPlane =
                new Plane(
                        directionVector,
                        vertex7
                );

        Plane bPlane =
                new Plane(
                        directionVector,
                        vertex1
                );

        boolean invertedDirection =
                direction == Direction.UP
                        || direction == Direction.SOUTH
                        || direction == Direction.EAST;

        Plane basePlane =
                invertedDirection
                        ? aPlane
                        : bPlane;

        Plane otherPlane =
                invertedDirection
                        ? bPlane
                        : aPlane;

        float fullSize =
                -directionVector.dot(vertex1)
                        + directionVector.dot(vertex7);

        float halfSize = fullSize / 2.0F;

        if (Math.abs(halfSize) < 1.0E-6F) {
            return new Vector3f(original);
        }

        Vector3f bendPlaneNormal =
                new Vector3f(direction.step())
                        .cross(axis);

        Plane bendPlane =
                new Plane(
                        bendPlaneNormal,
                        center
                );

        float distanceFromBend =
                bendPlane.distanceTo(original);

        if (invertedDirection) {
            distanceFromBend =
                    -distanceFromBend;
        }

        float distanceFromBase =
                basePlane.distanceTo(original);

        float distanceFromOther =
                otherPlane.distanceTo(original);

        double shear =
                Math.tan(bendValue / 2.0F)
                        * distanceFromBend;

        Vector3f result =
                new Vector3f(original);

        Vector3f directionOffset =
                new Vector3f(direction.step());

        if (Math.abs(distanceFromBase)
                < Math.abs(distanceFromOther)) {
            directionOffset.mul(
                    (float)(
                            -distanceFromBase
                                    / halfSize
                                    * shear
                    )
            );

            result.add(directionOffset);

            Vector4f rotated =
                    new Vector4f(result, 1.0F)
                            .mul(transformMatrix);

            result.set(
                    rotated.x,
                    rotated.y,
                    rotated.z
            );
        }
        else {
            directionOffset.mul(
                    (float)(
                            -distanceFromOther
                                    / halfSize
                                    * shear
                    )
            );

            result.add(directionOffset);
        }

        return result;
    }

    private static void emit(
            VertexConsumer buffer,
            Matrix4f poseMatrix,
            Vector3f normal,
            Sample sample,
            int packedLight,
            int packedOverlay,
            int colour
    ) {
        Vector4f position =
                new Vector4f(
                        sample.position(),
                        1.0F
                ).mul(poseMatrix);

        buffer.addVertex(
                position.x(),
                position.y(),
                position.z(),
                colour,
                sample.u(),
                sample.v(),
                packedOverlay,
                packedLight,
                normal.x(),
                normal.y(),
                normal.z()
        );
    }

    private static Vector3f geoToVanillaRaw(
            Vector3f geo
    ) {
        return new Vector3f(
                -geo.x,
                GEO_MODEL_HEIGHT - geo.y,
                geo.z
        );
    }

    private static Vector3f vanillaRawToGeo(
            Vector3f raw
    ) {
        return new Vector3f(
                -raw.x,
                GEO_MODEL_HEIGHT - raw.y,
                raw.z
        );
    }

    private record BendContext(
            PlayerAnimatorCompat.Bend bend,
            Direction direction,
            BendBounds bounds
    ) {
    }

    private record BendBounds(
            float minX,
            float minY,
            float minZ,
            float maxX,
            float maxY,
            float maxZ
    ) {
        private static BendBounds fromCorners(
                Vector3f a,
                Vector3f b
        ) {
            return new BendBounds(
                    Math.min(a.x, b.x),
                    Math.min(a.y, b.y),
                    Math.min(a.z, b.z),
                    Math.max(a.x, b.x),
                    Math.max(a.y, b.y),
                    Math.max(a.z, b.z)
            );
        }

        private Vector3f center() {
            return new Vector3f(
                    (this.minX + this.maxX) * 0.5F,
                    (this.minY + this.maxY) * 0.5F,
                    (this.minZ + this.maxZ) * 0.5F
            );
        }

        private Vector3f minCorner() {
            return new Vector3f(
                    this.minX,
                    this.minY,
                    this.minZ
            );
        }

        private Vector3f maxCorner() {
            return new Vector3f(
                    this.maxX,
                    this.maxY,
                    this.maxZ
            );
        }
    }

    private record Sample(
            Vector3f position,
            float u,
            float v
    ) {
    }

    private static final class Plane {
        private final Vector3f normal;
        private final float normDistance;

        private Plane(
                Vector3f normal,
                Vector3f position
        ) {
            this.normal =
                    new Vector3f(normal)
                            .normalize();

            this.normDistance =
                    -this.normal.dot(position);
        }

        private float distanceTo(
                Vector3f position
        ) {
            return this.normal.dot(position)
                    + this.normDistance;
        }
    }
}
