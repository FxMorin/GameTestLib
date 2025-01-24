package ca.fxco.gametestlib.client.renderer.blockentities;

import ca.fxco.gametestlib.base.GameTestProperties;
import ca.fxco.gametestlib.blocks.EntityInsideBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class EntityInsideBlockRenderer<T extends EntityInsideBlockEntity> implements BlockEntityRenderer<T> {

    public EntityInsideBlockRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(T blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        Level level = blockEntity.getLevel();
        if (level != null) {
            BlockPos blockPos = blockEntity.getBlockPos();
            DebugRenderer.renderFilledUnitCube(poseStack, multiBufferSource, blockPos, 0x7F, 0x7F, 0x7F, 0x7F);
            BlockState state = blockEntity.getBlockState();
            String entityType = state.getValue(GameTestProperties.ENTITY_TYPE).getSerializedName() +
                    " [" + state.getValue(GameTestProperties.DELAY_32) + "]";
            double x = (double)blockPos.getX() + 1;
            double y = (double)blockPos.getY() + 2.4;
            double z = (double)blockPos.getZ() + 1;
            DebugRenderer.renderFloatingText(poseStack, multiBufferSource,
                    entityType, x, y, z, -1, 0.025F, true, 0.0F, true);
        }
    }
}
