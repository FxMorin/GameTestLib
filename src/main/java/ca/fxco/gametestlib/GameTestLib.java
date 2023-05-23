package ca.fxco.gametestlib;

import ca.fxco.api.gametestlib.config.binder.ConfigBinder;
import ca.fxco.gametestlib.base.GameTestBlockEntities;
import ca.fxco.gametestlib.base.GameTestBlocks;
import ca.fxco.gametestlib.base.GameTestItems;
import ca.fxco.gametestlib.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.impl.entrypoint.EntrypointUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

public class GameTestLib implements ModInitializer {

    public static final String MOD_ID = "gametestlib";

    public static final boolean GAMETEST_ACTIVE = System.getProperty("fabric-api.gametest") != null;
    public static final boolean KEEPALIVE_ACTIVE = Boolean.parseBoolean(System.getProperty("gametestlib.keepAlive", "false"));

    public static final ConfigManager CONFIG_MANAGER = new ConfigManager();
    public static MinecraftServer CURRENT_SERVER = null;

    @Override
    public void onInitialize() {
        GameTestBlocks.boostrap();
        GameTestItems.boostrap();
        GameTestBlockEntities.boostrap();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            CURRENT_SERVER = server;
            EntrypointUtils.invoke(MOD_ID + "-binders", ConfigBinder.class, configBinder -> {
                CONFIG_MANAGER.loadBinder(server, configBinder);
            });
        });
    }

    public static ResourceLocation id(String location) {
        return new ResourceLocation(MOD_ID, location);
    }

    public static void switchServer(MinecraftServer server) {
        CURRENT_SERVER = server;
    }
}
