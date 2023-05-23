package ca.fxco.gametestlib.client.renderer.blockentities;

import ca.fxco.gametestlib.blocks.CheckStateBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

@Environment(EnvType.CLIENT)
public class CheckStateBlockRenderer implements BlockEntityRenderer<CheckStateBlockEntity> {

    private static final float COLOR_R = 0.5F;
    private static final float COLOR_G = 0.5F;
    private static final float COLOR_B = 0.5F;
    private static final float COLOR_A = 0.4F;

    public CheckStateBlockRenderer(BlockEntityRendererProvider.Context context) {}

    public void render(CheckStateBlockEntity checkStateBlockEntity, float f,
                       PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int j) {
        if (Minecraft.getInstance().player.canUseGameMasterBlocks() || Minecraft.getInstance().player.isSpectator()) {
            // TODO: Use new full face block renderer in 1.20
        }
    }
}
