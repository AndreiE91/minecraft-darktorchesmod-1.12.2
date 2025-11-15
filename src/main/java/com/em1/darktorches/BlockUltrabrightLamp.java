package com.em1.darktorches;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockUltrabrightLamp extends Block {
    
    private final boolean isOn;
    
    public BlockUltrabrightLamp(boolean isOn) {
        super(Material.REDSTONE_LIGHT);
        this.isOn = isOn;
        setUnlocalizedName(isOn ? "ultrabright_lamp_on" : "ultrabright_lamp");
        setRegistryName(isOn ? "ultrabright_lamp_on" : "ultrabright_lamp");
        setSoundType(SoundType.GLASS);
        setHardness(0.3F);
        
        if (isOn) {
            setLightLevel(1.0F);
        } else {
            setCreativeTab(CreativeTabs.REDSTONE);
        }
    }
    
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            if (this.isOn && !worldIn.isBlockPowered(pos)) {
                worldIn.setBlockState(pos, ModBlocks.ultrabrightLampOff.getDefaultState(), 2);
            } else if (!this.isOn && worldIn.isBlockPowered(pos)) {
                worldIn.setBlockState(pos, ModBlocks.ultrabrightLampOn.getDefaultState(), 2);
            }
        }
    }
    
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {
            if (this.isOn && !worldIn.isBlockPowered(pos)) {
                worldIn.scheduleUpdate(pos, this, 4);
            } else if (!this.isOn && worldIn.isBlockPowered(pos)) {
                worldIn.setBlockState(pos, ModBlocks.ultrabrightLampOn.getDefaultState(), 2);
            }
        }
    }
    
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, java.util.Random rand) {
        if (!worldIn.isRemote && this.isOn && !worldIn.isBlockPowered(pos)) {
            worldIn.setBlockState(pos, ModBlocks.ultrabrightLampOff.getDefaultState(), 2);
        }
    }
}