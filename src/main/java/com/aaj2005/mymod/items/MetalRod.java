package com.aaj2005.mymod.items;

import com.aaj2005.mymod.MyMod;
import net.minecraft.item.Item;

public class MetalRod extends Item {

    public MetalRod() {
        super(new Properties()
                .maxStackSize(64)
                .group(MyMod.setup.itemGroup));
        setRegistryName("metalrod");
    }
}
