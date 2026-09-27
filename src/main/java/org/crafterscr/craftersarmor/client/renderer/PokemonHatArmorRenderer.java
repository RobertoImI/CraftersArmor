package org.crafterscr.craftersarmor.client.renderer;

import net.minecraft.resources.ResourceLocation;

import org.crafterscr.craftersarmor.CraftersArmor;
import org.crafterscr.craftersarmor.item.PokemonHatItem;

import software.bernie.geckolib.model.GeoModel;

public final class PokemonHatArmorRenderer
        extends EmoteCompatibleArmorRenderer<PokemonHatItem> {

    public PokemonHatArmorRenderer() {
        super(new PokemonHatArmorModel());
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
