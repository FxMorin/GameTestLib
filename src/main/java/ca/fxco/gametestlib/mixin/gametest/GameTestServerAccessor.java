package ca.fxco.gametestlib.mixin.gametest;

import net.minecraft.gametest.framework.GameTestBatch;
import net.minecraft.gametest.framework.GameTestServer;
import net.minecraft.gametest.framework.MultipleTestTracker;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(GameTestServer.class)
public interface GameTestServerAccessor {

    @Accessor
    List<GameTestBatch> getTestBatches();

    @Accessor
    @Nullable MultipleTestTracker getTestTracker();
}
