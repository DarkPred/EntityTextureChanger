package com.github.darkpred.entitytexturechanger.event;

import com.github.darkpred.entitytexturechanger.EntityTextureChanger;
import com.github.darkpred.entitytexturechanger.capabilities.TexReplacementCapProvider;
import com.github.darkpred.entitytexturechanger.capabilities.ModCapabilities;
import com.github.darkpred.entitytexturechanger.network.MessageHandler;
import com.github.darkpred.entitytexturechanger.network.S2CSyncTextureMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = EntityTextureChanger.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof Player)) {
            TexReplacementCapProvider markerProvider = new TexReplacementCapProvider();
            event.addListener(markerProvider::invalidate);
            event.addCapability(TexReplacementCapProvider.ID, markerProvider);
        }
    }

    @SubscribeEvent
    public static void attachEntityCapabilities(PlayerEvent.StartTracking event) {
        ModCapabilities.getMarkerCap(event.getTarget()).ifPresent(iMarkerCap -> {
            if (iMarkerCap.hasTexture()) {
                MessageHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getPlayer()), new S2CSyncTextureMessage(event.getTarget().getId(), iMarkerCap.getTextureUrl()));
            }
        });
    }
}
