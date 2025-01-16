package ca.fxco.gametestlib.gametest.expansion;

import ca.fxco.api.gametestlib.gametest.Config;
import ca.fxco.api.gametestlib.gametest.GameTestChanges;
import ca.fxco.gametestlib.gametest.TestGenerator;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.*;

@Getter
public class TestFunctionGenerator {

    private final Method method;
    private final SortedSet<String> values;
    private final Map<Pair<String, String>, GameTestChanges> specialValues = new HashMap<>();
    private final ParsedGameTestConfig gameTestConfig;
    private final TestGenerator.GameTestData.GameTestDataBuilder gameTestDataBuilder;

    public TestFunctionGenerator(Method method, ParsedGameTestConfig gameTestConfig,
                                 TestGenerator.GameTestData.GameTestDataBuilder gameTestDataBuilder) {
        this.method = method;
        this.gameTestConfig = gameTestConfig;
        this.gameTestDataBuilder = gameTestDataBuilder;

        this.values = new TreeSet<>(List.of(gameTestConfig.value()));
        for (Config config : gameTestConfig.config()) {
            this.values.addAll(List.of(config.optionName()));
            int i = 0;
            String[] optionNames = config.optionName().length == 0 ? gameTestConfig.value() : config.optionName();
            for (String option : optionNames) {
                this.specialValues.put(new Pair<>(option, config.value()[i]), config.changes());
                i++;
            }
        }
    }

    public <T> GameTestChanges getChangesForOption(String optionName, T currentValue) {
        return specialValues.getOrDefault(new Pair<>(optionName, currentValue.toString()), GameTestChanges.NONE);
    }

}
