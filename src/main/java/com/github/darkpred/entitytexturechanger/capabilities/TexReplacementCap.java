package com.github.darkpred.entitytexturechanger.capabilities;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class TexReplacementCap implements ITexReplacementCap {
    private String textureUrl = "";

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (textureUrl != null) {
            tag.putString("textureUrl", textureUrl);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("textureUrl")) {
            setTextureUrl(tag.getString("textureUrl"));
        }
    }

    @Override
    public boolean hasTexture() {
        return !textureUrl.isBlank();
    }

    @Override
    public String getTextureUrl() {
        return textureUrl;
    }

    @Override
    public void setTextureUrl(String textureUrl) {
        this.textureUrl = textureUrl;
    }
}
