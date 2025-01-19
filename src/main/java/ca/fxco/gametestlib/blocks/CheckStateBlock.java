package ca.fxco.gametestlib.blocks;

import ca.fxco.api.gametestlib.gametest.GameTestActionBlock;
import ca.fxco.api.gametestlib.gametest.GameTestChanges;
import ca.fxco.gametestlib.gametest.expansion.GameTestGroupConditions;
import com.mojang.serialization.MapCodec;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CheckStateBlock extends BaseEntityBlock implements GameMasterBlock, GameTestActionBlock {

    public static final MapCodec<CheckStateBlock> CODEC = simpleCodec(CheckStateBlock::new);

    public CheckStateBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CheckStateBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof CheckStateBlockEntity checkStateBlockEntity) {
            if (level.isClientSide) {
                return checkStateBlockEntity.usedBy(player) ?
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
        if (blockEntity instanceof CheckStateBlockEntity checkStateBe) {
            BlockPos checkPos = blockPos.relative(checkStateBe.getDirection());
            return new CheckStateTestCondition(checkStateBe, checkPos, changes.shouldFlip(GameTestChanges.FLIP_CHECKS));
        }
        return null;
    }

    public static class CheckStateTestCondition extends GameTestGroupConditions.BasicTestCondition {
        private final BlockPos checkPos;
        @Getter
        private final CheckStateBlockEntity checkStateBe;

        public CheckStateTestCondition(CheckStateBlockEntity checkStateBe, BlockPos checkPos, boolean flip) {
            this.checkStateBe = checkStateBe;
            this.checkPos = checkPos.immutable();
            if (flip) {
                this.checkStateBe.setFailOnFound(!this.checkStateBe.isFailOnFound());
            }
        }

        @Override
        public boolean isSingleTick() {
            return this.checkStateBe.getTick() > -1;
        }

        @Override
        public boolean canRunThisTick(long tick) {
            return this.checkStateBe.getTick() == tick;
        }

        @Override
        public Boolean runCheck(GameTestHelper helper) {
            return this.checkStateBe.runGameTestChecks(helper, this.checkPos);
        }
    }
}
