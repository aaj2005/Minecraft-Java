package com.aaj2005.mymod.blocks;

import com.aaj2005.mymod.blocks.BlockFrame.BlockFrame;
import com.aaj2005.mymod.blocks.PowerGen.PowerGenerator;
import com.aaj2005.mymod.blocks.PowerGen.PowerGeneratorContainer;
import com.aaj2005.mymod.blocks.PowerGen.PowerGeneratorTile;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {

    @ObjectHolder("mymod:powergen")
    public static PowerGenerator POWERGEN;

    @ObjectHolder("mymod:powergen")
    public static TileEntityType<PowerGeneratorTile> POWERGENERATOR_TILE;

    @ObjectHolder("mymod:powergen")
    public static ContainerType<PowerGeneratorContainer> POWERGENERATOR_CONTAINER;

    @ObjectHolder("mymod:blockframe")
    public static BlockFrame BLOCKFRAME;

}
