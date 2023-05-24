package ca.fxco.api.gametestlib.control;

import ca.fxco.api.gametestlib.progressbar.GameTestProgressBar;
import net.minecraft.gametest.framework.GameTestListener;
import net.minecraft.gametest.framework.TestReporter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This class is used to set/control what GameTest uses for its internal systems,
 * such as: TestReporter, GameTestListener, and GameTestProgressBar (MultipleTestTracker maybe?)
 */
public interface GameTestControl {

    default List<TestReporter> registerTestReporters() {
        return List.of();
    }

    default List<GameTestListener> registerGameTestListeners() {
        return List.of();
    }

    /**
     * Only a single Custom Progress Bar can be used at a time!
     * You can set a priority if two mods are conflicting
     */
    default @Nullable GameTestProgressBar registerGameTestProgressBar() {
        return null;
    }
}
