package ca.fxco.gametestlib.client.renderer.blockentities;

import ca.fxco.gametestlib.Utils.RenderUtils;
import ca.fxco.gametestlib.blocks.CheckStateBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class CheckStateBlockRenderer<T extends CheckStateBlockEntity> implements BlockEntityRenderer<T> {

    private static final int GREEN = 0x9900C800; // a, r, g, b
    private static final int RED = 0x99C80000;
    private static final int GRAY = 0xC8202020;

    public CheckStateBlockRenderer(BlockEntityRendererProvider.Context context) {}

    public void render(T blockEntity, float f, PoseStack poseStack,
                       MultiBufferSource multiBufferSource, int light, int j) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player.canUseGameMasterBlocks() || minecraft.player.isSpectator()) {
            // TODO: Use new full face debug block renderer in 1.20
            poseStack.pushPose();
            Direction direction = blockEntity.getDirection();
            poseStack.translate(direction.getStepX(),direction.getStepY(),direction.getStepZ());
            poseStack.translate(-0.005F,-0.005F,-0.005F);
            poseStack.scale(1.01F, 1.01F, 1.01F);
            Matrix4f matrix4f = poseStack.last().pose();
            BlockPos sidePos = blockEntity.getBlockPos().relative(direction);
            BlockState state = blockEntity.getLevel().getBlockState(blockEntity.getBlockPos().relative(direction));
            int color = blockEntity.getBlockStateExp().matches(state) ?
                    (blockEntity.isFailOnFound() ? RED : GREEN) :
                    GRAY;
            RenderUtils.renderCube(matrix4f, multiBufferSource.getBuffer(RenderType.lightning()), color);
            poseStack.popPose();
            int tick = blockEntity.getTick();
            if (tick != -1) {
                double x = (double) sidePos.getX() + 1;
                double y = (double) sidePos.getY() + 2.4;
                double z = (double) sidePos.getZ() + 1;
                DebugRenderer.renderFloatingText(poseStack, multiBufferSource, "[" + tick + "]", x, y, z, -1,
                        0.025F, true, 0.0F, true);
            }
        }
    }
}
