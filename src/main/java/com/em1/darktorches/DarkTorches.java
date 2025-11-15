package com.em1.darktorches;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        File configFile = new File(event.getModConfigurationDirectory(), "darktorches.cfg");
        config = new Configuration(configFile);
        syncConfig();
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

        if (config.hasChanged()) {
            config.save();
        }
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

                IBlockState state = block.getDefaultState();
                int original = block.getLightValue(state);
                if (original > 0) {
                    int newValue = Math.min(15, Math.round(original * globalLightMultiplier));
                    lightValueField.setInt(block, newValue);
                    logger.debug("Adjusted {} from {} to {}", block.getRegistryName(), original, newValue);
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
