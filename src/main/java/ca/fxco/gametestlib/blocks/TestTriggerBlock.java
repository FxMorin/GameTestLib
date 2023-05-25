package ca.fxco.gametestlib.blocks;

import ca.fxco.gametestlib.base.GameTestBlocks;
import ca.fxco.gametestlib.gametest.block.GameTestActionBlock;
import ca.fxco.gametestlib.gametest.expansion.Config;
import ca.fxco.gametestlib.gametest.expansion.GameTestGroupConditions;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class TestTriggerBlock extends Block implements GameMasterBlock, GameTestActionBlock {

    public static final BooleanProperty INVERTED = BlockStateProperties.INVERTED;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public TestTriggerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false).setValue(INVERTED, false));
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos,
                                Block block, BlockPos blockPos2, boolean bl) {
        if (!blockState.getValue(POWERED) && level.hasNeighborSignal(blockPos)) {
            level.setBlock(blockPos, blockState.setValue(POWERED, true), Block.UPDATE_INVISIBLE);
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player,
                                 InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (player.canUseGameMasterBlocks()) {
            blockState = blockState.cycle(INVERTED).setValue(POWERED, false);
            level.setBlock(blockPos, blockState, Block.UPDATE_INVISIBLE);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(INVERTED, POWERED);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.BLOCK;
    }

    //
    // GameTest Action Logic
    //

    @Nullable
    public GameTestGroupConditions.TestCondition addTestCondition(GameTestHelper helper, BlockState state,
                                                                  BlockPos blockPos, Config.GameTestChanges changes) {
        return new TriggerTestCondition(blockPos, changes == Config.GameTestChanges.FLIP_TRIGGERS);
    }

    public static class TriggerTestCondition extends GameTestGroupConditions.BasicTestCondition {
        private final BlockPos blockPos;
        private final boolean flip;

        public TriggerTestCondition(BlockPos blockPos, boolean flip) {
            this.blockPos = blockPos.immutable();
            this.flip = flip;
        }

        @Override
        public Boolean runCheck(GameTestHelper helper) {
            BlockState state = helper.getBlockState(this.blockPos);
            if (state.getBlock() == GameTestBlocks.TEST_TRIGGER_BLOCK) {
                if (state.getValue(BlockStateProperties.POWERED)) {
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
}
