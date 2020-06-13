package com.aaj2005.mymod.blocks.BlockFrame;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
public class BlockFrame extends Block {

    public BlockFrame() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(5.0f)
                .lightValue(0)
        );
        setRegistryName("blockframe");
    }

}
