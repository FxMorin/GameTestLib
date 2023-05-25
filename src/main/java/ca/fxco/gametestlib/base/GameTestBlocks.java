package ca.fxco.gametestlib.base;

import ca.fxco.gametestlib.blocks.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import static ca.fxco.gametestlib.GameTestLibMod.id;

public class GameTestBlocks {

    // GameTest Blocks
    public static final PulseStateBlock PULSE_STATE_BLOCK = register(
            "pulse_state_block",
            new PulseStateBlock(FabricBlockSettings.copyOf(Blocks.STONE))
    );
    public static final CheckStateBlock CHECK_STATE_BLOCK = register(
            "check_state_block",
            new CheckStateBlock(FabricBlockSettings.copyOf(Blocks.STONE))
    );
    public static final TestTriggerBlock TEST_TRIGGER_BLOCK = register(
            "test_trigger_block",
            new TestTriggerBlock(FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE))
    );
    public static final GameTestPoweredBlock GAMETEST_REDSTONE_BLOCK = register(
            "gametest_redstone_block",
            new GameTestPoweredBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_BLOCK))
    );
    public static final EntityInteractionBlock ENTITY_INTERACTION_BLOCK = register(
            "entity_interaction_block",
            new EntityInteractionBlock(FabricBlockSettings.copyOf(Blocks.STONE))
    );
    public static final EntityInsideBlock ENTITY_INSIDE_BLOCK = register(
            "entity_inside_block",
            new EntityInsideBlock(FabricBlockSettings.copyOf(Blocks.STONE).collidable(false))
    );

    private static <T extends Block> T register(String name, T block) {
        return Registry.register(BuiltInRegistries.BLOCK, id(name), block);
    }

    public static void boostrap() {}
}
