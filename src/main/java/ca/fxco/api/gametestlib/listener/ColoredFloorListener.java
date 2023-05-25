package ca.fxco.api.gametestlib.listener;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestInfo;
import net.minecraft.gametest.framework.GameTestListener;
import net.minecraft.gametest.framework.StructureUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

/**
 * Fun listener that changes the ground color based on the GameTests current state
 */
public class ColoredFloorListener implements GameTestListener {

    @Override
    public void testStructureLoaded(GameTestInfo gameTestInfo) {
        fillFloor(gameTestInfo, Blocks.LIGHT_GRAY_CONCRETE.defaultBlockState());
    }

    @Override
    public void testPassed(GameTestInfo gameTestInfo) {
        fillFloor(gameTestInfo, Blocks.LIME_CONCRETE.defaultBlockState());
    }

    @Override
    public void testFailed(GameTestInfo gameTestInfo) {
        fillFloor(gameTestInfo, Blocks.RED_CONCRETE.defaultBlockState());
    }

    public void fillFloor(GameTestInfo gameTestInfo, BlockState state) {
        BoundingBox boundingBox = StructureUtils.getStructureBoundingBox(gameTestInfo.getStructureBlockEntity());
        int bottomY = boundingBox.minY() - 1;
        for (int x = boundingBox.minX(); x < boundingBox.maxX(); x++) {
            for (int z = boundingBox.minZ(); z < boundingBox.maxZ(); z++) {
                gameTestInfo.getLevel().setBlock(new BlockPos(x, bottomY, z), state, Block.UPDATE_INVISIBLE);
            }
        }
    }
}
