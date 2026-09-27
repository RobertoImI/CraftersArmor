package org.crafterscr.craftersarmor.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import org.crafterscr.craftersarmor.client.renderer.PokemonHatArmorRenderer;
import org.crafterscr.craftersarmor.client.renderer.PokemonHatItemRenderer;
import org.jetbrains.annotations.Nullable;

import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public final class PokemonHatItem extends ArmorItem implements GeoItem {

    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);

    private final String assetName;

    public PokemonHatItem(
            Holder<ArmorMaterial> armorMaterial,
            Type type,
            Properties properties,
            String assetName
    ) {
        super(armorMaterial, type, properties);
        this.assetName = assetName;
    }

    public String getAssetName() {
        return this.assetName;
    }

    /**
     * The source Pokémon headwear models were exported at different scales.
     * These values normalize the part that actually sits around the player's
     * head to roughly the same 8-9 px footprint as the existing CraftersArmor
     * caps, while keeping each model's brim/tail proportions intact.
     */
    public float getWearScale() {
        return switch (this.assetName) {
            case "hatblack" -> 0.71F;
            case "hathilbert" -> 1.31F;
            case "hatmay" -> 1.21F;
            case "hatserena" -> 1.42F;
            default -> 1.0F;
        };
    }

    /**
     * Independent inventory scale. This is intentionally not the same as the
     * equipped scale because the full silhouette of every hat has a different
     * width/depth (brims, bandana tail, etc.).
     */
    public float getInventoryScale() {
        return switch (this.assetName) {
            case "hatblack" -> 0.68F;
            case "hathilbert" -> 1.23F;
            case "hatmay" -> 1.10F;
            case "hatserena" -> 1.28F;
            default -> 1.0F;
        };
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {

            private GeoArmorRenderer<?> armorRenderer;
            private BlockEntityWithoutLevelRenderer itemRenderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.itemRenderer == null) {
                    this.itemRenderer =
                            new PokemonHatItemRenderer(PokemonHatItem.this);
                }

                return this.itemRenderer;
            }

            @Override
            public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(
                    @Nullable T livingEntity,
                    ItemStack itemStack,
                    @Nullable EquipmentSlot equipmentSlot,
                    @Nullable HumanoidModel<T> original
            ) {
                if (this.armorRenderer == null) {
                    this.armorRenderer = new PokemonHatArmorRenderer();
                }

                return this.armorRenderer;
            }
        });
    }

    @Override
    public void registerControllers(
            AnimatableManager.ControllerRegistrar controllers
    ) {
        // Sin animaciones propias.
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
