package ca.fxco.gametestlib.mixin.gametest;

import ca.fxco.api.gametestlib.control.GameTestControl;
import ca.fxco.gametestlib.GameTestLibMod;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestInfo;
import net.minecraft.gametest.framework.GameTestRunner;
import net.minecraft.gametest.framework.GameTestTicker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameTestRunner.class)
public class GameTestRunnerMixin {

    @Inject(
            method = "runTest",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/gametest/framework/GameTestTicker;" +
                            "add(Lnet/minecraft/gametest/framework/GameTestInfo;)V"
            )
    )
    private static void useCustomListeners(GameTestInfo gameTestInfo, BlockPos blockPos,
                                           GameTestTicker gameTestTicker, CallbackInfo ci) {
        for (GameTestControl.GameTestListenerFactory create : GameTestLibMod.CONTROLLER.getGameTestListeners()) {
            gameTestInfo.addListener(create.apply(gameTestInfo, gameTestTicker, blockPos));
        }
    }
}
