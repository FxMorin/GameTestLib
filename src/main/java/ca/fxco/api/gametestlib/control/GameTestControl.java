package ca.fxco.api.gametestlib.control;

import ca.fxco.api.gametestlib.progressbar.GameTestProgressBar;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestInfo;
import net.minecraft.gametest.framework.GameTestListener;
import net.minecraft.gametest.framework.GameTestTicker;
import net.minecraft.gametest.framework.TestReporter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This class is used to set/control what GameTest uses for its internal systems,
 * such as: TestReporter, GameTestListener, and GameTestProgressBar
 */
public interface GameTestControl {

    /**
     * This is where dev resources should be set, in order for it to be loaded before anything is processed
     */
    default void onInitialize() {}

    default List<TestReporter> registerTestReporters() {
        return List.of();
    }

    default List<GameTestListenerFactory> registerGameTestListeners() {
        return List.of();
    }

    /**
     * Only a single Custom Progress Bar can be used at a time!
     * You can set a priority if two mods are conflicting
     */
    default @Nullable GameTestProgressBar registerGameTestProgressBar() {
        return null;
    }

    @FunctionalInterface
    interface GameTestListenerFactory {
        GameTestListener apply(GameTestInfo gameTestInfo, GameTestTicker gameTestTicker, BlockPos blockPos);
    }
}
