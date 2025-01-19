package ca.fxco.gametestlib.blocks;

import ca.fxco.api.gametestlib.gametest.GameTestActionBlock;
import ca.fxco.api.gametestlib.gametest.GameTestChanges;
import ca.fxco.gametestlib.gametest.GameTestUtil;
import ca.fxco.gametestlib.gametest.expansion.GameTestGroupConditions;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class PulseStateBlock extends BaseEntityBlock implements GameMasterBlock, GameTestActionBlock {

    public static final MapCodec<PulseStateBlock> CODEC = simpleCodec(PulseStateBlock::new);

    public PulseStateBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PulseStateBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player,
                                 InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof PulseStateBlockEntity pulseStateBlockEntity) {
            if (level.isClientSide) {
                return pulseStateBlockEntity.usedBy(player) ?
                        InteractionResult.sidedSuccess(true) : InteractionResult.PASS;
            }
            return InteractionResult.sidedSuccess(false);
        }
        return InteractionResult.PASS;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    //
    // GameTest Action Logic
    //

    @Nullable
    @Override
    public GameTestGroupConditions.TestCondition addTestCondition(GameTestHelper helper, BlockState state,
                                                                  BlockPos blockPos, GameTestChanges changes) {
        BlockEntity blockEntity = helper.getBlockEntity(blockPos);
        if (blockEntity instanceof PulseStateBlockEntity pulseStateBe) {
            if (pulseStateBe.isDisableFirstBlockUpdates()) {
                GameTestUtil.setBlock(helper, blockPos, pulseStateBe.getFirstBlockState(), Block.UPDATE_INVISIBLE);
            } else {
                helper.setBlock(blockPos, pulseStateBe.getFirstBlockState());
            }
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
        return null;
    }
}
