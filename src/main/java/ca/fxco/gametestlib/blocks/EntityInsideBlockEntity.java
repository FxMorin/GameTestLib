package ca.fxco.gametestlib.blocks;

import ca.fxco.gametestlib.base.GameTestBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EntityInsideBlockEntity extends BlockEntity {
    public EntityInsideBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(GameTestBlockEntities.ENTITY_INSIDE_BLOCK_ENTITY, blockPos, blockState);
    }
}
