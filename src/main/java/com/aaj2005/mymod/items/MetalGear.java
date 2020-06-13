package com.aaj2005.mymod.items;

import com.aaj2005.mymod.MyMod;
import net.minecraft.item.Item;

public class MetalGear extends Item {

    public MetalGear() {
        super(new Properties()
                .maxStackSize(16)
                .group(MyMod.setup.itemGroup));
        setRegistryName("metalgear");
    }
}
