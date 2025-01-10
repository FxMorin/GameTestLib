package ca.fxco.api.gametestlib.config.binder;

import ca.fxco.api.gametestlib.config.GameRuleResolvedValue;
import ca.fxco.api.gametestlib.config.ResolvedValue;
import net.minecraft.world.level.GameRules;

import java.util.HashMap;
import java.util.Map;

public class GameRuleConfigBinder implements ConfigBinder {

    @Override
    public Map<String, ResolvedValue<?>> registerConfigValues() {
        Map<String, ResolvedValue<?>> resolvedValues = new HashMap<>();
        GameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {
            public <T extends GameRules.Value<T>> void visit(GameRules.Key<T> key, GameRules.Type<T> type) {
                resolvedValues.put(key.getId(), new GameRuleResolvedValue<>(key));
            }
        });
        return resolvedValues;
    }
}
