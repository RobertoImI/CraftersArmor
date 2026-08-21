package org.crafterscr.craftersarmor.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import org.crafterscr.craftersarmor.CraftersArmor;
import org.crafterscr.craftersarmor.item.ClanHatItem;

import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public final class ClanHatRenderer extends GeoArmorRenderer<ClanHatItem> {

    public ClanHatRenderer() {
        super(new ClanHatModel());
    }

    public void setCurrentPlayer(Player player) {
        // Se conserva para que ClanHatItem no necesite cambios.
    }

    private static final class ClanHatModel extends GeoModel<ClanHatItem> {

        private static final ResourceLocation MODEL =
                ResourceLocation.fromNamespaceAndPath(
                        CraftersArmor.MOD_ID,
                        "geo/gorra_template.geo.json"
                );

        @Override
        public ResourceLocation getModelResource(ClanHatItem animatable) {
            return MODEL;
        }

        @Override
        public ResourceLocation getTextureResource(ClanHatItem animatable) {
            return ResourceLocation.fromNamespaceAndPath(
                    CraftersArmor.MOD_ID,
                    "textures/armor/" + animatable.getTextureName() + ".png"
            );
        }

        @Override
        public ResourceLocation getAnimationResource(ClanHatItem animatable) {
            return null;
        }
    }
}
