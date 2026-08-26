package org.crafterscr.craftersarmor.client.renderer;

import net.minecraft.resources.ResourceLocation;

import org.crafterscr.craftersarmor.CraftersArmor;
import org.crafterscr.craftersarmor.item.LiderCrownItem;

import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public final class LiderCrownRenderer
        extends GeoArmorRenderer<LiderCrownItem> {

    public LiderCrownRenderer() {
        super(new LiderCrownModel());
    }

    private static final class LiderCrownModel
            extends GeoModel<LiderCrownItem> {

        private static final ResourceLocation MODEL =
                ResourceLocation.fromNamespaceAndPath(
                        CraftersArmor.MOD_ID,
                        "geo/corona_lider.geo.json"
                );

        private static final ResourceLocation TEXTURE =
                ResourceLocation.fromNamespaceAndPath(
                        CraftersArmor.MOD_ID,
                        "textures/armor/corona_lider.png"
                );

        @Override
        public ResourceLocation getModelResource(
                LiderCrownItem animatable
        ) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(
                LiderCrownItem animatable
        ) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(
                LiderCrownItem animatable
        ) {
            return null;
        }
    }
}