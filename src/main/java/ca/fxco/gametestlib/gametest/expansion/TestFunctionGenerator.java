package ca.fxco.gametestlib.gametest.expansion;

import ca.fxco.api.gametestlib.gametest.Change;
import ca.fxco.api.gametestlib.gametest.Config;
import ca.fxco.api.gametestlib.gametest.GameTestChanges;
import ca.fxco.gametestlib.gametest.TestGenerator;
import com.mojang.datafixers.util.Pair;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.*;

@Getter
public class TestFunctionGenerator {

    private final Method method;

    private final Map<Pair<String, String>, GameTestChanges> changeValues = new HashMap<>();
    private final ParsedGameTestConfig gameTestConfig;
    private final TestGenerator.GameTestData.GameTestDataBuilder gameTestDataBuilder;

    public TestFunctionGenerator(Method method, ParsedGameTestConfig gameTestConfig,
                                 TestGenerator.GameTestData.GameTestDataBuilder gameTestDataBuilder) {
        this.method = method;
        this.gameTestConfig = gameTestConfig;
        this.gameTestDataBuilder = gameTestDataBuilder;

        for (Config config : gameTestConfig.config()) {
            for (Change change : config.changes()) {
                for (String name : config.name()) {
                    for (String value : change.value()) {
                        this.changeValues.put(new Pair<>(name, value), change.change());
                    }
                }
            }
        }
    }

    public <T> GameTestChanges getChangesForOption(String optionName, T currentValue) {
        return changeValues.getOrDefault(new Pair<>(optionName, currentValue.toString()), GameTestChanges.NONE);
    }

}
