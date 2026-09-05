package org.crafterscr.craftersarmor.client.renderer;

import net.minecraft.resources.ResourceLocation;

import org.crafterscr.craftersarmor.CraftersArmor;
import org.crafterscr.craftersarmor.item.ClanCoatItem;

import software.bernie.geckolib.model.GeoModel;

public final class ClanCoatRenderer
        extends LiveDynamicCoatArmorRenderer<ClanCoatItem> {

    public ClanCoatRenderer() {
        super(new ClanCoatModel());
    }

    private static final class ClanCoatModel
            extends GeoModel<ClanCoatItem> {

        private static final ResourceLocation MODEL =
                ResourceLocation.fromNamespaceAndPath(
                        CraftersArmor.MOD_ID,
                        "geo/gabardina_template.geo.json"
                );

        @Override
        public ResourceLocation getModelResource(
                ClanCoatItem animatable
        ) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(
                ClanCoatItem animatable
        ) {
            return ResourceLocation.fromNamespaceAndPath(
                    CraftersArmor.MOD_ID,
                    "textures/armor/"
                            + animatable.getTextureName()
                            + ".png"
            );
        }

        @Override
        public ResourceLocation getAnimationResource(
                ClanCoatItem animatable
        ) {
            return null;
        }
    }
}
