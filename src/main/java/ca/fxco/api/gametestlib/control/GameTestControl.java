package ca.fxco.api.gametestlib.control;

import net.minecraft.gametest.framework.GameTestListener;
import net.minecraft.gametest.framework.TestReporter;

import java.util.List;

/**
 * This class is used to set/control what GameTest uses for its internal systems,
 * such as: TestReporter, GameTestListener, and GameTestProgressBar (MultipleTestTracker maybe?)
 */
public interface GameTestControl {

    List<TestReporter> registerTestReporters();

    List<GameTestListener> registerGameTestListeners();

    List<GameTestListener> registerGameTestProgressBar();
}
