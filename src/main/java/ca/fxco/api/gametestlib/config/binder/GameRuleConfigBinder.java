package ca.fxco.api.gametestlib.config.binder;

import ca.fxco.api.gametestlib.config.GameRuleParsedValue;
import ca.fxco.api.gametestlib.config.ParsedValue;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

import java.util.HashMap;
import java.util.Map;

public class GameRuleConfigBinder extends ConfigBinder {

    @Override
    public Map<String, ParsedValue<?>> registerConfigValues(MinecraftServer server) {
        Map<String, ParsedValue<?>> parsedValues = new HashMap<>();
        GameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {
            public <T extends GameRules.Value<T>> void visit(GameRules.Key<T> key, GameRules.Type<T> type) {
                parsedValues.put(key.getId(), new GameRuleParsedValue<>(key));
            }
        });
        return parsedValues;
    }
}
