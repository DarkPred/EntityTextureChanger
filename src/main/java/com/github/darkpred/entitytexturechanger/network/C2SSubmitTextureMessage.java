package com.github.darkpred.entitytexturechanger.network;

import com.github.darkpred.entitytexturechanger.capabilities.ModCapabilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class C2SSubmitTextureMessage {
    private final int targetId;
    private final String url;

    public C2SSubmitTextureMessage(FriendlyByteBuf buf) {
        this.targetId = buf.readInt();
        this.url = buf.readUtf();
    }

    public C2SSubmitTextureMessage(int targetId, String url) {
        this.targetId = targetId;
        this.url = url;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(targetId);
        buf.writeUtf(url);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                Entity target = player.level.getEntity(targetId);
                if (target != null) {
                    ModCapabilities.getMarkerCap(target).ifPresent(iMarkerCap -> {
                        iMarkerCap.setTextureUrl(url);
                        MessageHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> target), new S2CSyncTextureMessage(targetId, url));
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
