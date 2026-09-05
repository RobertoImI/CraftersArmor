package org.crafterscr.craftersarmor.client.renderer;

import net.minecraft.resources.ResourceLocation;

import org.crafterscr.craftersarmor.CraftersArmor;
import org.crafterscr.craftersarmor.item.LiderCoatItem;

import software.bernie.geckolib.model.GeoModel;

public final class LiderCoatRenderer
        extends DynamicCoatArmorRenderer<LiderCoatItem> {

    public LiderCoatRenderer() {
        super(new LiderCoatModel());
    }

    private static final class LiderCoatModel
            extends GeoModel<LiderCoatItem> {

        private static final ResourceLocation MODEL =
                ResourceLocation.fromNamespaceAndPath(
                        CraftersArmor.MOD_ID,
                        "geo/lider_coat.geo.json"
                );

        private static final ResourceLocation TEXTURE =
                ResourceLocation.fromNamespaceAndPath(
                        CraftersArmor.MOD_ID,
                        "textures/armor/lider_coat.png"
                );

        @Override
        public ResourceLocation getModelResource(
                LiderCoatItem animatable
        ) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(
                LiderCoatItem animatable
        ) {
            return TEXTURE;
        }

        @Override
        public ResourceLocation getAnimationResource(
                LiderCoatItem animatable
        ) {
            return null;
        }
    }
}
