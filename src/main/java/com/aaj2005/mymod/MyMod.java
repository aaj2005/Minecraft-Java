package com.aaj2005.mymod;

import com.aaj2005.mymod.blocks.BlockFrame.BlockFrame;
import com.aaj2005.mymod.blocks.PowerGen.PowerGeneratorContainer;
import com.aaj2005.mymod.blocks.PowerGen.PowerGenerator;
import com.aaj2005.mymod.blocks.PowerGen.PowerGeneratorTile;
import com.aaj2005.mymod.blocks.ModBlocks;
import com.aaj2005.mymod.entities.WeirdMobEntity;
import com.aaj2005.mymod.items.KeyItem;
import com.aaj2005.mymod.items.MetalGear;
import com.aaj2005.mymod.items.MetalRod;
import com.aaj2005.mymod.items.WeirdMobEggItem;
import com.aaj2005.mymod.setup.ClientProxy;
import com.aaj2005.mymod.setup.IProxy;
import com.aaj2005.mymod.setup.ModSetup;
import com.aaj2005.mymod.setup.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("mymod")
public class MyMod {

    public static final String MODID = "mymod";

    public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public static ModSetup setup = new ModSetup();

    private static final Logger LOGGER = LogManager.getLogger();

    public MyMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("mymod-client.toml"));
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("mymod-common.toml"));
    }

    private void setup(final FMLCommonSetupEvent event) {
        setup.init();
        proxy.init();
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            event.getRegistry().register(new PowerGenerator());
            event.getRegistry().register(new BlockFrame());
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            Item.Properties properties = new Item.Properties()
                    .group(setup.itemGroup);
            event.getRegistry().register(new BlockItem(ModBlocks.POWERGEN, properties).setRegistryName("powergen"));
            event.getRegistry().register(new KeyItem());
            event.getRegistry().register(new BlockItem(ModBlocks.BLOCKFRAME, properties).setRegistryName("blockframe"));
            event.getRegistry().register(new MetalRod());
            event.getRegistry().register(new MetalGear());
            event.getRegistry().register(new WeirdMobEggItem());
        }

        @SubscribeEvent
        public static  void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event){
            event.getRegistry().register( TileEntityType.Builder.create(PowerGeneratorTile::new, ModBlocks.POWERGEN).build(null).setRegistryName("powergen"));
        }

        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event){
            event.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                return new PowerGeneratorContainer(windowId,MyMod.proxy.getClientWorld(), pos, inv, MyMod.proxy.getClientPlayer());
            }).setRegistryName("powergen"));
        }

        @SubscribeEvent
        public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> event){
            event.getRegistry().register(EntityType.Builder.create(WeirdMobEntity::new, EntityClassification.CREATURE)
                .size(1, 1)
                .setShouldReceiveVelocityUpdates(false)
                .build("weirdmob").setRegistryName(MyMod.MODID, "weirdmob"));
        }
    }
}
