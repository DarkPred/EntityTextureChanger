package com.github.darkpred.entitytexturechanger.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface ITexReplacementCap extends INBTSerializable<CompoundTag> {
    /**
     * Returns whether a texture url is currently set
     */
    boolean hasTexture();

    String getTextureUrl();

    void setTextureUrl(String textureUrl);
}
