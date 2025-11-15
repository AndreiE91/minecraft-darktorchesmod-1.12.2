package com.em1.darktorches;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ModBlocks {

    public static Block ultrabrightTorch;
    public static Block ultrabrightGlowstone;
    public static Block ultrabrightLampOff;
    public static Block ultrabrightLampOn;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ultrabrightTorch = new BlockUltrabrightTorch();
        ultrabrightGlowstone = new BlockUltrabrightGlowstone();
        ultrabrightLampOff = new BlockUltrabrightLamp(false);
        ultrabrightLampOn = new BlockUltrabrightLamp(true);
        
        event.getRegistry().registerAll(
            ultrabrightTorch,
            ultrabrightGlowstone,
            ultrabrightLampOff,
            ultrabrightLampOn
        );
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(ultrabrightTorch).setRegistryName(ultrabrightTorch.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ultrabrightGlowstone).setRegistryName(ultrabrightGlowstone.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ultrabrightLampOff).setRegistryName(ultrabrightLampOff.getRegistryName()));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ultrabrightTorch), 0,
            new ModelResourceLocation("darktorches:ultrabright_torch", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ultrabrightGlowstone), 0,
            new ModelResourceLocation("darktorches:ultrabright_glowstone", "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ultrabrightLampOff), 0,
            new ModelResourceLocation("darktorches:ultrabright_lamp", "inventory"));
    }
}