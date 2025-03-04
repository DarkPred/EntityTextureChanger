package com.github.darkpred.entitytexturechanger.item;

import com.github.darkpred.entitytexturechanger.EntityTextureChanger;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EntityTextureChanger.MOD_ID);

    public static final RegistryObject<Item> MARKER = ITEMS.register("texture_changer", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
