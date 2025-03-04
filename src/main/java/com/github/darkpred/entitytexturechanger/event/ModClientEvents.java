package com.github.darkpred.entitytexturechanger.event;

import com.github.darkpred.entitytexturechanger.EntityTextureChanger;
import com.github.darkpred.entitytexturechanger.client.gui.TextureChangeScreen;
import com.github.darkpred.entitytexturechanger.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EntityTextureChanger.MOD_ID, value = Dist.CLIENT)
public class ModClientEvents {

    @SubscribeEvent
    public static void openScreen(PlayerInteractEvent.EntityInteractSpecific event) {
        if (event.getSide() == LogicalSide.CLIENT && event.getItemStack().is(ModItems.MARKER.get())) {
            Minecraft.getInstance().setScreen(new TextureChangeScreen(event.getTarget()));
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }
}
