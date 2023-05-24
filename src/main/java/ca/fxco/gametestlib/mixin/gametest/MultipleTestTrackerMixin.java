package ca.fxco.gametestlib.mixin.gametest;

import ca.fxco.gametestlib.GameTestLibMod;
import net.minecraft.gametest.framework.GameTestInfo;
import net.minecraft.gametest.framework.MultipleTestTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;

@Mixin(MultipleTestTracker.class)
public class MultipleTestTrackerMixin {

    @Shadow @Final private Collection<GameTestInfo> tests;

    /**
     * @author FX
     * @reason API for custom progress bars through GameTestLib
     */
    @Overwrite
    public String getProgressBar() {
        return GameTestLibMod.CONTROLLER.getProgressBar().getProgressBar(this.tests);
    }
}
