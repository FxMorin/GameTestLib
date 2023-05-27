package ca.fxco.testmod;

import ca.fxco.gametestlib.GameTestLibMod;
import net.fabricmc.api.ModInitializer;

import java.nio.file.Path;

public class GameTestLibTestMod implements ModInitializer {

    public static final String MOD_ID = "gamtestlib-testmod";

    @Override
    public void onInitialize() {
        GameTestLibMod.setDevResources(Path.of("..", "src", "testmod", "resources", "data", MOD_ID, "gametest", "structures"));
    }
}
