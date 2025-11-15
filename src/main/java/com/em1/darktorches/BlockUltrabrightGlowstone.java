package com.em1.darktorches;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockUltrabrightGlowstone extends Block {
    
    public BlockUltrabrightGlowstone() {
        super(Material.GLASS);
        setUnlocalizedName("ultrabright_glowstone");
        setRegistryName("ultrabright_glowstone");
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setSoundType(SoundType.GLASS);
        setHardness(0.3F);
        setLightLevel(1.0F);
    }
}