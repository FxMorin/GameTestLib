package ca.fxco.gametestlib.mixin.gametest;

import ca.fxco.api.gametestlib.control.GameTestControl;
import ca.fxco.gametestlib.GameTestLibMod;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestInfo;
import net.minecraft.gametest.framework.GameTestRunner;
import net.minecraft.gametest.framework.GameTestTicker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(GameTestRunner.class)
public class GameTestRunnerMixin {

    @Shadow @Final private GameTestTicker testTicker;

    @Inject(
            method = "runBatch",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/gametest/framework/GameTestRunner;" +
                            "testTicker:Lnet/minecraft/gametest/framework/GameTestTicker;"
            )
    )
    private void gtl$useCustomListeners(int i, CallbackInfo ci, @Local Collection<GameTestInfo> collection) {
        for (GameTestControl.GameTestListenerFactory create : GameTestLibMod.CONTROLLER.getGameTestListeners()) {
            for (GameTestInfo gameTestInfo : collection) {
                gameTestInfo.addListener(create.apply(
                        gameTestInfo,
                        this.testTicker,
                        gameTestInfo.getStructureBlockPos()
                ));
            }
        }
    }
}
