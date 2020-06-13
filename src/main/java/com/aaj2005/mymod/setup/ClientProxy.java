package com.aaj2005.mymod.setup;

import com.aaj2005.mymod.blocks.ModBlocks;
import com.aaj2005.mymod.blocks.PowerGen.PowerGeneratorScreen;
import com.aaj2005.mymod.entities.WeirdMobEntity;
import com.aaj2005.mymod.entities.WeirdMobRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IProxy{

    @Override
    public void init() {
        ScreenManager.registerFactory(ModBlocks.POWERGENERATOR_CONTAINER, PowerGeneratorScreen::new);
        RenderingRegistry.registerEntityRenderingHandler(WeirdMobEntity.class, WeirdMobRenderer::new);
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
