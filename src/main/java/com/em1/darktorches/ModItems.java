package com.em1.darktorches;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ModItems {

    public static Item superchargedCoal;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        superchargedCoal = new Item()
                .setUnlocalizedName("supercharged_coal")
                .setRegistryName("supercharged_coal")
                .setCreativeTab(CreativeTabs.MATERIALS);

        event.getRegistry().registerAll(superchargedCoal);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(superchargedCoal, 0,
            new ModelResourceLocation("darktorches:supercharged_coal", "inventory"));
    }
}