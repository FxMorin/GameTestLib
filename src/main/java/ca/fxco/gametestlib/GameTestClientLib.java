package ca.fxco.gametestlib;

import ca.fxco.gametestlib.base.GameTestBlockEntities;
import ca.fxco.gametestlib.client.renderer.blockentities.CheckStateBlockRenderer;
import ca.fxco.gametestlib.client.renderer.blockentities.EntityInsideBlockRenderer;
import ca.fxco.gametestlib.network.GameTestNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class GameTestClientLib implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        GameTestNetwork.initializeClient();

        BlockEntityRenderers.register(GameTestBlockEntities.CHECK_STATE_BLOCK_ENTITY, CheckStateBlockRenderer::new);
        BlockEntityRenderers.register(GameTestBlockEntities.ENTITY_INSIDE_BLOCK_ENTITY, EntityInsideBlockRenderer::new);
    }
}
