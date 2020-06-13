package com.aaj2005.mymod.blocks.PowerGen;

import com.aaj2005.mymod.Config;
import com.aaj2005.mymod.tools.CustomEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

import static com.aaj2005.mymod.blocks.ModBlocks.POWERGENERATOR_TILE;

public class PowerGeneratorTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);

    private int counter;

    public PowerGeneratorTile() {
        super(POWERGENERATOR_TILE);
    }

    @Override
    public void tick() {
        if (world.isRemote){
            return;
        }

        if (counter > 0) {
            counter--;
            if (counter <= 0) {
                energy.ifPresent(e -> ((CustomEnergyStorage) e).addEnergy(Config.POWERGENERATOR_GENERATE.get()));
            }
            markDirty();
        }

        if (counter <= 0)
            handler.ifPresent(h -> {
                ItemStack stack = h.getStackInSlot(0);
                if (stack.getItem() == Items.COAL) {
                    h.extractItem(0, 1, false);
                    counter = Config.POWERGENERATOR_TICKS.get();
                    markDirty();
                }
            });

        BlockState blockState = world.getBlockState(pos);
        if (blockState.get(BlockStateProperties.POWERED) !=counter > 0){
            world.setBlockState(pos, blockState.with(BlockStateProperties.POWERED, counter > 0), 3);
        }

        sendOutPower();
    }

    private void sendOutPower() {
        energy.ifPresent(energy -> {
            AtomicInteger capacity = new AtomicInteger(energy.getEnergyStored());
            if (capacity.get() > 0) {
                for (Direction direction : Direction.values()) {
                    TileEntity te = world.getTileEntity(pos.offset(direction));
                    if (te != null) {
                        boolean doContinue = te.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
                                    if (handler.canReceive()) {
                                        int received = handler.receiveEnergy(Math.min(capacity.get(), 100), false);
                                        capacity.addAndGet(-received);
                                        ((CustomEnergyStorage) energy).consumeEnergy(received);
                                        markDirty();
                                        return capacity.get() > 0;
                                    } else {
                                        return true;
                                    }
                                }
                        ).orElse(true);
                        if (!doContinue) {
                            return;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void read(CompoundNBT tag) {
        CompoundNBT invTag = tag.getCompound("inv");
        handler.ifPresent(h-> ((INBTSerializable<CompoundNBT>)h).deserializeNBT(invTag));
        CompoundNBT energyTag = tag.getCompound("energy");
        energy.ifPresent(h-> ((INBTSerializable<CompoundNBT>)h).deserializeNBT(energyTag));
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag){
        handler.ifPresent(h->{
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>)h).serializeNBT();
            tag.put("inv", compound);
        });
        energy.ifPresent(h->{
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>)h).serializeNBT();
            tag.put("energy", compound);
        });

        tag.putInt("counter", counter);
        return  super.write(tag);
    }

    private IItemHandler createHandler(){
        return new ItemStackHandler(1){

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
                public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                    return stack.getItem() == Items.COAL;
                }

                @Nonnull
                @Override
                public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                    if (stack.getItem() != Items.COAL){
                        return stack;
                    }
                    return super.insertItem(slot, stack, simulate);
                }
            };
        }

    private IEnergyStorage createEnergy(){
        return new CustomEnergyStorage(Config.POWERGENERATOR_MAXPOWER.get(), 0);


}

    @Nonnull
    @Override
    public <T>LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side){
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return handler.cast();
        }
        if (cap == CapabilityEnergy.ENERGY){
            return energy.cast();
        }
        return  super.getCapability(cap, side);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new PowerGeneratorContainer(i, world, pos, playerInventory, playerEntity);
    }
}
