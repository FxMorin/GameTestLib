package ca.fxco.gametestlib.gametest;

import ca.fxco.api.gametestlib.control.GameTestControl;
import lombok.Getter;
import net.minecraft.gametest.framework.LogTestReporter;
import net.minecraft.gametest.framework.TestReporter;

import java.util.LinkedList;
import java.util.List;

public class GameTestController {

    @Getter
    public final List<TestReporter> testReporters = new LinkedList<>();

    public GameTestController() {
    }

    public void loadControl(GameTestControl control) {
        this.testReporters.addAll(control.registerTestReporters());
    }

    public void initializeDefaults() {
        if (this.testReporters.size() == 0) {
            this.testReporters.add(new LogTestReporter()); // Default test reporter
        }
    }
}
