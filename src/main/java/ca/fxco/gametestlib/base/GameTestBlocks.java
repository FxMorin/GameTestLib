package ca.fxco.gametestlib.base;

import ca.fxco.gametestlib.blocks.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import java.util.function.Function;

import static ca.fxco.gametestlib.GameTestLibMod.id;
import static net.minecraft.world.level.material.PushReaction.*;

public class GameTestBlocks {

    // GameTest Blocks
    public static final PulseStateBlock PULSE_STATE_BLOCK = register(
            "pulse_state_block",
            PulseStateBlock::new,
            Properties.ofFullCopy(Blocks.STONE).pushReaction(BLOCK)
    );
    public static final CheckStateBlock CHECK_STATE_BLOCK = register(
            "check_state_block",
            CheckStateBlock::new,
            Properties.ofFullCopy(Blocks.STONE).pushReaction(BLOCK)
    );
    public static final TestTriggerBlock TEST_TRIGGER_BLOCK = register(
            "test_trigger_block",
            TestTriggerBlock::new,
            Properties.ofFullCopy(Blocks.WHITE_CONCRETE).pushReaction(BLOCK)
    );
    public static final GameTestPoweredBlock GAMETEST_REDSTONE_BLOCK = register(
            "gametest_redstone_block",
            GameTestPoweredBlock::new,
            Properties.ofFullCopy(Blocks.REDSTONE_BLOCK).pushReaction(BLOCK)
    );
    public static final EntityInteractionBlock ENTITY_INTERACTION_BLOCK = register(
            "entity_interaction_block",
            EntityInteractionBlock::new,
            Properties.ofFullCopy(Blocks.STONE).pushReaction(BLOCK)
    );
    public static final EntityInsideBlock ENTITY_INSIDE_BLOCK = register(
            "entity_inside_block",
            EntityInsideBlock::new,
            Properties.ofFullCopy(Blocks.STONE).noCollission().pushReaction(BLOCK)
    );

    public static <T extends Block> T register(String name, Function<Properties, T> function,
                                               BlockBehaviour.Properties properties) {
        ResourceKey<Block> resourceKey = ResourceKey.create(Registries.BLOCK, id(name));
        T block = function.apply(properties.setId(resourceKey));
        return Registry.register(BuiltInRegistries.BLOCK, resourceKey, block);
    }

    public static void boostrap() {}
}
