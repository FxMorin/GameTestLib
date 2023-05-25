package ca.fxco.gametestlib.blocks;

import ca.fxco.gametestlib.base.GameTestProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class EntityInsideBlock extends BaseEntityBlock implements GameMasterBlock {

    public static final IntegerProperty DELAY = GameTestProperties.DELAY_32;
    public static final EnumProperty<EntityInsideBlock.EntityType> TYPE = GameTestProperties.ENTITY_TYPE;

    public EntityInsideBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(DELAY, 0)
                .setValue(TYPE, EntityType.ALL)
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EntityInsideBlockEntity(blockPos, blockState);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.empty();
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player,
                                 InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (player.canUseGameMasterBlocks()) {
            if (player.isShiftKeyDown()) {
                level.setBlock(blockPos, blockState.cycle(TYPE), Block.UPDATE_INVISIBLE | Block.UPDATE_CLIENTS);
            } else {
                level.setBlock(blockPos, blockState.cycle(DELAY), Block.UPDATE_INVISIBLE | Block.UPDATE_CLIENTS);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DELAY, TYPE);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.BLOCK;
    }

    @AllArgsConstructor
    public enum EntityType implements StringRepresentable {
        ALL("all"),
        LIVING_ENTITY("living"),
        NONE_LIVING_ENTITY("none_living"),
        ITEM("item"),
        PLAYER("player");

        @Getter
        private final String serializedName;

        public String toString() {
            return this.serializedName;
        }
    }
}
