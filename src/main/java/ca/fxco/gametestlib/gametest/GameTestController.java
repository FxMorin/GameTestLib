package ca.fxco.gametestlib.gametest;

import ca.fxco.api.gametestlib.control.GameTestControl;
import ca.fxco.api.gametestlib.progressbar.DefaultProgressBar;
import ca.fxco.api.gametestlib.progressbar.GameTestProgressBar;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.gametest.framework.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
public class GameTestController {

    private final Set<TestReporter> testReporters = new LinkedHashSet<>();
    private final Set<GameTestControl.GameTestListenerFactory> gameTestListeners = new LinkedHashSet<>();
    private GameTestProgressBar progressBar;

    public void loadControl(GameTestControl control) {
        this.testReporters.addAll(control.registerTestReporters());
        this.gameTestListeners.addAll(control.registerGameTestListeners());
        GameTestProgressBar newProgressBar = control.registerGameTestProgressBar();
        if (newProgressBar != null && (this.progressBar == null || newProgressBar.getPriority() <= this.progressBar.getPriority())) {
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
