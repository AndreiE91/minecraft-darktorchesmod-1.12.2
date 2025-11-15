package com.em1.darktorches;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.creativetab.CreativeTabs;

public class BlockUltrabrightTorch extends BlockTorch {
    
    public BlockUltrabrightTorch() {
        super();
        setUnlocalizedName("ultrabright_torch");
        setRegistryName("ultrabright_torch");
        setCreativeTab(CreativeTabs.DECORATIONS);
        setSoundType(SoundType.WOOD);
        setLightLevel(1.0F);
    }
}