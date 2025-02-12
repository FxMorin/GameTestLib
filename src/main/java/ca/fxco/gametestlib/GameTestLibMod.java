package ca.fxco.gametestlib;

import ca.fxco.api.gametestlib.config.binder.ConfigBinder;
import ca.fxco.api.gametestlib.control.GameTestControl;
import ca.fxco.gametestlib.base.GameTestBlockEntities;
import ca.fxco.gametestlib.base.GameTestBlocks;
import ca.fxco.gametestlib.base.GameTestCreativeModeTabs;
import ca.fxco.gametestlib.base.GameTestItems;
import ca.fxco.gametestlib.config.ConfigManager;
import ca.fxco.gametestlib.gametest.GameTestController;
import ca.fxco.gametestlib.gametest.expansion.MultiTestReporter;
import ca.fxco.gametestlib.mixin.gametest.GameTestServerAccessor;
import ca.fxco.gametestlib.network.GameTestNetwork;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.gametest.framework.GameTestServer;
import net.minecraft.gametest.framework.GlobalTestReporter;
import net.minecraft.gametest.framework.MultipleTestTracker;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;

public class GameTestLibMod implements ModInitializer {

    public static final String MOD_ID = "gametestlib";

    public static Path DEV_RESOURCES;

    public static final boolean GAMETEST_ACTIVE = System.getProperty("fabric-api.gametest") != null;
    public static final boolean KEEPALIVE_ACTIVE = Boolean.parseBoolean(System.getProperty("gametestlib.keepAlive", "false"));

    public static final ConfigManager CONFIG_MANAGER = new ConfigManager();
    public static final GameTestController CONTROLLER = new GameTestController();
    public static MinecraftServer CURRENT_SERVER = null;

    private static boolean initialized = false;

    @Override
    public void onInitialize() {
        initialize();
    }

    public static void initialize() {
        if (!initialized) {
            initialized = true;

            ServerLifecycleEvents.SERVER_STARTED.register(server -> {
                CURRENT_SERVER = server;
                CONFIG_MANAGER.resetAllValues();
            });

            GameTestBlocks.boostrap();
            GameTestItems.boostrap();
            GameTestBlockEntities.boostrap();
            GameTestCreativeModeTabs.bootstrap();
            GameTestNetwork.initializeServer();

            GlobalTestReporter.replaceWith(new MultiTestReporter());
            FabricLoader.getInstance().invokeEntrypoints(MOD_ID + "-control", GameTestControl.class, CONTROLLER::loadControl);
            CONTROLLER.initializeDefaults();

            FabricLoader.getInstance().invokeEntrypoints(MOD_ID + "-binders", ConfigBinder.class, CONFIG_MANAGER::loadBinder);
        }
    }

    public static ResourceLocation id(String location) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, location);
    }

    public static void switchServer(MinecraftServer server) {
        CURRENT_SERVER = server;
    }

    public static boolean areTestsRunning() {
        if (GAMETEST_ACTIVE && CURRENT_SERVER instanceof GameTestServer gameTestServer) {
            MultipleTestTracker tracker = ((GameTestServerAccessor)gameTestServer).getTestTracker();
            return tracker != null && !tracker.isDone();
        }
        return false;
    }

    public static void setDevResources(Path path) {
        DEV_RESOURCES = path;
    }

    public static void setDevResources(String modId) {
        DEV_RESOURCES = Path.of("..","..", "src", "main", "resources", "data", modId, "gametest", "structures");
    }
}
