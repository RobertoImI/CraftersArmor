package org.crafterscr.craftersarmor.client.renderer;

import net.minecraft.resources.ResourceLocation;

import org.crafterscr.craftersarmor.CraftersArmor;
import org.crafterscr.craftersarmor.item.PokemonHatItem;

import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public final class PokemonHatItemRenderer
        extends GeoItemRenderer<PokemonHatItem> {

    public PokemonHatItemRenderer() {
        super(new PokemonHatItemModel());
        useAlternateGuiLighting();
    }

    private static final class PokemonHatItemModel
            extends GeoModel<PokemonHatItem> {

        @Override
        public ResourceLocation getModelResource(
                PokemonHatItem animatable
        ) {
            return ResourceLocation.fromNamespaceAndPath(
                    CraftersArmor.MOD_ID,
                    "geo/item/"
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
