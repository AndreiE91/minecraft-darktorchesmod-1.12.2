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
        // Supercharged Coal
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

        // Ultrabright Torch
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

        // Ultrabright Glowstone
        ShapedOreRecipe ultrabrightGlowstoneRecipe = new ShapedOreRecipe(
                new ResourceLocation("darktorches", "ultrabright_glowstone"),
                new ItemStack(ModBlocks.ultrabrightGlowstone, 8),
                "GGG",
                "GCG",
                "GGG",
                'G', Blocks.GLOWSTONE,
                'C', ModItems.superchargedCoal
        );
        event.getRegistry().register(ultrabrightGlowstoneRecipe.setRegistryName("darktorches", "ultrabright_glowstone"));

        // Ultrabright Lamp
        ShapedOreRecipe ultrabrightLampRecipe = new ShapedOreRecipe(
                new ResourceLocation("darktorches", "ultrabright_lamp"),
                new ItemStack(ModBlocks.ultrabrightLampOff, 8),
                "LLL",
                "LCL",
                "LLL",
                'L', Blocks.REDSTONE_LAMP,
                'C', ModItems.superchargedCoal
        );
        event.getRegistry().register(ultrabrightLampRecipe.setRegistryName("darktorches", "ultrabright_lamp"));

        // Industrial Light
        ShapedOreRecipe industrialLightRecipe = new ShapedOreRecipe(
                new ResourceLocation("darktorches", "industrial_light"),
                new ItemStack(ModBlocks.industrialLight),
                "GSG",
                "SDS",
                "GSG",
                'G', Blocks.GLOWSTONE,
                'S', Blocks.SEA_LANTERN,
                'D', Blocks.BEDROCK
        );
        event.getRegistry().register(industrialLightRecipe.setRegistryName("darktorches", "industrial_light"));
    }
}