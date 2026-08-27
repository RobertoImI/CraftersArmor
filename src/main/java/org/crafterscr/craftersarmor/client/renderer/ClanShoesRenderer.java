package org.crafterscr.craftersarmor.client.renderer;

import net.minecraft.resources.ResourceLocation;

import org.crafterscr.craftersarmor.CraftersArmor;
import org.crafterscr.craftersarmor.item.ClanShoesItem;

import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public final class ClanShoesRenderer
        extends GeoArmorRenderer<ClanShoesItem> {

    public ClanShoesRenderer() {
        super(new ClanShoesModel());
    }

    private static final class ClanShoesModel
            extends GeoModel<ClanShoesItem> {

        private static final ResourceLocation MODEL =
                ResourceLocation.fromNamespaceAndPath(
                        CraftersArmor.MOD_ID,
                        "geo/zapatos_template.geo.json"
                );

        @Override
        public ResourceLocation getModelResource(
                ClanShoesItem animatable
        ) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(
                ClanShoesItem animatable
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
                ClanShoesItem animatable
        ) {
            return null;
        }
    }
}