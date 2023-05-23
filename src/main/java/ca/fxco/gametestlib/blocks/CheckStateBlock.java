package ca.fxco.gametestlib.blocks;

import ca.fxco.gametestlib.Utils.Utils;
import ca.fxco.gametestlib.mixin.gametest.GameTestDebugRendererAccessor;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.debug.GameTestDebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Map;

public class CheckStateBlock extends BaseEntityBlock implements GameMasterBlock {

    private static final int GREEN = NativeImage.combine(100,100,200, 100);
    private static final int RED = NativeImage.combine(100,100,100, 200);
    private static final int GRAY = NativeImage.combine(100,0,0, 0);

    public CheckStateBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CheckStateBlockEntity(blockPos, blockState);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState neighborState,
                                  LevelAccessor levelAccessor, BlockPos blockPos, BlockPos neighborBlockPos) {
        updateDebugRenderer(levelAccessor, blockPos, neighborBlockPos, neighborState, direction, false, true);
        return blockState;
    }

    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        updateDebugRenderer(level, blockPos, null, null, null, true, true);
    }

    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        Direction dir = Utils.fromDirection(blockPos, blockPos2);
        updateDebugRenderer(level, blockPos, blockPos2, level.getBlockState(blockPos2), dir, false, true);
    }

    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!blockState2.is(blockState.getBlock())) {
            updateDebugRenderer(level, blockPos, null, null, null, true, true);
        }
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

    public void updateDebugRenderer(LevelAccessor levelAccessor, BlockPos blockPos, BlockPos neighborPos,
                                    BlockState neighborState, Direction direction, boolean update, boolean remove) {
        if (levelAccessor.isClientSide()) {
            BlockEntity blockEntity = levelAccessor.getBlockEntity(blockPos);
            if (blockEntity instanceof CheckStateBlockEntity checkStateBlockEntity &&
                    (update || direction == checkStateBlockEntity.getDirection())) {
                DebugRenderer debugRenderer = Minecraft.getInstance().debugRenderer;
                GameTestDebugRenderer gameTestDebugRenderer = debugRenderer.gameTestDebugRenderer;
                if (update) {
                    neighborPos = blockPos.relative(checkStateBlockEntity.getDirection());
                    neighborState = levelAccessor.getBlockState(neighborPos);
                }
                if (remove) {
                    Map<BlockPos, Object> markers = ((GameTestDebugRendererAccessor) gameTestDebugRenderer).getMarkers();
                    markers.remove(neighborPos);
                }
                if (checkStateBlockEntity.getBlockStateExp().matches(neighborState)) {
                    gameTestDebugRenderer.addMarker(
                            neighborPos,
                            checkStateBlockEntity.isFailOnFound() ? RED : GREEN,
                            checkStateBlockEntity.getBlockStateExp().asString(),
                            30000
                    );
                } else {
                    gameTestDebugRenderer.addMarker(
                            neighborPos,
                            GRAY, // gray -2130771968
                            checkStateBlockEntity.getBlockStateExp().asString(),
                            30000
                    );
                }
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.BLOCK;
    }
}
