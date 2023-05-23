package ca.fxco.gametestlib;

import ca.fxco.gametestlib.network.GameTestNetwork;
import net.fabricmc.api.ClientModInitializer;

public class GameTestClientLib implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // TODO: Use block entity based debug renderer in 1.20
        //BlockEntityRenderers.register(GameTestBlockEntities.CHECK_STATE_BLOCK_ENTITY, CheckStateBlockRenderer::new);
        GameTestNetwork.initializeClient();
    }
}
