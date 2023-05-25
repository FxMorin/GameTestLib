package ca.fxco.gametestlib.gametest;

import ca.fxco.gametestlib.base.GameTestBlocks;
import ca.fxco.gametestlib.base.GameTestProperties;
import ca.fxco.gametestlib.blocks.CheckStateBlockEntity;
import ca.fxco.gametestlib.blocks.EntityInsideBlock;
import ca.fxco.gametestlib.blocks.EntityInteractionBlock;
import ca.fxco.gametestlib.blocks.PulseStateBlockEntity;
import ca.fxco.gametestlib.gametest.expansion.Config;
import ca.fxco.gametestlib.gametest.expansion.GameTestGroupConditions;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.entity.EntityTypeTest;

import java.util.function.BooleanSupplier;

public class GameTestUtil {

    public static final EntityTypeTest<Entity, ?> ANY_TYPE = new EntityTypeTest<>() {
        public Entity tryCast(Entity entity) {
            return entity;
        }
        public Class<? extends Entity> getBaseClass() {
            return Entity.class;
        }
    };

    public static final EntityTypeTest<Entity, ?> LIVING_ENTITY = EntityTypeTest.forClass(LivingEntity.class);

    /**
     * By using this method as the first line in a gametest, you will be able to use gametest blocks within those tests
     */
    public static void pistonLibGameTest(GameTestHelper helper) {
        pistonLibGameTest(helper, Config.GameTestChanges.NONE);
    }

    /**
     * By using this method as the first line in a gametest, you will be able to use gametest blocks within those tests
     */
    public static void pistonLibGameTest(GameTestHelper helper, Config.GameTestChanges changes) {
        if (helper.getTick() != 0) { // Only run searching logic on the first tick
            return;
        }
        GameTestGroupConditions groupConditions = new GameTestGroupConditions();
        helper.forEveryBlockInStructure(blockPos -> {
            BlockState state = helper.getBlockState(blockPos);
            Block block = state.getBlock();
            if (block == GameTestBlocks.PULSE_STATE_BLOCK) {
                BlockEntity blockEntity = helper.getBlockEntity(blockPos);
                if (blockEntity instanceof PulseStateBlockEntity pulseStateBe) {
                    helper.setBlock(blockPos, pulseStateBe.getFirstBlockState());
                    int delay = pulseStateBe.getDelay();
                    int duration = pulseStateBe.getDuration();
                    BlockPos blockPos2 = blockPos.immutable();
                    if (delay > 0) {
                        helper.runAfterDelay(delay, () ->
                                helper.setBlock(blockPos2, pulseStateBe.getPulseBlockState())
                        );
                    }
                    if (duration > 0) {
                        helper.runAfterDelay(delay + duration, () ->
                                helper.setBlock(blockPos2, pulseStateBe.getLastBlockState())
                        );
                    }
                }
            } else if (block == GameTestBlocks.CHECK_STATE_BLOCK) {
                BlockEntity blockEntity = helper.getBlockEntity(blockPos);
                if (blockEntity instanceof CheckStateBlockEntity checkStateBe) {
                    BlockPos checkPos = blockPos.relative(checkStateBe.getDirection());
                    groupConditions.addCondition(checkStateBe, checkPos, changes == Config.GameTestChanges.FLIP_CHECKS);
                }
            } else if (block == GameTestBlocks.TEST_TRIGGER_BLOCK) {
                groupConditions.addTestTrigger(blockPos, changes == Config.GameTestChanges.FLIP_TRIGGERS);
            } else if (block == GameTestBlocks.GAMETEST_REDSTONE_BLOCK) {
                helper.setBlock(blockPos, state.cycle(BlockStateProperties.POWERED));
            } else if (block == GameTestBlocks.ENTITY_INTERACTION_BLOCK) {
                groupConditions.addEntityInteraction(blockPos, state.getValue(GameTestProperties.INTERACTION_TYPE), changes == Config.GameTestChanges.FLIP_INTERACTIONS);
                setBlock(helper, blockPos, state.setValue(GameTestProperties.INTERACTION_TYPE, EntityInteractionBlock.InteractionType.NONE), Block.UPDATE_INVISIBLE);
            } else if (block == GameTestBlocks.ENTITY_INSIDE_BLOCK) {
                EntityInsideBlock.EntityType entityType = state.getValue(GameTestProperties.ENTITY_TYPE);
                groupConditions.addEntityInside(blockPos, entityType, changes == Config.GameTestChanges.FLIP_INSIDE);
                Minecraft.getInstance().debugRenderer.gameTestDebugRenderer.addMarker(blockPos, 0xffffff77, entityType.getSerializedName(), Integer.MAX_VALUE);
                setBlock(helper, blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_INVISIBLE);
            }
        });
        if (!groupConditions.getTestConditions().isEmpty()) {
            helper.onEachTick(() -> {
                groupConditions.runTick(helper);
                if (groupConditions.isSuccess()) {
                    helper.succeed();
                }
            });
        }
    }

    public static void succeedAfterDelay(GameTestHelper helper, long tick, BooleanSupplier supplier, String failReason) {
        helper.runAfterDelay(tick, () -> {
            if (supplier.getAsBoolean()) {
                helper.succeed();
            } else {
                helper.fail(failReason);
            }
        });
    }

    public static void setBlock(GameTestHelper helper, int i, int j, int k, Block block, int flags) {
        setBlock(helper, new BlockPos(i, j, k), block, flags);
    }

    public static void setBlock(GameTestHelper helper, int i, int j, int k, BlockState blockState, int flags) {
        setBlock(helper, new BlockPos(i, j, k), blockState, flags);
    }

    public static void setBlock(GameTestHelper helper, BlockPos blockPos, Block block, int flags) {
        setBlock(helper, blockPos, block.defaultBlockState(), flags);
    }

    public static void setBlock(GameTestHelper helper, BlockPos blockPos, BlockState blockState, int flags) {
        helper.getLevel().setBlock(helper.absolutePos(blockPos), blockState, flags);
    }
}
