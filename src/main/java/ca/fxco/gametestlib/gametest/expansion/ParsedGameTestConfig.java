package ca.fxco.gametestlib.gametest.expansion;

import ca.fxco.api.gametestlib.gametest.Config;
import ca.fxco.api.gametestlib.gametest.GameTestLib;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record ParsedGameTestConfig(Set<String> requiredValues, Set<String> invertedValues, Set<String> variants,
                                   Config[] config, boolean customBlocks) {

    /**
     * Creates a new {@link ParsedGameTestConfig}, which is a combination of this one and the input.
     * The input overrides this one, when conflicting.
     *
     * @param gameTestLib The input gameTestLib
     * @return A new combined {@link ParsedGameTestConfig}
     */
    public ParsedGameTestConfig createChild(GameTestLib gameTestLib) {
        Set<String> variants = new HashSet<>(this.variants);
        variants.addAll(List.of(gameTestLib.variants()));
        Set<Config> configs = new HashSet<>(List.of(this.config));
        configs.addAll(List.of(gameTestLib.config()));

        Set<String> requiredValues;
        if (gameTestLib.value().length > 0) {
            requiredValues = new HashSet<>(this.requiredValues);
            requiredValues.addAll(List.of(gameTestLib.value()));
        } else {
            requiredValues = this.requiredValues;
        }
        Set<String> invertedValues;
        if (gameTestLib.inverted().length > 0) {
            invertedValues = new HashSet<>(this.invertedValues);
            invertedValues.addAll(List.of(gameTestLib.inverted()));
        } else {
            invertedValues = this.invertedValues;
        }
        return new ParsedGameTestConfig(
                requiredValues,
                invertedValues,
                variants,
                configs.toArray(new Config[0]),
                gameTestLib.customBlocks()
        );
    }

    public static ParsedGameTestConfig of(GameTestLib gameTestLib) {
        return new ParsedGameTestConfig(
                Set.of(gameTestLib.value()),
                Set.of(gameTestLib.inverted()),
                Set.of(gameTestLib.variants()),
                gameTestLib.config(),
                gameTestLib.customBlocks()
        );
    }
}
