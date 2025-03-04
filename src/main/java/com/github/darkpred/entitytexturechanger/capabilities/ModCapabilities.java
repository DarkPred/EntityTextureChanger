package com.github.darkpred.entitytexturechanger.capabilities;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ModCapabilities {
    private static final ConcurrentMap<Entity, LazyOptional<ITexReplacementCap>> cachedMarker = new ConcurrentHashMap<>();

    public static Optional<ITexReplacementCap> getMarkerCap(Entity entity) {
        LazyOptional<ITexReplacementCap> cap = cachedMarker.get(entity);
        if (cap == null) {
            cap = entity.getCapability(TexReplacementCapProvider.MARKER_CAPABILITY);
            cachedMarker.put(entity, cap);
            cap.addListener(optional -> cachedMarker.remove(entity));
        }
        return cap.resolve();
    }
}
