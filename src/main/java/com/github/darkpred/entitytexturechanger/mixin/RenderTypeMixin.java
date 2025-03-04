package com.github.darkpred.entitytexturechanger.mixin;

import com.github.darkpred.entitytexturechanger.util.Util;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderType.class)
public class RenderTypeMixin {

    @Inject(method = {
            "entitySolid",
            "entityCutout",
            "itemEntityTranslucentCull",
            "entityTranslucentCull",
            "entitySmoothCutout",
            "entityDecal",
            "entityNoOutline",
            "entityShadow",
            "eyes"
    }, at = @At(value = "HEAD"))
    private static void loadDifferentTexture(ResourceLocation pLocation, CallbackInfoReturnable<RenderType> cir, @Local(argsOnly = true) LocalRef<ResourceLocation> location) {
        Util.loadDifferentTexture(location);
    }

    @Inject(method = {
            "entityCutoutNoCull(Lnet/minecraft/resources/ResourceLocation;Z)Lnet/minecraft/client/renderer/RenderType;",
            "entityCutoutNoCullZOffset(Lnet/minecraft/resources/ResourceLocation;Z)Lnet/minecraft/client/renderer/RenderType;",
            "entityTranslucent(Lnet/minecraft/resources/ResourceLocation;Z)Lnet/minecraft/client/renderer/RenderType;",
    }, at = @At(value = "HEAD"))
    private static void loadDifferentTexture(ResourceLocation pLocation, boolean pOutline, CallbackInfoReturnable<RenderType> cir, @Local(argsOnly = true) LocalRef<ResourceLocation> location) {
        Util.loadDifferentTexture(location);
    }
}
