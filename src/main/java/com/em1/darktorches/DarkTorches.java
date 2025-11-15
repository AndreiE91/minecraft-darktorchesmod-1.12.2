package com.em1.darktorches;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.lang.reflect.Field;

@Mod(modid = "darktorches", name = "Dark Torches", version = "1.2")
public class DarkTorches {

    private static Logger logger;
    private static Configuration config;

    // Configurable values
    private static float globalLightMultiplier = 0.5F;
    private static float ultrabrightTorchLight = 15.0F;
    private static int industrialLightMaxEnergy = 10000;
    private static int industrialLightEnergyPerTick = 50;
    private static int industrialLightMaxReceiveEnergyPerTick = 100;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        File configFile = new File(event.getModConfigurationDirectory(), "darktorches.cfg");
        config = new Configuration(configFile);
        syncConfig();

        GameRegistry.registerTileEntity(TileEntityIndustrialLight.class, new ResourceLocation("darktorches", "industrial_light"));
    }

    private void syncConfig() {
        globalLightMultiplier = config.getFloat(
                "globalLightMultiplier",
                Configuration.CATEGORY_GENERAL,
                globalLightMultiplier,
                0f, 5f,
                "Multiplier for all light-emitting blocks"
        );

        ultrabrightTorchLight = config.getFloat(
                "ultrabrightTorchLight",
                Configuration.CATEGORY_GENERAL,
                ultrabrightTorchLight,
                0f, 15f,
                "Base light level for the ultrabright torch (not scaled by multiplier)"
        );

        industrialLightMaxEnergy = config.getInt(
                "industrialLightMaxEnergy",
                Configuration.CATEGORY_GENERAL,
                industrialLightMaxEnergy,
                1000, 10000000,
                "Maximum energy storage for Industrial Light (FE)"
        );
        
        industrialLightEnergyPerTick = config.getInt(
                "industrialLightEnergyPerTick",
                Configuration.CATEGORY_GENERAL,
                industrialLightEnergyPerTick,
                1, 10000,
                "Energy consumed per tick by Industrial Light (FE/t)"
        );

        industrialLightMaxReceiveEnergyPerTick = config.getInt(
                "industrialLightMaxReceiveEnergyPerTick",
                Configuration.CATEGORY_GENERAL,
                industrialLightMaxReceiveEnergyPerTick,
                1, 100000,
                "Max energy received per tick by Industrial Light (FE/t)"
        );

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static int getIndustrialLightMaxEnergy() {
        return industrialLightMaxEnergy;
    }

    public static int getIndustrialLightEnergyPerTick() {
        return industrialLightEnergyPerTick;
    }

    public static int getIndustrialLightMaxReceiveEnergyPerTick() {
        return industrialLightMaxReceiveEnergyPerTick;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        try {
            Field lightValueField;
            try {
                lightValueField = Block.class.getDeclaredField("lightValue");
            } catch (NoSuchFieldException e) {
                lightValueField = Block.class.getDeclaredField("field_149784_t"); // SRG fallback
            }
            lightValueField.setAccessible(true);

            for (Block block : Block.REGISTRY) {
                // Skip all ultrabright blocks
                if (ModBlocks.ultrabrightTorch != null && block == ModBlocks.ultrabrightTorch)
                    continue;
                if (ModBlocks.ultrabrightGlowstone != null && block == ModBlocks.ultrabrightGlowstone)
                    continue;
                if (ModBlocks.ultrabrightLampOn != null && block == ModBlocks.ultrabrightLampOn)
                    continue;
                if (ModBlocks.industrialLight != null && block == ModBlocks.industrialLight)
                    continue;

                IBlockState state = block.getDefaultState();
                int original = block.getLightValue(state);
                if (original > 0) {
                    // Check if this block gets reduced penalty
                    boolean reducedPenalty = block == Blocks.GLOWSTONE || 
                                            block == Blocks.END_ROD || 
                                            block == Blocks.REDSTONE_LAMP || 
                                            block == Blocks.FIRE || 
                                            block == Blocks.LAVA || 
                                            block == Blocks.FLOWING_LAVA;
                    
                    float multiplier = reducedPenalty ? Math.min(globalLightMultiplier * 1.5F, 1.0F) : globalLightMultiplier;
                    int newValue = Math.min(15, Math.round(original * multiplier));
                    lightValueField.setInt(block, newValue);
                    logger.debug("Adjusted {} from {} to {} (multiplier: {})", 
                                block.getRegistryName(), original, newValue, multiplier);
                }
            }

            // Set ultrabright torch light
            if (ModBlocks.ultrabrightTorch != null) {
                int finalTorchValue = Math.min(15, Math.round(ultrabrightTorchLight));
                lightValueField.setInt(ModBlocks.ultrabrightTorch, finalTorchValue);
                logger.info("Set ultrabright torch light to {}", finalTorchValue);
            }

            // Set ultrabright glowstone light
            if (ModBlocks.ultrabrightGlowstone != null) {
                lightValueField.setInt(ModBlocks.ultrabrightGlowstone, 15);
                logger.info("Set ultrabright glowstone light to 15");
            }

            // Set ultrabright lamp light
            if (ModBlocks.ultrabrightLampOn != null) {
                lightValueField.setInt(ModBlocks.ultrabrightLampOn, 15);
                logger.info("Set ultrabright lamp light to 15");
            }

        } catch (Exception e) {
            logger.error("Failed to apply light multipliers or ultrabright torch settings", e);
        }
    }

    public static ItemStack torchItem() {
        return new ItemStack(Item.getItemFromBlock(Blocks.TORCH));
    }
}
