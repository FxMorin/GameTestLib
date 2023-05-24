package ca.fxco.gametestlib.gametest.expansion;

import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class ParsedGameTestConfig {

    private final boolean customBlocks;
    private final String[] value;
    private final Config[] config;
    private final boolean ignored;
    private final boolean combined;

    public boolean customBlocks() {
        return this.customBlocks;
    }

    public String[] value() {
        return this.value;
    }

    public Config[] config() {
        return this.config;
    }

    public boolean ignored() {
        return this.ignored;
    }

    public boolean combined() {
        return this.combined;
    }

    // GameTestConfig being merged will override values
    public ParsedGameTestConfig createMerged(GameTestLib gameTestLib, boolean clazz) {
        if (clazz) {
            // These warning are here otherwise your values would not be doing what you expected them to do!
            if (this.customBlocks != gameTestLib.customBlocks()) {
                throw new RuntimeException("CustomBlocks cannot be different than the class @GameTestConfig customBlocks value!");
            }
            if (this.ignored != gameTestLib.ignored()) {
                throw new RuntimeException("Ignored cannot be different than the class @GameTestConfig ignored value!");
            }
            if (this.combined != gameTestLib.combined()) {
                throw new RuntimeException("Combined cannot be different than the class @GameTestConfig combined value!");
            }
        }
        Set<String> tempSet = new HashSet<>(List.of(this.value));
        tempSet.addAll(List.of(gameTestLib.value()));
        Set<Config> tempSet2 = new HashSet<>(List.of(this.config));
        tempSet2.addAll(List.of(gameTestLib.config()));
        return new ParsedGameTestConfig(
                gameTestLib.customBlocks(),
                tempSet.toArray(new String[0]),
                tempSet2.toArray(new Config[0]),
                gameTestLib.ignored(),
                gameTestLib.combined()
        );
    }

    public static ParsedGameTestConfig of(GameTestLib gameTestLib) {
        return new ParsedGameTestConfig(
                gameTestLib.customBlocks(),
                gameTestLib.value(),
                gameTestLib.config(),
                gameTestLib.ignored(),
                gameTestLib.combined()
        );
    }
}
