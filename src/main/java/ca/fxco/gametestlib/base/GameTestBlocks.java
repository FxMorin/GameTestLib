package ca.fxco.gametestlib.base;

import ca.fxco.gametestlib.blocks.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import static ca.fxco.gametestlib.GameTestLibMod.id;
import static net.minecraft.world.level.material.PushReaction.*;

public class GameTestBlocks {

    // GameTest Blocks
    public static final PulseStateBlock PULSE_STATE_BLOCK = register(
            "pulse_state_block",
            new PulseStateBlock(Properties.ofFullCopy(Blocks.STONE).pushReaction(BLOCK))
    );
    public static final CheckStateBlock CHECK_STATE_BLOCK = register(
            "check_state_block",
            new CheckStateBlock(Properties.ofFullCopy(Blocks.STONE).pushReaction(BLOCK))
    );
    public static final TestTriggerBlock TEST_TRIGGER_BLOCK = register(
            "test_trigger_block",
            new TestTriggerBlock(Properties.ofFullCopy(Blocks.WHITE_CONCRETE).pushReaction(BLOCK))
    );
    public static final GameTestPoweredBlock GAMETEST_REDSTONE_BLOCK = register(
            "gametest_redstone_block",
            new GameTestPoweredBlock(Properties.ofFullCopy(Blocks.REDSTONE_BLOCK).pushReaction(BLOCK))
    );
    public static final EntityInteractionBlock ENTITY_INTERACTION_BLOCK = register(
            "entity_interaction_block",
            new EntityInteractionBlock(Properties.ofFullCopy(Blocks.STONE).pushReaction(BLOCK))
    );
    public static final EntityInsideBlock ENTITY_INSIDE_BLOCK = register(
            "entity_inside_block",
            new EntityInsideBlock(Properties.ofFullCopy(Blocks.STONE).noCollission().pushReaction(BLOCK))
    );

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(BuiltInRegistries.BLOCK, id(name), block);
    }

    public static void boostrap() {}
}
