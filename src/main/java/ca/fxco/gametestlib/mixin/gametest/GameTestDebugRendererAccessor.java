package ca.fxco.gametestlib.mixin.gametest;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.debug.GameTestDebugRenderer;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(GameTestDebugRenderer.class)
public interface GameTestDebugRendererAccessor {

    @Accessor("markers")
    Map<BlockPos, Object> getMarkers();
}
