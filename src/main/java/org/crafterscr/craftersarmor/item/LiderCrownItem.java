package org.crafterscr.craftersarmor.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import org.crafterscr.craftersarmor.client.renderer.LiderCrownRenderer;
import org.jetbrains.annotations.Nullable;

import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public final class LiderCrownItem
        extends ArmorItem
        implements GeoItem {

    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);

    public LiderCrownItem(
            Holder<ArmorMaterial> armorMaterial,
            Type type,
            Properties properties
    ) {
        super(
                armorMaterial,
                type,
                properties
        );
    }

    @Override
    public void createGeoRenderer(
            Consumer<GeoRenderProvider> consumer
    ) {

        consumer.accept(new GeoRenderProvider() {

            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity>
            HumanoidModel<?> getGeoArmorRenderer(
                    @Nullable T livingEntity,
                    ItemStack itemStack,
                    @Nullable EquipmentSlot equipmentSlot,
                    @Nullable HumanoidModel<T> original
            ) {

                if (this.renderer == null) {
                    this.renderer =
                            new LiderCrownRenderer();
                }

                return this.renderer;
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
    public AnimatableInstanceCache
    getAnimatableInstanceCache() {
        return this.cache;
    }
}