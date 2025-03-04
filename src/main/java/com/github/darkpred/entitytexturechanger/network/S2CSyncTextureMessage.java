package com.github.darkpred.entitytexturechanger.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CSyncTextureMessage {
    private final int targetId;
    private final String url;

    public S2CSyncTextureMessage(FriendlyByteBuf buf) {
        this.targetId = buf.readInt();
        this.url = buf.readUtf();
    }

    public S2CSyncTextureMessage(int targetId, String url) {
        this.targetId = targetId;
        this.url = url;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(targetId);
        buf.writeUtf(url);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPacketHandler.handlePacket(targetId, url)));
        ctx.get().setPacketHandled(true);
    }
}
