package com.github.darkpred.entitytexturechanger;

import com.github.darkpred.entitytexturechanger.item.ModItems;
import com.github.darkpred.entitytexturechanger.network.C2SSubmitTextureMessage;
import com.github.darkpred.entitytexturechanger.network.MessageHandler;
import com.github.darkpred.entitytexturechanger.network.S2CSyncTextureMessage;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EntityTextureChanger.MOD_ID)
public class EntityTextureChanger {
    public static final String MOD_ID = "entitytexturechanger";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public EntityTextureChanger() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::buildContents);

        MinecraftForge.EVENT_BUS.register(this);
        ModItems.register(FMLJavaModLoadingContext.get().getModEventBus());
        MessageHandler.INSTANCE.registerMessage(0, S2CSyncTextureMessage.class, S2CSyncTextureMessage::write, S2CSyncTextureMessage::new, S2CSyncTextureMessage::handle);
        MessageHandler.INSTANCE.registerMessage(1, C2SSubmitTextureMessage.class, C2SSubmitTextureMessage::write, C2SSubmitTextureMessage::new, C2SSubmitTextureMessage::handle);
    }

    private void buildContents(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.MARKER);
        }
    }
}
