package ca.fxco.gametestlib.base;

import ca.fxco.gametestlib.blocks.CheckStateBlockEntity;
import ca.fxco.gametestlib.blocks.PulseStateBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import static ca.fxco.gametestlib.GameTestLib.id;

public class GameTestBlockEntities {

    // GameTest
    public static final BlockEntityType<PulseStateBlockEntity> PULSE_STATE_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            id("pulse_state_block"),
            FabricBlockEntityTypeBuilder.create(PulseStateBlockEntity::new, GameTestBlocks.PULSE_STATE_BLOCK).build(null)
    );
    public static final BlockEntityType<CheckStateBlockEntity> CHECK_STATE_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            id("check_state_block"),
            FabricBlockEntityTypeBuilder.create(CheckStateBlockEntity::new, GameTestBlocks.CHECK_STATE_BLOCK).build(null)
    );

    public static void boostrap() { }
}
