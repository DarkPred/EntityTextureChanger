package com.github.darkpred.entitytexturechanger.capabilities;

import com.github.darkpred.entitytexturechanger.EntityTextureChanger;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class TexReplacementCapProvider implements ICapabilitySerializable<CompoundTag> {
    public static final ResourceLocation ID = EntityTextureChanger.location("marker");

    public static final Capability<ITexReplacementCap> MARKER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    private final ITexReplacementCap instance = new TexReplacementCap();
    private final LazyOptional<ITexReplacementCap> optional = LazyOptional.of(() -> instance);

    @Override
    public CompoundTag serializeNBT() {
        return instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        instance.deserializeNBT(nbt);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction Dist) {
        return MARKER_CAPABILITY.orEmpty(cap, optional);
    }

    public void invalidate() {
        optional.invalidate();
    }
}
