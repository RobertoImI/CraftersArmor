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
import org.crafterscr.craftersarmor.item.PokemonHatItem;

public final class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(CraftersArmor.MOD_ID);


    // =========================================================
    // CAMPEÓN
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

    public static final DeferredItem<ClanShoesItem> LIDER_ZAPATOS =
            ITEMS.register(
                    "lider_zapatos",
                    () -> new ClanShoesItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.BOOTS,
                            new Item.Properties().stacksTo(1),
                            "lider_zapatos"
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
// CORONA DEL CAMPEÓN
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


    // =========================================================
    // GIMNASIOS
    // =========================================================

    public static final DeferredItem<ClanCoatItem> GYM_FUEGO =
            ITEMS.register(
                    "gym_fuego",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().stacksTo(1),
                            "gym_fuego"
                    )
            );

    public static final DeferredItem<ClanShoesItem> GYM_FUEGO_ZAPATOS =
            ITEMS.register(
                    "gym_fuego_zapatos",
                    () -> new ClanShoesItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.BOOTS,
                            new Item.Properties().stacksTo(1),
                            "gym_fuego_zapatos"
                    )
            );

    public static final DeferredItem<ClanCoatItem> GYM_ELECTRICO =
            ITEMS.register(
                    "gym_electrico",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().stacksTo(1),
                            "gym_electrico"
                    )
            );

    public static final DeferredItem<ClanShoesItem> GYM_ELECTRICO_ZAPATOS =
            ITEMS.register(
                    "gym_electrico_zapatos",
                    () -> new ClanShoesItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.BOOTS,
                            new Item.Properties().stacksTo(1),
                            "gym_electrico_zapatos"
                    )
            );

    public static final DeferredItem<ClanCoatItem> GYM_FANTASMA =
            ITEMS.register(
                    "gym_fantasma",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().stacksTo(1),
                            "gym_fantasma"
                    )
            );

    public static final DeferredItem<ClanShoesItem> GYM_FANTASMA_ZAPATOS =
            ITEMS.register(
                    "gym_fantasma_zapatos",
                    () -> new ClanShoesItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.BOOTS,
                            new Item.Properties().stacksTo(1),
                            "gym_fantasma_zapatos"
                    )
            );

    public static final DeferredItem<ClanCoatItem> GYM_HADA =
            ITEMS.register(
                    "gym_hada",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().stacksTo(1),
                            "gym_hada"
                    )
            );

    public static final DeferredItem<ClanShoesItem> GYM_HADA_ZAPATOS =
            ITEMS.register(
                    "gym_hada_zapatos",
                    () -> new ClanShoesItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.BOOTS,
                            new Item.Properties().stacksTo(1),
                            "gym_hada_zapatos"
                    )
            );

    public static final DeferredItem<ClanCoatItem> GYM_HIELO =
            ITEMS.register(
                    "gym_hielo",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().stacksTo(1),
                            "gym_hielo"
                    )
            );

    public static final DeferredItem<ClanShoesItem> GYM_HIELO_ZAPATOS =
            ITEMS.register(
                    "gym_hielo_zapatos",
                    () -> new ClanShoesItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.BOOTS,
                            new Item.Properties().stacksTo(1),
                            "gym_hielo_zapatos"
                    )
            );

    public static final DeferredItem<ClanCoatItem> GYM_PLANTA =
            ITEMS.register(
                    "gym_planta",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().stacksTo(1),
                            "gym_planta"
                    )
            );

    public static final DeferredItem<ClanShoesItem> GYM_PLANTA_ZAPATOS =
            ITEMS.register(
                    "gym_planta_zapatos",
                    () -> new ClanShoesItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.BOOTS,
                            new Item.Properties().stacksTo(1),
                            "gym_planta_zapatos"
                    )
            );

    public static final DeferredItem<ClanCoatItem> GYM_SINIESTRO =
            ITEMS.register(
                    "gym_siniestro",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().stacksTo(1),
                            "gym_siniestro"
                    )
            );

    public static final DeferredItem<ClanShoesItem> GYM_SINIESTRO_ZAPATOS =
            ITEMS.register(
                    "gym_siniestro_zapatos",
                    () -> new ClanShoesItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.BOOTS,
                            new Item.Properties().stacksTo(1),
                            "gym_siniestro_zapatos"
                    )
            );

    public static final DeferredItem<ClanCoatItem> GYM_DRAGON =
            ITEMS.register(
                    "gym_dragon",
                    () -> new ClanCoatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.CHESTPLATE,
                            new Item.Properties().stacksTo(1),
                            "gym_dragon"
                    )
            );

    public static final DeferredItem<ClanShoesItem> GYM_DRAGON_ZAPATOS =
            ITEMS.register(
                    "gym_dragon_zapatos",
                    () -> new ClanShoesItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.BOOTS,
                            new Item.Properties().stacksTo(1),
                            "gym_dragon_zapatos"
                    )
            );

    // =========================================================
    // ACCESORIOS DE CABEZA - POKÉMON
    // =========================================================

    public static final DeferredItem<PokemonHatItem> HAT_BLACK =
            ITEMS.register(
                    "hatblack",
                    () -> new PokemonHatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.HELMET,
                            new Item.Properties().stacksTo(1),
                            "hatblack"
                    )
            );

    public static final DeferredItem<PokemonHatItem> HAT_HILBERT =
            ITEMS.register(
                    "hathilbert",
                    () -> new PokemonHatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.HELMET,
                            new Item.Properties().stacksTo(1),
                            "hathilbert"
                    )
            );

    public static final DeferredItem<PokemonHatItem> HAT_MAY =
            ITEMS.register(
                    "hatmay",
                    () -> new PokemonHatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.HELMET,
                            new Item.Properties().stacksTo(1),
                            "hatmay"
                    )
            );

    public static final DeferredItem<PokemonHatItem> HAT_SERENA =
            ITEMS.register(
                    "hatserena",
                    () -> new PokemonHatItem(
                            ArmorMaterials.NETHERITE,
                            ArmorItem.Type.HELMET,
                            new Item.Properties().stacksTo(1),
                            "hatserena"
                    )
            );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}