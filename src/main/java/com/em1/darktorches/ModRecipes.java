package com.em1.darktorches;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber
public class ModRecipes {

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        ShapedOreRecipe superchargedCoalRecipe = new ShapedOreRecipe(
                new ResourceLocation("darktorches", "supercharged_coal"),
                new ItemStack(ModItems.superchargedCoal, 8),
                "CCC",
                "CSC",
                "CCC",
                'C', Items.COAL,
                'S', Items.NETHER_STAR
        );
        event.getRegistry().register(superchargedCoalRecipe.setRegistryName("darktorches", "supercharged_coal"));

        ShapedOreRecipe ultrabrightTorchRecipe = new ShapedOreRecipe(
                new ResourceLocation("darktorches", "ultrabright_torch"),
                new ItemStack(ModBlocks.ultrabrightTorch, 8),
                "TTT",
                "TCT",
                "TTT",
                'T', Blocks.TORCH,
                'C', ModItems.superchargedCoal
        );
        event.getRegistry().register(ultrabrightTorchRecipe.setRegistryName("darktorches", "ultrabright_torch"));
    }
}