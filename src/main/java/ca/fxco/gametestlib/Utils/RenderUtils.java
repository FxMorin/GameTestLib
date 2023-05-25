package ca.fxco.gametestlib.Utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Transformation;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;

import java.util.function.Function;

@UtilityClass
public class RenderUtils {

    public static void renderCubeWithCull(Matrix4f matrix4f, VertexConsumer vertexConsumer,
                                          Function<Direction, Boolean> canCull) {
        renderCubeWithCull(matrix4f, vertexConsumer, canCull, 0x7F7F7F7F);
    }
    public static void renderCubeWithCull(Matrix4f matrix4f, VertexConsumer vertexConsumer,
                                          Function<Direction, Boolean> canCull, int color) {
        if (canCull.apply(Direction.SOUTH)) {
            renderFace(matrix4f, vertexConsumer, color, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        if (canCull.apply(Direction.NORTH)) {
            renderFace(matrix4f, vertexConsumer, color, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        }
        if (canCull.apply(Direction.EAST)) {
            renderFace(matrix4f, vertexConsumer, color, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
        }
        if (canCull.apply(Direction.WEST)) {
            renderFace(matrix4f, vertexConsumer, color, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F);
        }
        if (canCull.apply(Direction.DOWN)) {
            renderFace(matrix4f, vertexConsumer, color, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F);
        }
        if (canCull.apply(Direction.UP)) {
            renderFace(matrix4f, vertexConsumer, color, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F);
        }
    }

    public static void renderCube(Matrix4f matrix4f, VertexConsumer vertexConsumer, int color) {
        renderFace(matrix4f, vertexConsumer, color, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F);
        renderFace(matrix4f, vertexConsumer, color, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        renderFace(matrix4f, vertexConsumer, color, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
        renderFace(matrix4f, vertexConsumer, color, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F);
        renderFace(matrix4f, vertexConsumer, color, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F);
        renderFace(matrix4f, vertexConsumer, color, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F);
    }

    public static void renderFace(Matrix4f matrix4f, VertexConsumer vertexConsumer, int color,
                                  float f, float g, float h, float i, float j, float k, float l, float m) {
        vertexConsumer.vertex(matrix4f, f, h, j).color(color).endVertex();
        vertexConsumer.vertex(matrix4f, g, h, k).color(color).endVertex();
        vertexConsumer.vertex(matrix4f, g, i, l).color(color).endVertex();
        vertexConsumer.vertex(matrix4f, f, i, m).color(color).endVertex();
    }

    // TODO: This is definitely just temporary until we switch to the new debug renderers in 1.20
    public static void renderFloatingText(PoseStack pose, String text, double x, double y, double z, int color,
                                          float scale, boolean centered, float offsetX, boolean throughWalls) {
        PoseStack poseStack2 = RenderSystem.getModelViewStack();
        poseStack2.pushPose();
        poseStack2.mulPoseMatrix(pose.last().pose());
        RenderSystem.applyModelViewMatrix();
        DebugRenderer.renderFloatingText(text, x, y, z, color, scale, centered, offsetX, throughWalls);
        poseStack2.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    // TODO: This is definitely just temporary until we switch to the new debug renderers in 1.20
    public static void renderFloatingText(PoseStack pose, String text, double x, double y, double z,
                                          int color, float scale, boolean centered, float offsetX,
                                          boolean throughWalls, double maxDist) {
        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = minecraft.gameRenderer.getMainCamera();
        if (camera.isInitialized() && minecraft.getEntityRenderDispatcher().options != null &&
                camera.getPosition().distanceToSqr(x, y, z) < maxDist * maxDist) {
            PoseStack poseStack2 = RenderSystem.getModelViewStack();
            poseStack2.pushPose();
            poseStack2.mulPoseMatrix(pose.last().pose());
            RenderSystem.applyModelViewMatrix();
            renderDebugFloatingText(camera, minecraft.font, text, x, y, z, color, scale, centered, offsetX, throughWalls);
            poseStack2.popPose();
            RenderSystem.applyModelViewMatrix();
        }
    }

    private static void renderDebugFloatingText(Camera camera, Font font, String text,
                                                double x, double y, double z, int color, float scale,
                                                boolean centered, float offsetX, boolean throughWalls) {
        double x2 = camera.getPosition().x;
        double y2 = camera.getPosition().y;
        double z2 = camera.getPosition().z;
        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        poseStack.translate((float)(x - x2), (float)(y - y2) + 0.07F, (float)(z - z2));
        poseStack.mulPoseMatrix(new Matrix4f().rotation(camera.rotation()));
        poseStack.scale(scale, -scale, scale);
        RenderSystem.enableTexture();
        if (throughWalls) {
            RenderSystem.disableDepthTest();
        } else {
            RenderSystem.enableDepthTest();
        }
        RenderSystem.depthMask(true);
        poseStack.scale(-1.0F, 1.0F, 1.0F);
        RenderSystem.applyModelViewMatrix();
        float m = centered ? (float)(-font.width(text)) / 2.0F : 0.0F;
        m -= offsetX / scale;
        MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        font.drawInBatch(text, m, 0.0F, color, false, Transformation.identity().getMatrix(), bufferSource, throughWalls, 0, 15728880);
        bufferSource.endBatch();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableDepthTest();
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
    }
}
