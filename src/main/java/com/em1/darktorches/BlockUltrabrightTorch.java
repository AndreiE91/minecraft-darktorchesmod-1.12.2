package com.em1.darktorches;

import net.minecraft.block.BlockTorch;
import net.minecraft.creativetab.CreativeTabs;

public class BlockUltrabrightTorch extends BlockTorch {
    
    public BlockUltrabrightTorch() {
        super();
        setUnlocalizedName("ultrabright_torch");
        setRegistryName("ultrabright_torch");
        setCreativeTab(CreativeTabs.DECORATIONS);
        setLightLevel(1.0F);
    }
}