package com.aaj2005.mymod.setup;

import com.aaj2005.mymod.blocks.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModSetup {

    public ItemGroup itemGroup = new ItemGroup("mymod") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.POWERGEN);
        }

    };



    public void init(){

    }
}
