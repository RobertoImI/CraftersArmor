package org.crafterscr.craftersarmor.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.crafterscr.craftersarmor.CraftersArmor;

public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(
                    Registries.CREATIVE_MODE_TAB,
                    CraftersArmor.MOD_ID
            );

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab>
            CRAFTERS_ARMOR_TAB =
            CREATIVE_TABS.register(
                    "crafters_armor",
                    () -> CreativeModeTab.builder()
                            .title(
                                    Component.translatable(
                                            "creativetab.craftersarmor.crafters_armor"
                                    )
                            )
                            .icon(
                                    () -> new ItemStack(
                                            ModItems.LIDER_COAT.get()
                                    )
                            )
                            .displayItems((parameters, output) -> {

                                // Líder
                                output.accept(ModItems.LIDER_COAT.get());
                                output.accept(ModItems.LIDER_CROWN.get());

                                // =====================================================
                                // CLAN TAKA
                                // =====================================================
                                output.accept(ModItems.CLAN_TAKA.get());
                                output.accept(ModItems.CLAN_TAKA_S1.get());
                                output.accept(ModItems.CLAN_TAKA_S2.get());

                                // =====================================================
                                // GUERREROS CELESTIALES DEL ABISMO
                                // =====================================================
                                output.accept(ModItems.CLAN_GUERREROS_CELESTIALES.get());
                                output.accept(ModItems.CLAN_GUERREROS_CELESTIALES_S1.get());
                                output.accept(ModItems.CLAN_GUERREROS_CELESTIALES_S2.get());

                                // =====================================================
                                // CLAN TSUKI
                                // =====================================================
                                output.accept(ModItems.CLAN_TSUKI.get());
                                output.accept(ModItems.CLAN_TSUKI_S1.get());
                                output.accept(ModItems.CLAN_TSUKI_S2.get());

                                // =====================================================
                                // CLAN ARCANUM MISTERY
                                // =====================================================
                                output.accept(ModItems.CLAN_ARCANUM_MISTERY.get());
                                output.accept(ModItems.CLAN_ARCANUM_MISTERY_S1.get());
                                output.accept(ModItems.CLAN_ARCANUM_MISTERY_S2.get());

                                // Gorras
                                output.accept(ModItems.CLAN_TAKA_GORRA.get());
                                output.accept(ModItems.CLAN_GUERREROS_CELESTIALES_GORRA.get());
                                output.accept(ModItems.CLAN_TSUKI_GORRA.get());
                                output.accept(ModItems.CLAN_ARCANUM_MISTERY_GORRA.get());

                                // Zapatos
                                output.accept(ModItems.CLAN_TAKA_ZAPATOS.get());
                                output.accept(ModItems.CLAN_GUERREROS_CELESTIALES_ZAPATOS.get());
                                output.accept(ModItems.CLAN_TSUKI_ZAPATOS.get());
                                output.accept(ModItems.CLAN_ARCANUM_MISTERY_ZAPATOS.get());
                            })
                            .build()
            );

    private ModCreativeTabs() {
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}