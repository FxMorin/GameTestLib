package ca.fxco.gametestlib;

import ca.fxco.gametestlib.base.GameTestBlockEntities;
import ca.fxco.gametestlib.base.GameTestBlocks;
import ca.fxco.gametestlib.base.GameTestItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

public class GameTestLib implements ModInitializer {

    public static final String MOD_ID = "gametestlib";

    public static final boolean GAMETEST_ACTIVE = System.getProperty("fabric-api.gametest") != null;
    public static final boolean KEEPALIVE_ACTIVE = Boolean.parseBoolean(System.getProperty("gametestlib.keepAlive", "false"));

    @Override
    public void onInitialize() {
        GameTestBlocks.boostrap();
        GameTestItems.boostrap();
        GameTestBlockEntities.boostrap();
    }

    public static ResourceLocation id(String location) {
        return new ResourceLocation(MOD_ID, location);
    }
}
