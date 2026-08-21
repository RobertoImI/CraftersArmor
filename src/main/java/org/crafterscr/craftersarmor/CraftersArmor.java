package org.crafterscr.craftersarmor;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.crafterscr.craftersarmor.registry.ModCreativeTabs;
import org.crafterscr.craftersarmor.registry.ModItems;

@Mod(CraftersArmor.MOD_ID)
public final class CraftersArmor {

    public static final String MOD_ID = "craftersarmor";

    public CraftersArmor(IEventBus modEventBus) {

        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
    }
}