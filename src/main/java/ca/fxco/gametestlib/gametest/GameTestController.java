package ca.fxco.gametestlib.gametest;

import ca.fxco.api.gametestlib.control.GameTestControl;
import ca.fxco.api.gametestlib.progressbar.DefaultProgressBar;
import ca.fxco.api.gametestlib.progressbar.GameTestProgressBar;
import lombok.Getter;
import net.minecraft.gametest.framework.LogTestReporter;
import net.minecraft.gametest.framework.TestReporter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class GameTestController {

    private final Set<TestReporter> testReporters = new LinkedHashSet<>();
    private GameTestProgressBar progressBar;

    public GameTestController() {
    }

    public void loadControl(GameTestControl control) {
        this.testReporters.addAll(control.registerTestReporters());
        GameTestProgressBar newProgressBar = control.registerGameTestProgressBar();
        if (newProgressBar != null && newProgressBar.getPriority() >= this.progressBar.getPriority()) {
            this.progressBar = newProgressBar;
        }
    }

    public void initializeDefaults() {
        if (this.testReporters.size() == 0) {
            this.testReporters.add(new LogTestReporter()); // Default test reporter
        }
        if (this.progressBar == null) {
            this.progressBar = new DefaultProgressBar();
        }
    }
}
