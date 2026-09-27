package org.crafterscr.craftersarmor.client.renderer;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;

import org.crafterscr.craftersarmor.CraftersArmor;
import org.crafterscr.craftersarmor.item.PokemonHatItem;

import software.bernie.geckolib.model.GeoModel;

public final class PokemonHatArmorRenderer
        extends EmoteCompatibleArmorRenderer<PokemonHatItem> {

    private static final float HEADWEAR_BASE_Y = 29.25F;
    private static final float ARMOR_HEAD_PIVOT_Y = 23.5F;

    public PokemonHatArmorRenderer() {
        super(new PokemonHatArmorModel());
    }

    @Override
    protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
        super.applyBaseTransformations(baseModel);

        if (this.head == null
                || this.currentStack == null
                || !(this.currentStack.getItem() instanceof PokemonHatItem hat)) {
            return;
        }

        float scale = hat.getWearScale();

        /*
         * Scale around GeckoLib's armorHead pivot, then compensate Y so the
         * bottom of every imported hat remains at the same 29.25 px contact
         * line on the Minecraft head. Without this compensation, small hats
         * sink and enlarged hats float above the head.
         */
        float yCorrection =
                (1.0F - scale)
                        * (HEADWEAR_BASE_Y - ARMOR_HEAD_PIVOT_Y);

        this.head.setScaleX(this.head.getScaleX() * scale);
        this.head.setScaleY(this.head.getScaleY() * scale);
        this.head.setScaleZ(this.head.getScaleZ() * scale);
        this.head.setPosY(this.head.getPosY() + yCorrection);
    }

    private static final class PokemonHatArmorModel
            extends GeoModel<PokemonHatItem> {

        @Override
        public ResourceLocation getModelResource(
                PokemonHatItem animatable
        ) {
            return ResourceLocation.fromNamespaceAndPath(
                    CraftersArmor.MOD_ID,
                    "geo/headwear/"
                            + animatable.getAssetName()
                            + ".geo.json"
            );
        }

        @Override
        public ResourceLocation getTextureResource(
                PokemonHatItem animatable
        ) {
            return ResourceLocation.fromNamespaceAndPath(
                    CraftersArmor.MOD_ID,
                    "textures/armor/headwear/"
                            + animatable.getAssetName()
                            + ".png"
            );
        }

        @Override
        public ResourceLocation getAnimationResource(
                PokemonHatItem animatable
        ) {
            return null;
        }
    }
}
