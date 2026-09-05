package org.crafterscr.craftersarmor.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Final coat renderer stage for procedural cloth motion.
 *
 * <p>GeckoLib evaluates/reset animations inside actuallyRender, which happens
 * after preRender. Therefore coat-tail rotations must be applied immediately
 * before each tail bone is recursively rendered. This class provides that
 * late hook while {@link DynamicCoatArmorRenderer} handles continuous sleeve
 * bending.</p>
 */
public abstract class LiveDynamicCoatArmorRenderer<T extends Item & GeoItem>
        extends DynamicCoatArmorRenderer<T> {

    private final Map<UUID, Float> liveTailMotion = new HashMap<>();

    protected LiveDynamicCoatArmorRenderer(GeoModel<T> model) {
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
        if (!isReRender) {
            applyLiveTailPose(bone);
        }

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

    private void applyLiveTailPose(GeoBone bone) {
        String name = bone.getName();

        if (!name.equals("coatBack")
                && !name.equals("coatLeftTail")
                && !name.equals("coatRightTail")) {
            return;
        }

        Entity entity = this.currentEntity;
        if (entity == null || this.baseModel == null) {
            bone.setRotX(0.0F);
            bone.setRotZ(0.0F);
            return;
        }

        float rightLeg = this.baseModel.rightLeg.xRot;
        float leftLeg = this.baseModel.leftLeg.xRot;
        float legActivity = Math.max(Math.abs(rightLeg), Math.abs(leftLeg));
        double horizontalSpeed = entity.getDeltaMovement().horizontalDistance();

        float target = clamp(
                (float) horizontalSpeed * 1.45F + legActivity * 0.085F,
                0.0F,
                0.34F
        );

        if (entity.isSprinting()) {
            target = Math.min(0.40F, target + 0.07F);
        }

        UUID id = entity.getUUID();
        float current = this.liveTailMotion.getOrDefault(id, 0.0F);
        current += (target - current) * 0.22F;
        this.liveTailMotion.put(id, current);

        switch (name) {
            case "coatBack" -> {
                bone.setRotX(-current);
                bone.setRotZ(0.0F);
            }
            case "coatLeftTail" -> {
                float open = clamp(
                        Math.abs(leftLeg) * 0.10F + current * 0.15F,
                        0.0F,
                        0.18F
                );

                bone.setRotX(-current * 0.70F);
                bone.setRotZ(-open);
            }
            case "coatRightTail" -> {
                float open = clamp(
                        Math.abs(rightLeg) * 0.10F + current * 0.15F,
                        0.0F,
                        0.18F
                );

                bone.setRotX(-current * 0.70F);
                bone.setRotZ(open);
            }
            default -> {
            }
        }
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
