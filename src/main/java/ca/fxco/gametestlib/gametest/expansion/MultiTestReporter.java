package ca.fxco.gametestlib.gametest.expansion;

import ca.fxco.gametestlib.GameTestLibMod;
import net.minecraft.gametest.framework.GameTestInfo;
import net.minecraft.gametest.framework.TestReporter;

/**
 * The test reporter used to run multiple test reporters at the same time
 */
public class MultiTestReporter implements TestReporter {

    @Override
    public void onTestFailed(GameTestInfo gameTestInfo) {
        GameTestLibMod.CONTROLLER.getTestReporters().forEach(testReporter -> testReporter.onTestFailed(gameTestInfo));
    }

    @Override
    public void onTestSuccess(GameTestInfo gameTestInfo) {
        GameTestLibMod.CONTROLLER.getTestReporters().forEach(testReporter -> testReporter.onTestSuccess(gameTestInfo));
    }

    @Override
    public void finish() {
        GameTestLibMod.CONTROLLER.getTestReporters().forEach(TestReporter::finish);
    }
}
