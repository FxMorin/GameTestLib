package ca.fxco.gametestlib.gametest;

import ca.fxco.gametestlib.gametest.block.GameTestActionBlock;
import ca.fxco.gametestlib.gametest.expansion.Config;
import ca.fxco.gametestlib.gametest.expansion.GameTestGroupConditions;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BooleanSupplier;

public class GameTestUtil {

    /**
     * By using this method as the first line in a gametest, you will be able to use gametest blocks within those tests
     * Even if that test does not have the `GameTestConfig` annotation.
     * However, you should not use this method if you are using the `GameTestConfig` annotation, as that annotation
     * automatically adds this logic! (If you really need to, you can disable the automatic addition of the method by
     * setting `customBlocks()` to false in the annotation)
     */
    public static void pistonLibGameTest(GameTestHelper helper) {
        pistonLibGameTest(helper, Config.GameTestChanges.NONE);
    }

    /**
     * By using this method as the first line in a gametest, you will be able to use gametest blocks within those tests
     * Even if that test does not have the `GameTestConfig` annotation.
     * However, you should not use this method if you are using the `GameTestConfig` annotation, as that annotation
     * automatically adds this logic! (If you really need to, you can disable the automatic addition of the method by
     * setting `customBlocks()` to false in the annotation)
     */
    public static void pistonLibGameTest(GameTestHelper helper, Config.GameTestChanges changes) {
        if (helper.getTick() != 0) { // Only run searching logic on the first tick
            return;
        }
        GameTestGroupConditions groupConditions = new GameTestGroupConditions();
        helper.forEveryBlockInStructure(blockPos -> {
            BlockState state = helper.getBlockState(blockPos);
            if (state.getBlock() instanceof GameTestActionBlock actionBlock) {
                GameTestGroupConditions.TestCondition testCondition = actionBlock.addTestCondition(
                        helper,
                        state,
                        blockPos,
                        changes
                );
                if (testCondition != null) {
                    groupConditions.addCondition(testCondition);
                }
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
