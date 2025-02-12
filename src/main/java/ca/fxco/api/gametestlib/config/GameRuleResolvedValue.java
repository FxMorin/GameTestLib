package ca.fxco.api.gametestlib.config;

import ca.fxco.gametestlib.GameTestLibMod;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule;
import net.minecraft.gametest.framework.GameTestServer;
import net.minecraft.world.level.GameRules;

public class GameRuleResolvedValue<T extends GameRules.Value<T>> extends ResolvedValue<String> {

    private final GameRules.Key<T> key;
    private final T originalValue;

    public GameRuleResolvedValue(GameRules.Key<T> key) {
        this.key = key;
        this.originalValue = GameTestServer.TEST_GAME_RULES.getRule(key);
    }

    @Override
    public String[] getTestingValues() { // TODO: Allow manual submissions for values
        if (originalValue instanceof GameRules.BooleanValue) {
            return new String[]{"true", "false"};
        } else if (originalValue instanceof GameRules.IntegerValue) {
            return new String[]{"-1", "0", "1"}; // TODO: Should be custom input
        } else if (originalValue instanceof DoubleRule) {
            return new String[]{"-1.0", "0.0", "1.0"}; // TODO: Should be custom input
        } else if (originalValue instanceof EnumRule<?> enumRule) {
            Object[] enums = enumRule.getEnumClass().getEnumConstants();
            String[] vals = new String[enums.length];
            for (int i = 0; i < enums.length; i++)
                if (enums[i] instanceof Enum<?> e)
                    vals[i] = e.name();
            return vals;
        }
        // TODO: Add logger to mention that this type was not found
        return new String[0];
    }

    @Override
    public void setValue(String value) {
        GameTestLibMod.CURRENT_SERVER.getGameRules().getRule(key).deserialize(value);
    }

    @Override
    public void setDefault() {
        GameTestLibMod.CURRENT_SERVER.getGameRules().getRule(key).setFrom(this.originalValue, GameTestLibMod.CURRENT_SERVER);
    }
}
