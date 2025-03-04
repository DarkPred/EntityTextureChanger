package com.github.darkpred.entitytexturechanger.client;

import com.github.darkpred.entitytexturechanger.EntityTextureChanger;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class CustomHttpTexture extends SimpleTexture {
    private final String textureUrl;
    @Nullable
    private CompletableFuture<?> future;
    private boolean uploaded;

    public CustomHttpTexture(ResourceLocation location, String textureUrl) {
        super(location);
        this.textureUrl = textureUrl;
    }

    @Override
    public void load(@NotNull ResourceManager pResourceManager) {
        Minecraft.getInstance().execute(() -> {
            if (!uploaded) {
                try {
                    super.load(pResourceManager);
                } catch (IOException ioexception) {
                    EntityTextureChanger.LOGGER.warn("Failed to load texture: {}", location, ioexception);
                }
                uploaded = true;
            }
        });
        if (future == null) {
            future = CompletableFuture.runAsync(this::download, Util.backgroundExecutor());
        }
    }

    private void download() {
        HttpURLConnection connection = null;
        EntityTextureChanger.LOGGER.debug("Downloading http texture from {}", textureUrl);
        try {
            connection = (HttpURLConnection) (new URL(textureUrl)).openConnection();
            connection.setDoInput(true);
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:135.0) Gecko/20100101 Firefox/135.0");
            connection.connect();
            EntityTextureChanger.LOGGER.info("ResponseCode: {} ContentLength: {}", connection.getResponseCode(), connection.getContentLength());
            if (connection.getResponseCode() / 100 == 2) {
                InputStream inputstream = connection.getInputStream();
                NativeImage image = readImage(inputstream);
                if (image != null) {
                    uploaded = true;
                    if (!RenderSystem.isOnRenderThread()) {
                        RenderSystem.recordRenderCall(() -> upload(image));
                    } else {
                        upload(image);
                    }
                }
            }
        } catch (IOException e) {
            EntityTextureChanger.LOGGER.error("Couldn't download http texture", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private NativeImage readImage(InputStream inputStream) {
        NativeImage nativeimage = null;
        try {
            nativeimage = NativeImage.read(inputStream);
        } catch (Exception exception) {
            EntityTextureChanger.LOGGER.warn("Error while loading the skin texture", exception);
        }
        return nativeimage;
    }

    private void upload(NativeImage image) {
        EntityTextureChanger.LOGGER.info("uploading: {}, width: {}, height: {}", getId(), image.getWidth(), image.getHeight());
        TextureUtil.prepareImage(getId(), image.getWidth(), image.getHeight());
        image.upload(0, 0, 0, true);
    }
}
