package ca.fxco.api.gametestlib.gametest;

import ca.fxco.gametestlib.gametest.expansion.GameTestGroupConditions;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface GameTestActionBlock {
    @Nullable
    default GameTestGroupConditions.TestCondition addTestCondition(GameTestHelper helper, BlockState state,
                                                                   BlockPos blockPos, GameTestChanges changes) {
        return null;
    }
}
