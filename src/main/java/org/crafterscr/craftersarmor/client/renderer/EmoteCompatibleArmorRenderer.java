package org.crafterscr.craftersarmor.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import org.crafterscr.craftersarmor.client.compat.PlayerAnimatorCompat;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

/**
 * GeoArmorRenderer with optional Player Animator / Emotecraft bend support.
 *
 * <p>GeoArmorRenderer already copies Player Animator's position and ordinary
 * rotation from the vanilla player model. Bendy-lib's limb bend, however,
 * deforms the vanilla cuboid without changing that ModelPart rotation. The
 * special child bones in CraftersArmor's geo models represent the lower half
 * of the limb, so this renderer applies the missing bend to those bones.</p>
 */
public abstract class EmoteCompatibleArmorRenderer<T extends Item & GeoItem>
        extends GeoArmorRenderer<T> {

    protected EmoteCompatibleArmorRenderer(GeoModel<T> model) {
        super(model);
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
        String playerPart = playerPartForBone(bone.getName());

        if (playerPart == null) {
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
            return;
        }

        PlayerAnimatorCompat.Bend bend =
                PlayerAnimatorCompat.getBend(this.currentEntity, playerPart);

        if (!bend.isActive()) {
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
            return;
        }

        float originalX = bone.getRotX();
        float originalY = bone.getRotY();
        float originalZ = bone.getRotZ();

        /*
         * Player Animator uses this same lower-limb axis convention when
         * transforming children of a bent ModelPart:
         * axis = (cos(-bendAxis), 0, sin(-bendAxis)).
         *
         * GeckoLib's model-space rotation convention differs from ModelPart:
         * X/Y are inverted while Z keeps its sign (see RenderUtil#matchModelPartRot).
         */
        float axis = -bend.axis();
        Quaternionf rotation = new Quaternionf().rotateAxis(
                bend.amount(),
                (float)Math.cos(axis),
                0.0F,
                (float)Math.sin(axis)
        );

        Vector3f euler = rotation.getEulerAnglesXYZ(new Vector3f());

        bone.updateRotation(
                originalX - euler.x,
                originalY - euler.y,
                originalZ + euler.z
        );

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
            bone.updateRotation(originalX, originalY, originalZ);
        }
    }

    private static String playerPartForBone(String boneName) {
        return switch (boneName) {
            case "emoteRightForearm" -> "rightArm";
            case "emoteLeftForearm" -> "leftArm";
            case "emoteRightLowerLeg" -> "rightLeg";
            case "emoteLeftLowerLeg" -> "leftLeg";
            default -> null;
        };
    }
}
