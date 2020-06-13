package com.aaj2005.mymod.datagen;

import com.aaj2005.mymod.blocks.ModBlocks;
import net.minecraft.data.DataGenerator;

public class LootTables extends BaseLootTableProvider {

    public LootTables(DataGenerator dataGeneratorIn){
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables(){
        lootTables.put(ModBlocks.POWERGEN, createStandardTable("powergen", ModBlocks.POWERGEN));
    }
}
