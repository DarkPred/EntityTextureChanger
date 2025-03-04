package com.github.darkpred.entitytexturechanger.network;

import com.github.darkpred.entitytexturechanger.capabilities.ModCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public class ClientPacketHandler {

    public static void handlePacket(int targetId, String url) {
        Entity target = Minecraft.getInstance().level.getEntity(targetId);
        if (target != null) {
            ModCapabilities.getMarkerCap(target).ifPresent(iMarkerCap -> iMarkerCap.setTextureUrl(url));
        }
    }
}
