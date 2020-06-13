package com.aaj2005.mymod.setup;

import com.aaj2005.mymod.MyMod;
import com.aaj2005.mymod.items.Moditems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MyMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistration {

    @SubscribeEvent
    public static void onItemColor(ColorHandlerEvent.Item event){
        event.getItemColors().register((stack, i) -> 0xff0000, Moditems.WEIRDMOBEGGITEM);
    }
}
