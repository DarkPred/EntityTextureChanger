package com.github.darkpred.entitytexturechanger.util;

import com.github.darkpred.entitytexturechanger.EntityTextureChanger;
import com.github.darkpred.entitytexturechanger.capabilities.ITexReplacementCap;
import com.github.darkpred.entitytexturechanger.capabilities.ModCapabilities;
import com.github.darkpred.entitytexturechanger.client.CustomHttpTexture;
import com.google.common.hash.Hashing;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Nullable;

public class Util {
    @Nullable
    private static Entity activeEntity;

    public static void setActiveEntity(@Nullable Entity entity) {
        activeEntity = entity;
    }

    /**
     * Replaces the reference in the parameter with a new texture location if the entity set with {@link #setActiveEntity}
     * has a texture set in {@link ITexReplacementCap}
     */
    public static void loadDifferentTexture(LocalRef<ResourceLocation> location) {
        if (activeEntity != null) {
            ModCapabilities.getMarkerCap(activeEntity).filter(ITexReplacementCap::hasTexture)
                    .ifPresent(cap -> location.set(loadTexture(location.get(), cap.getTextureUrl())));
        }
    }

    private static ResourceLocation loadTexture(ResourceLocation fallback, String url) {
        String s = Hashing.goodFastHash(160).hashUnencodedChars(FilenameUtils.getBaseName(url)).toString();
        ResourceLocation location = EntityTextureChanger.location(s);
        TextureManager textureManager = Minecraft.getInstance().getTextureManager();
        if (textureManager.getTexture(location, MissingTextureAtlasSprite.getTexture()) == MissingTextureAtlasSprite.getTexture()) {
            textureManager.register(location, new CustomHttpTexture(fallback, url));
        }
        return location;
    }
}
