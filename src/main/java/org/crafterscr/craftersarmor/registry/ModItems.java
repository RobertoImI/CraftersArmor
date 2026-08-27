package org.crafterscr.craftersarmor.registry;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import org.crafterscr.craftersarmor.CraftersArmor;
import org.crafterscr.craftersarmor.item.ClanCoatItem;
import org.crafterscr.craftersarmor.item.LiderCoatItem;

import org.crafterscr.craftersarmor.item.ClanHatItem;

import org.crafterscr.craftersarmor.item.LiderCrownItem;

import org.crafterscr.craftersarmor.item.ClanShoesItem;

public final class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(CraftersArmor.MOD_ID);


    // =========================================================
    // LÍDER
    // =========================================================

    public static final DeferredItem<LiderCoatItem> LIDER_COAT =
            ITEMS.register(
                    "lider_coat",
                    () -> new LiderCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties()
                                    .stacksTo(1)
                    )
            );


    // =========================================================
    // CLAN TAKA
    // =========================================================

    public static final DeferredItem<ClanCoatItem> CLAN_TAKA =
            ITEMS.register(
                    "clan_taka",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties()
                                    .stacksTo(1),
                            "clan_taka"
                    )
            );


    // =========================================================
    // GUERREROS CELESTIALES DEL ABISMO
    // =========================================================

    public static final DeferredItem<ClanCoatItem>
            CLAN_GUERREROS_CELESTIALES =
            ITEMS.register(
                    "clan_guerreroscelestiales",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties()
                                    .stacksTo(1),
                            "clan_guerreroscelestiales"
                    )
            );


    // =========================================================
    // CLAN TSUKI
    // =========================================================

    public static final DeferredItem<ClanCoatItem> CLAN_TSUKI =
            ITEMS.register(
                    "clan_tsuki",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties()
                                    .stacksTo(1),
                            "clan_tsuki"
                    )
            );


    // =========================================================
    // CLAN ARCANUM MISTERY
    // =========================================================

    public static final DeferredItem<ClanCoatItem>
            CLAN_ARCANUM_MISTERY =
            ITEMS.register(
                    "clan_arcanummistery",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties()
                                    .stacksTo(1),
                            "clan_arcanummistery"
                    )
            );

    // =========================================================
// GORRA - CLAN TAKA
// =========================================================

    public static final DeferredItem<ClanHatItem> CLAN_TAKA_GORRA =
            ITEMS.register(
                    "clan_taka_gorra",
                    () -> new ClanHatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.HELMET,
                            new Item.Properties()
                                    .stacksTo(1),
                            "clan_taka_gorra"
                    )
            );


// =========================================================
// GORRA - GUERREROS CELESTIALES DEL ABISMO
// =========================================================

    public static final DeferredItem<ClanHatItem>
            CLAN_GUERREROS_CELESTIALES_GORRA =
            ITEMS.register(
                    "clan_guerreroscelestiales_gorra",
                    () -> new ClanHatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.HELMET,
                            new Item.Properties()
                                    .stacksTo(1),
                            "clan_guerreroscelestiales_gorra"
                    )
            );


// =========================================================
// GORRA - CLAN TSUKI
// =========================================================

    public static final DeferredItem<ClanHatItem> CLAN_TSUKI_GORRA =
            ITEMS.register(
                    "clan_tsuki_gorra",
                    () -> new ClanHatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.HELMET,
                            new Item.Properties()
                                    .stacksTo(1),
                            "clan_tsuki_gorra"
                    )
            );


// =========================================================
// GORRA - CLAN ARCANUM MISTERY
// =========================================================

    public static final DeferredItem<ClanHatItem>
            CLAN_ARCANUM_MISTERY_GORRA =
            ITEMS.register(
                    "clan_arcanummistery_gorra",
                    () -> new ClanHatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.HELMET,
                            new Item.Properties()
                                    .stacksTo(1),
                            "clan_arcanummistery_gorra"
                    )
            );

    // =========================================================
// VARIANTES S1
// =========================================================

    public static final DeferredItem<ClanCoatItem> CLAN_TAKA_S1 =
            ITEMS.register(
                    "clan_taka_s1",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().stacksTo(1),
                            "clan_taka_s1"
                    )
            );

    public static final DeferredItem<ClanCoatItem> CLAN_GUERREROS_CELESTIALES_S1 =
            ITEMS.register(
                    "clan_guerreroscelestiales_s1",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().stacksTo(1),
                            "clan_guerreroscelestiales_s1"
                    )
            );

    public static final DeferredItem<ClanCoatItem> CLAN_TSUKI_S1 =
            ITEMS.register(
                    "clan_tsuki_s1",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().stacksTo(1),
                            "clan_tsuki_s1"
                    )
            );

    public static final DeferredItem<ClanCoatItem> CLAN_ARCANUM_MISTERY_S1 =
            ITEMS.register(
                    "clan_arcanummistery_s1",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().stacksTo(1),
                            "clan_arcanummistery_s1"
                    )
            );

// =========================================================
// VARIANTES S2
// =========================================================

    public static final DeferredItem<ClanCoatItem> CLAN_TAKA_S2 =
            ITEMS.register(
                    "clan_taka_s2",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().stacksTo(1),
                            "clan_taka_s2"
                    )
            );

    public static final DeferredItem<ClanCoatItem> CLAN_GUERREROS_CELESTIALES_S2 =
            ITEMS.register(
                    "clan_guerreroscelestiales_s2",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().stacksTo(1),
                            "clan_guerreroscelestiales_s2"
                    )
            );

    public static final DeferredItem<ClanCoatItem> CLAN_TSUKI_S2 =
            ITEMS.register(
                    "clan_tsuki_s2",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().stacksTo(1),
                            "clan_tsuki_s2"
                    )
            );

    public static final DeferredItem<ClanCoatItem> CLAN_ARCANUM_MISTERY_S2 =
            ITEMS.register(
                    "clan_arcanummistery_s2",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().stacksTo(1),
                            "clan_arcanummistery_s2"
                    )
            );

    private ModItems() {
    }

    // =========================================================
// CORONA DEL LÍDER
// =========================================================

    public static final DeferredItem<LiderCrownItem> LIDER_CROWN =
            ITEMS.register(
                    "lider_crown",
                    () -> new LiderCrownItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.HELMET,
                            new Item.Properties()
                                    .stacksTo(1)
                    )
            );

    // =========================================================
// ZAPATOS - CLAN TAKA
// =========================================================

    public static final DeferredItem<ClanShoesItem> CLAN_TAKA_ZAPATOS =
            ITEMS.register(
                    "clan_taka_zapatos",
                    () -> new ClanShoesItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.BOOTS,
                            new Item.Properties().stacksTo(1),
                            "clan_taka_zapatos"
                    )
            );


// =========================================================
// ZAPATOS - GUERREROS CELESTIALES DEL ABISMO
// =========================================================

    public static final DeferredItem<ClanShoesItem>
            CLAN_GUERREROS_CELESTIALES_ZAPATOS =
            ITEMS.register(
                    "clan_guerreroscelestiales_zapatos",
                    () -> new ClanShoesItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.BOOTS,
                            new Item.Properties().stacksTo(1),
                            "clan_guerreroscelestiales_zapatos"
                    )
            );


// =========================================================
// ZAPATOS - CLAN TSUKI
// =========================================================

    public static final DeferredItem<ClanShoesItem> CLAN_TSUKI_ZAPATOS =
            ITEMS.register(
                    "clan_tsuki_zapatos",
                    () -> new ClanShoesItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.BOOTS,
                            new Item.Properties().stacksTo(1),
                            "clan_tsuki_zapatos"
                    )
            );


// =========================================================
// ZAPATOS - ARCANUM MISTERY
// =========================================================

    public static final DeferredItem<ClanShoesItem>
            CLAN_ARCANUM_MISTERY_ZAPATOS =
            ITEMS.register(
                    "clan_arcanummistery_zapatos",
                    () -> new ClanShoesItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.BOOTS,
                            new Item.Properties().stacksTo(1),
                            "clan_arcanummistery_zapatos"
                    )
            );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}