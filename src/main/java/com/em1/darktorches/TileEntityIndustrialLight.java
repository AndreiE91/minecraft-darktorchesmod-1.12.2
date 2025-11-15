package com.em1.darktorches;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

import com.em1.darktorches.DarkTorches;

public class TileEntityIndustrialLight extends TileEntity implements ITickable, IEnergyStorage {

    private int energyStored = 0;
    private int lastLightLevel = 0;
    private int cooldownTicks = 0; // Cooldown timer
    private int checkTicks = 0; // Periodic energy check counter
    private static final int COOLDOWN_DURATION = 40; // 2 seconds (20 ticks/second)
    private static final int ENERGY_MARGIN_TICKS = 20; // Require enough energy for 1 second of operation
    private static final int CHECK_INTERVAL = 10; // Check every 10 ticks (0.5 seconds)


    private void applyLightLevel(int newLightLevel) {
        if (lastLightLevel == newLightLevel) return;
        lastLightLevel = newLightLevel;

        if (!world.isRemote) {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof BlockIndustrialLight) {
                boolean active = newLightLevel > 0;
                if (state.getValue(BlockIndustrialLight.ACTIVE) != active) {
                    world.setBlockState(pos, state.withProperty(BlockIndustrialLight.ACTIVE, active), 3);
                } else {
                    world.checkLightFor(net.minecraft.world.EnumSkyBlock.BLOCK, pos);
                    world.notifyBlockUpdate(pos, state, state, 3);
                }
            } else {
                // Fallback (should not happen if block correct)
                world.checkLightFor(net.minecraft.world.EnumSkyBlock.BLOCK, pos);
            }
        }
        markDirty();
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            checkTicks++;
            
            // Handle cooldown timer
            if (cooldownTicks > 0) {
                cooldownTicks--;
                applyLightLevel(0);
                return;
            }

            int consumeAmount = DarkTorches.getIndustrialLightEnergyPerTick();
            int requiredEnergy = consumeAmount * ENERGY_MARGIN_TICKS; // Need enough for margin period
            
            // Periodic check: verify we still have enough energy to stay on
            if (checkTicks >= CHECK_INTERVAL) {
                checkTicks = 0;
                
                // If light is on but we don't have enough energy, force it off
                if (lastLightLevel > 0 && energyStored < requiredEnergy) {
                    energyStored = 0;
                    cooldownTicks = COOLDOWN_DURATION;
                    applyLightLevel(0);
                    return;
                }
            }
            
            if (energyStored >= consumeAmount) {
                energyStored -= consumeAmount;
                // Only stay on if we have enough energy buffer
                if (energyStored >= requiredEnergy) {
                    applyLightLevel(15);
                } else {
                    // Not enough energy buffer, turn off and enter cooldown
                    energyStored = 0;
                    cooldownTicks = COOLDOWN_DURATION;
                    applyLightLevel(0);
                }
            } else {
                energyStored = 0;
                cooldownTicks = COOLDOWN_DURATION; // Start cooldown
                applyLightLevel(0);
            }
        }
    }

    public int getLightLevel() {
        return lastLightLevel;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("Energy", energyStored);
        compound.setInteger("LightLevel", lastLightLevel);
        compound.setInteger("Cooldown", cooldownTicks);
        compound.setInteger("CheckTicks", checkTicks);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        energyStored = compound.getInteger("Energy");
        lastLightLevel = compound.getInteger("LightLevel");
        cooldownTicks = compound.getInteger("Cooldown");
        checkTicks = compound.getInteger("CheckTicks");
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    // IEnergyStorage implementation
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        // Block energy reception during cooldown
        if (cooldownTicks > 0) {
            return 0;
        }

        int limit = DarkTorches.getIndustrialLightMaxReceiveEnergyPerTick();
        int energyReceived = Math.min(getMaxEnergyStored() - energyStored, Math.min(maxReceive, limit));
        if (!simulate && energyReceived > 0) {
            energyStored += energyReceived;
            
            // Only turn on if we have enough energy margin
            int consumeAmount = DarkTorches.getIndustrialLightEnergyPerTick();
            int requiredEnergy = consumeAmount * ENERGY_MARGIN_TICKS;
            applyLightLevel(energyStored >= requiredEnergy ? 15 : 0);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0; // Cannot extract energy
    }

    @Override
    public int getEnergyStored() {
        return energyStored;
    }

    @Override
    public int getMaxEnergyStored() {
        return DarkTorches.getIndustrialLightMaxEnergy();
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return cooldownTicks == 0; // Only receive when not in cooldown
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(this);
        }
        return super.getCapability(capability, facing);
    }
}