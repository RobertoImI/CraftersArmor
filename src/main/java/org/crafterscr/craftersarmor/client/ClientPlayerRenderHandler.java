package org.crafterscr.craftersarmor.client;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

import org.crafterscr.craftersarmor.CraftersArmor;
import org.crafterscr.craftersarmor.item.ClanCoatItem;
import org.crafterscr.craftersarmor.item.LiderCoatItem;

@EventBusSubscriber(
        modid = CraftersArmor.MOD_ID,
        value = Dist.CLIENT
)
public final class ClientPlayerRenderHandler {

    private ClientPlayerRenderHandler() {
    }

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        ItemStack chest =
                event.getEntity().getItemBySlot(EquipmentSlot.CHEST);

        boolean wearingCraftersCoat =
                chest.getItem() instanceof ClanCoatItem
                        || chest.getItem() instanceof LiderCoatItem;

        if (!wearingCraftersCoat) {
            return;
        }

        PlayerModel<?> model = event.getRenderer().getModel();
        model.leftSleeve.visible = false;
        model.rightSleeve.visible = false;
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        PlayerModel<?> model = event.getRenderer().getModel();

        model.leftSleeve.visible =
                event.getEntity().isModelPartShown(PlayerModelPart.LEFT_SLEEVE);

        model.rightSleeve.visible =
                event.getEntity().isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
    }
}