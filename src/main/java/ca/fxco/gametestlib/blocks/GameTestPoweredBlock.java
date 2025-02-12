package ca.fxco.gametestlib.blocks;

import ca.fxco.api.gametestlib.gametest.GameTestActionBlock;
import ca.fxco.api.gametestlib.gametest.GameTestChanges;
import ca.fxco.gametestlib.gametest.expansion.GameTestGroupConditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.PoweredBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

// Instead of using a complicated PulseStateBlock to set a redstone block when the test starts. Just use this block!
// This also allows you to make a redstone block that will turn off when a test starts by right-clicking the block
public class GameTestPoweredBlock extends PoweredBlock implements GameMasterBlock, GameTestActionBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public GameTestPoweredBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
    }

    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return blockState.getValue(POWERED) ? 15 : 0;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos,
                                               Player player, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (player.canUseGameMasterBlocks()) {
            blockState = blockState.cycle(POWERED);
            level.setBlock(blockPos, blockState, Block.UPDATE_ALL);
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    //
    // GameTest Action Logic
    //

    @Nullable
    @Override
    public GameTestGroupConditions.TestCondition addTestCondition(GameTestHelper helper, BlockState state,
                                                                  BlockPos blockPos, GameTestChanges changes) {
        helper.setBlock(blockPos, state.cycle(BlockStateProperties.POWERED));
        return null;
    }
}
