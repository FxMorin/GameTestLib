package ca.fxco.gametestlib.blocks;

import ca.fxco.gametestlib.base.GameTestBlocks;
import ca.fxco.gametestlib.base.GameTestProperties;
import ca.fxco.gametestlib.gametest.GameTestUtil;
import ca.fxco.gametestlib.gametest.block.GameTestActionBlock;
import ca.fxco.gametestlib.gametest.expansion.Config;
import ca.fxco.gametestlib.gametest.expansion.GameTestGroupConditions;
import lombok.AllArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * Used to test the entities stepOn(), fallOn(), entityInside(), and onProjectileHit() calls
 * Right-click to change the height of the block, shift right-click to change the inverted property,
 * and shift right-click the top to change the trigger type.
 * Setting it to null will make it not trigger, which makes it a useful block due to its shapes
 */
public class EntityInteractionBlock extends Block implements GameMasterBlock, GameTestActionBlock {

    protected static final VoxelShape[] SHAPE_BY_LEVEL;

    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;
    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
    public static final EnumProperty<InteractionType> TRIGGERED = GameTestProperties.INTERACTION_TYPE;

    public EntityInteractionBlock(Properties properties) {
        super(properties);
        // Set the trigger value to true when starting to prevent this block from triggering a success
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LEVEL, 15)
                .setValue(INVERTED, false)
                .setValue(TRIGGERED, InteractionType.NONE)
        );
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE_BY_LEVEL[blockState.getValue(LEVEL)];
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player,
                                 InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (player.canUseGameMasterBlocks()) {
            if (player.isShiftKeyDown()) {
                if (blockHitResult.getDirection() == Direction.UP) {
                    level.setBlock(blockPos, blockState.cycle(TRIGGERED), Block.UPDATE_INVISIBLE);
                } else {
                    level.setBlock(blockPos, blockState.cycle(INVERTED), Block.UPDATE_INVISIBLE);
                }
            } else {
                int currentLevel = blockState.getValue(LEVEL);
                level.setBlock(blockPos, blockState.setValue(LEVEL, currentLevel == 0 ? 15 : currentLevel - 1), Block.UPDATE_INVISIBLE);
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void stepOn(Level level, BlockPos blockPos, BlockState blockState, Entity entity) {
        setTrigger(level, blockState, blockPos, InteractionType.STEP);
        super.stepOn(level, blockPos, blockState, entity);
    }

    @Override
    public void fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float f) {
        setTrigger(level, blockState, blockPos, InteractionType.FALL);
        super.fallOn(level, blockState, blockPos, entity, f);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (Shapes.joinIsNotEmpty(Shapes.create(entity.getBoundingBox().deflate(0.001).move(-blockPos.getX(), -blockPos.getY(), -blockPos.getZ())), blockState.getShape(level, blockPos), BooleanOp.AND)) {
            setTrigger(level, blockState, blockPos, InteractionType.INSIDE);
        }
        super.entityInside(blockState, level, blockPos, entity);
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult blockHitResult, Projectile projectile) {
        setTrigger(level, state, blockHitResult.getBlockPos(), InteractionType.PROJECTILE);
        super.onProjectileHit(level, state, blockHitResult, projectile);
    }

    private void setTrigger(LevelAccessor level, BlockState blockState, BlockPos blockPos, InteractionType type) {
        if (blockState.getValue(TRIGGERED) == InteractionType.NONE) {
            level.setBlock(blockPos, blockState.setValue(TRIGGERED, type), Block.UPDATE_INVISIBLE | Block.UPDATE_CLIENTS);
            if (!level.isClientSide() && !level.getBlockTicks().hasScheduledTick(blockPos, this)) {
                level.scheduleTick(blockPos, this, 2);
            }
        }
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if (blockState.getValue(TRIGGERED) != InteractionType.NONE) {
            serverLevel.setBlock(blockPos, blockState.setValue(TRIGGERED, InteractionType.NONE), Block.UPDATE_INVISIBLE);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL, INVERTED, TRIGGERED);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.BLOCK;
    }

    //
    // GameTest Action Logic
    //

    @Nullable
    @Override
    public GameTestGroupConditions.TestCondition addTestCondition(GameTestHelper helper, BlockState state,
                                                                  BlockPos blockPos, Config.GameTestChanges changes) {
        EntityInteractionCondition condition = new EntityInteractionCondition(
                blockPos,
                state.getValue(GameTestProperties.INTERACTION_TYPE),
                changes == Config.GameTestChanges.FLIP_INTERACTIONS
        );
        GameTestUtil.setBlock(
                helper,
                blockPos,
                state.setValue(GameTestProperties.INTERACTION_TYPE, EntityInteractionBlock.InteractionType.NONE),
                Block.UPDATE_INVISIBLE
        );
        return condition;
    }

    static {
        VoxelShape[] levels = new VoxelShape[16];
        for (int i = 16; i >= 1; i--) {
            levels[i - 1] = Block.box(0.0, 0.0, 0.0, 16.0, i, 16.0);
        }
        SHAPE_BY_LEVEL = levels;
    }

    public static class EntityInteractionCondition extends GameTestGroupConditions.BasicTestCondition {
        private final BlockPos blockPos;
        private final boolean flip;

        private final EntityInteractionBlock.InteractionType interactionType;

        public EntityInteractionCondition(BlockPos blockPos, EntityInteractionBlock.InteractionType interactionType,
                                          boolean flip) {
            this.blockPos = blockPos.immutable();
            this.interactionType = interactionType;
            this.flip = flip;
        }

        @Override
        public Boolean runCheck(GameTestHelper helper) {
            BlockState state = helper.getBlockState(this.blockPos);
            if (state.getBlock() == GameTestBlocks.ENTITY_INTERACTION_BLOCK) {
                if (state.getValue(GameTestProperties.INTERACTION_TYPE) == this.interactionType) {
                    if (state.getValue(BlockStateProperties.INVERTED) == this.flip) {
                        helper.fail("Test Trigger at " + this.blockPos.toShortString() + " was triggered");
                        return false;
                    }
                    return true;
                }
                return null;
            }
            return false;
        }
    }

    @AllArgsConstructor
    public enum InteractionType implements StringRepresentable {
        NONE("none"),
        STEP("step"),
        FALL("fall"),
        INSIDE("inside"),
        PROJECTILE("projectile");

        private final String name;

        public String toString() {
            return this.name;
        }

        public String getSerializedName() {
            return this.name;
        }
    }
}
