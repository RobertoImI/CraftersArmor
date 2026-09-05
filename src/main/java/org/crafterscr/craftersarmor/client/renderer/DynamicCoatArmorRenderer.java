package org.crafterscr.craftersarmor.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.crafterscr.craftersarmor.client.compat.PlayerAnimatorCompat;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoCube;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.cache.object.GeoVertex;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.RenderUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Shared renderer for Crafters Armor coats.
 *
 * <p>GeckoLib already copies the final vanilla/Emotecraft rotations and
 * positions from the player's HumanoidModel. What it cannot copy is
 * PlayerAnimator/BendyLib's vertex deformation. This renderer fills that gap:
 * it reads the final bend pair and tessellates/deforms the existing GeckoLib
 * sleeve cube while preserving the original GEO and its UVs.</p>
 *
 * <p>The coat tails are separate child bones in the GEO. Their motion is small
 * and procedural: speed moves the rear panel backwards and the current leg
 * swing opens the side panels. It does not simulate full cloth physics, but it
 * prevents most leg clipping while keeping the coat stable.</p>
 */
public abstract class DynamicCoatArmorRenderer<T extends Item & GeoItem>
        extends GeoArmorRenderer<T> {

    private static final float EPSILON = 0.00001F;

    private final Map<UUID, Float> tailMotion = new HashMap<>();

    protected DynamicCoatArmorRenderer(GeoModel<T> model) {
        super(model);
    }

    @Override
    public void preRender(
            PoseStack poseStack,
            T animatable,
            BakedGeoModel model,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            boolean isReRender,
            float partialTick,
            int packedLight,
            int packedOverlay,
            int colour
    ) {
        /*
         * First let GeckoLib copy the final HumanoidModel pose. This includes
         * the normal arm/body rotations produced by Emotecraft.
         */
        super.preRender(
                poseStack,
                animatable,
                model,
                bufferSource,
                buffer,
                isReRender,
                partialTick,
                packedLight,
                packedOverlay,
                colour
        );

        if (!isReRender) {
            applyDynamicCoatMotion();
        }
    }

    /**
     * Dynamic motion for the rear and side coat panels.
     */
    private void applyDynamicCoatMotion() {
        Entity entity = this.currentEntity;
        if (entity == null || this.baseModel == null) {
            resetTailBones();
            return;
        }

        double horizontalSpeed = entity.getDeltaMovement().horizontalDistance();
        float rightLeg = this.baseModel.rightLeg.xRot;
        float leftLeg = this.baseModel.leftLeg.xRot;
        float legActivity = Math.max(Math.abs(rightLeg), Math.abs(leftLeg));

        float target = clamp(
                (float) horizontalSpeed * 1.45F + legActivity * 0.085F,
                0.0F,
                0.34F
        );

        if (entity.isSprinting()) {
            target = Math.min(0.40F, target + 0.07F);
        }

        UUID id = entity.getUUID();
        float previous = this.tailMotion.getOrDefault(id, 0.0F);
        float smoothed = previous + (target - previous) * 0.22F;
        this.tailMotion.put(id, smoothed);

        GeoBone back = bone("coatBack");
        GeoBone leftTail = bone("coatLeftTail");
        GeoBone rightTail = bone("coatRightTail");

        if (back != null) {
            // Negative X swings the lower coat backwards in GeckoLib's armor
            // coordinate system.
            back.setRotX(-smoothed);
        }

        float leftOpen = clamp(Math.abs(leftLeg) * 0.10F + smoothed * 0.15F, 0.0F, 0.18F);
        float rightOpen = clamp(Math.abs(rightLeg) * 0.10F + smoothed * 0.15F, 0.0F, 0.18F);

        if (leftTail != null) {
            leftTail.setRotX(-smoothed * 0.70F);
            leftTail.setRotZ(-leftOpen);
        }

        if (rightTail != null) {
            rightTail.setRotX(-smoothed * 0.70F);
            rightTail.setRotZ(rightOpen);
        }
    }

    private void resetTailBones() {
        GeoBone back = bone("coatBack");
        GeoBone leftTail = bone("coatLeftTail");
        GeoBone rightTail = bone("coatRightTail");

        if (back != null) {
            back.setRotX(0.0F);
        }

        if (leftTail != null) {
            leftTail.setRotX(0.0F);
            leftTail.setRotZ(0.0F);
        }

        if (rightTail != null) {
            rightTail.setRotX(0.0F);
            rightTail.setRotZ(0.0F);
        }
    }

    private GeoBone bone(String name) {
        return this.getGeoModel()
                .getAnimationProcessor()
                .getBone(name);
    }

    /**
     * Render arm cubes with continuous vertex deformation when PlayerAnimator
     * reports a bend. No arm is split into upper/lower pieces, so the original
     * UV layout remains continuous.
     */
    @Override
    public void renderCubesOfBone(
            PoseStack poseStack,
            GeoBone bone,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            int colour
    ) {
        PlayerAnimatorCompat.BendPose bend = bendForBone(bone);

        if (bend == null || !bend.active() || bone.getCubes().isEmpty()) {
            super.renderCubesOfBone(
                    poseStack,
                    bone,
                    buffer,
                    packedLight,
                    packedOverlay,
                    colour
            );
            return;
        }

        GeoCube referenceCube = largestVerticalCube(bone);
        BendVolume bendVolume = BendVolume.from(referenceCube);

        if (bendVolume == null) {
            super.renderCubesOfBone(
                    poseStack,
                    bone,
                    buffer,
                    packedLight,
                    packedOverlay,
                    colour
            );
            return;
        }

        for (GeoCube cube : bone.getCubes()) {
            poseStack.pushPose();
            RenderUtil.translateToPivotPoint(poseStack, cube);
            RenderUtil.rotateMatrixAroundCube(poseStack, cube);
            RenderUtil.translateAwayFromPivotPoint(poseStack, cube);

            renderBentCube(
                    poseStack,
                    cube,
                    bendVolume,
                    bend,
                    buffer,
                    packedLight,
                    packedOverlay,
                    colour
            );

            poseStack.popPose();
        }
    }

    private PlayerAnimatorCompat.BendPose bendForBone(GeoBone bone) {
        if (this.currentEntity == null) {
            return PlayerAnimatorCompat.BendPose.NONE;
        }

        return switch (bone.getName()) {
            case "armorRightArm" -> PlayerAnimatorCompat.getBend(this.currentEntity, "rightArm");
            case "armorLeftArm" -> PlayerAnimatorCompat.getBend(this.currentEntity, "leftArm");
            default -> PlayerAnimatorCompat.BendPose.NONE;
        };
    }

    private static GeoCube largestVerticalCube(GeoBone bone) {
        GeoCube best = bone.getCubes().getFirst();

        for (GeoCube cube : bone.getCubes()) {
            if (cube.size().y() > best.size().y()) {
                best = cube;
            }
        }

        return best;
    }

    private static void renderBentCube(
            PoseStack poseStack,
            GeoCube cube,
            BendVolume volume,
            PlayerAnimatorCompat.BendPose bend,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            int colour
    ) {
        for (GeoQuad quad : cube.quads()) {
            if (quad == null) {
                continue;
            }

            renderBentQuad(
                    poseStack,
                    quad,
                    volume,
                    bend,
                    buffer,
                    packedLight,
                    packedOverlay,
                    colour
            );
        }
    }

    /**
     * Tessellates a face along its vertical direction. GeckoLib normally has
     * only the four corner vertices of a box face; adding intermediate rows is
     * what lets the sleeve curve instead of behaving like two rigid pieces.
     */
    private static void renderBentQuad(
            PoseStack poseStack,
            GeoQuad quad,
            BendVolume volume,
            PlayerAnimatorCompat.BendPose bend,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            int colour
    ) {
        GeoVertex[] vertices = quad.vertices();

        float dy01 = Math.abs(vertices[1].position().y() - vertices[0].position().y());
        float dy12 = Math.abs(vertices[2].position().y() - vertices[1].position().y());
        float verticalSpan = Math.max(dy01, dy12);

        if (verticalSpan < EPSILON) {
            emitBentQuad(
                    poseStack,
                    quad,
                    volume,
                    bend,
                    vertices[0],
                    vertices[1],
                    vertices[2],
                    vertices[3],
                    buffer,
                    packedLight,
                    packedOverlay,
                    colour
            );
            return;
        }

        int segments = Math.max(2, Math.min(12, Math.round(verticalSpan * 16.0F)));

        if (dy01 >= dy12) {
            // Vertical edges: 0->1 and 3->2
            for (int i = 0; i < segments; i++) {
                float t0 = i / (float) segments;
                float t1 = (i + 1) / (float) segments;

                emitBentQuad(
                        poseStack,
                        quad,
                        volume,
                        bend,
                        lerp(vertices[0], vertices[1], t0),
                        lerp(vertices[0], vertices[1], t1),
                        lerp(vertices[3], vertices[2], t1),
                        lerp(vertices[3], vertices[2], t0),
                        buffer,
                        packedLight,
                        packedOverlay,
                        colour
                );
            }
        }
        else {
            // Vertical edges: 0->3 and 1->2
            for (int i = 0; i < segments; i++) {
                float t0 = i / (float) segments;
                float t1 = (i + 1) / (float) segments;

                emitBentQuad(
                        poseStack,
                        quad,
                        volume,
                        bend,
                        lerp(vertices[0], vertices[3], t0),
                        lerp(vertices[1], vertices[2], t0),
                        lerp(vertices[1], vertices[2], t1),
                        lerp(vertices[0], vertices[3], t1),
                        buffer,
                        packedLight,
                        packedOverlay,
                        colour
                );
            }
        }
    }

    private static void emitBentQuad(
            PoseStack poseStack,
            GeoQuad sourceQuad,
            BendVolume volume,
            PlayerAnimatorCompat.BendPose bend,
            GeoVertex a,
            GeoVertex b,
            GeoVertex c,
            GeoVertex d,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            int colour
    ) {
        Vector3f p0 = volume.bend(a.position(), bend.axis(), bend.angle());
        Vector3f p1 = volume.bend(b.position(), bend.axis(), bend.angle());
        Vector3f p2 = volume.bend(c.position(), bend.axis(), bend.angle());
        Vector3f p3 = volume.bend(d.position(), bend.axis(), bend.angle());

        Vector3f edge1 = new Vector3f(p1).sub(p0);
        Vector3f edge2 = new Vector3f(p2).sub(p0);
        Vector3f normal = edge1.cross(edge2);

        if (normal.lengthSquared() < EPSILON) {
            normal.set(sourceQuad.normal());
        }
        else {
            normal.normalize();

            if (normal.dot(sourceQuad.normal()) < 0.0F) {
                normal.negate();
            }
        }

        normal.mul(poseStack.last().normal()).normalize();

        emitVertex(poseStack, p0, a, normal, buffer, packedLight, packedOverlay, colour);
        emitVertex(poseStack, p1, b, normal, buffer, packedLight, packedOverlay, colour);
        emitVertex(poseStack, p2, c, normal, buffer, packedLight, packedOverlay, colour);
        emitVertex(poseStack, p3, d, normal, buffer, packedLight, packedOverlay, colour);
    }

    private static void emitVertex(
            PoseStack poseStack,
            Vector3f position,
            GeoVertex uvSource,
            Vector3f normal,
            VertexConsumer buffer,
            int packedLight,
            int packedOverlay,
            int colour
    ) {
        Vector4f transformed = new Vector4f(position, 1.0F);
        transformed.mul(poseStack.last().pose());

        buffer.addVertex(
                transformed.x(),
                transformed.y(),
                transformed.z(),
                colour,
                uvSource.texU(),
                uvSource.texV(),
                packedOverlay,
                packedLight,
                normal.x(),
                normal.y(),
                normal.z()
        );
    }

    private static GeoVertex lerp(GeoVertex from, GeoVertex to, float delta) {
        Vector3f pos = new Vector3f(from.position()).lerp(to.position(), delta);
        float u = from.texU() + (to.texU() - from.texU()) * delta;
        float v = from.texV() + (to.texV() - from.texV()) * delta;

        return new GeoVertex(pos, u, v);
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Minimal adaptation of BendyLib's continuous cuboid bend math for a
     * GeckoLib cube. Coordinates here are already GeckoLib world/model units.
     */
    private record BendVolume(
            float minX,
            float minY,
            float minZ,
            float maxX,
            float maxY,
            float maxZ
    ) {

        static BendVolume from(GeoCube cube) {
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
                    Vector3f p = vertex.position();
                    minX = Math.min(minX, p.x());
                    minY = Math.min(minY, p.y());
                    minZ = Math.min(minZ, p.z());
                    maxX = Math.max(maxX, p.x());
                    maxY = Math.max(maxY, p.y());
                    maxZ = Math.max(maxZ, p.z());
                }
            }

            if (!Float.isFinite(minX) || Math.abs(maxY - minY) < EPSILON) {
                return null;
            }

            return new BendVolume(minX, minY, minZ, maxX, maxY, maxZ);
        }

        Vector3f bend(Vector3f source, float bendAxis, float bendValue) {
            Vector3f result = new Vector3f(source);

            float centerX = (this.minX + this.maxX) * 0.5F;
            float centerY = (this.minY + this.maxY) * 0.5F;
            float centerZ = (this.minZ + this.maxZ) * 0.5F;
            float halfHeight = (this.maxY - this.minY) * 0.5F;

            if (halfHeight < EPSILON || Math.abs(bendValue) < 0.0001F) {
                return result;
            }

            float axisX = (float) Math.cos(bendAxis);
            float axisZ = (float) Math.sin(bendAxis);

            // Direction.UP cross bend-axis, matching BendyLib's arm setup.
            float planeX = axisZ;
            float planeZ = -axisX;

            // Direction.UP is one of BendyLib's inverted bend directions.
            float distanceFromBend = -(
                    planeX * (result.x() - centerX)
                            + planeZ * (result.z() - centerZ)
            );

            float distanceFromBase = result.y() - this.maxY;
            float distanceFromOther = result.y() - this.minY;
            float shear = (float) Math.tan(bendValue * 0.5F) * distanceFromBend;

            if (Math.abs(distanceFromBase) < Math.abs(distanceFromOther)) {
                result.y += (-distanceFromBase / halfHeight) * shear;
                rotateAroundAxis(result, centerX, centerY, centerZ, axisX, axisZ, bendValue);
            }
            else {
                result.y += (-distanceFromOther / halfHeight) * shear;
            }

            return result;
        }

        private static void rotateAroundAxis(
                Vector3f point,
                float centerX,
                float centerY,
                float centerZ,
                float axisX,
                float axisZ,
                float angle
        ) {
            point.sub(centerX, centerY, centerZ);

            // Rodrigues' rotation formula around (axisX, 0, axisZ).
            float x = point.x();
            float y = point.y();
            float z = point.z();
            float cos = (float) Math.cos(angle);
            float sin = (float) Math.sin(angle);
            float dot = x * axisX + z * axisZ;

            float rx = x * cos - axisZ * y * sin + axisX * dot * (1.0F - cos);
            float ry = y * cos + (axisZ * x - axisX * z) * sin;
            float rz = z * cos + axisX * y * sin + axisZ * dot * (1.0F - cos);

            point.set(
                    rx + centerX,
                    ry + centerY,
                    rz + centerZ
            );
        }
    }
}
