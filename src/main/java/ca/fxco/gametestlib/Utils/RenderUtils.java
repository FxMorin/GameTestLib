package ca.fxco.gametestlib.Utils;

import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.experimental.UtilityClass;
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
        vertexConsumer.addVertex(matrix4f, f, h, j).setColor(color);
        vertexConsumer.addVertex(matrix4f, g, h, k).setColor(color);
        vertexConsumer.addVertex(matrix4f, g, i, l).setColor(color);
        vertexConsumer.addVertex(matrix4f, f, i, m).setColor(color);
    }
}
