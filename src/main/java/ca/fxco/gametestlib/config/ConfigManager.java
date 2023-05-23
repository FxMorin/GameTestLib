package ca.fxco.gametestlib.config;

import ca.fxco.api.gametestlib.config.ParsedValue;
import ca.fxco.gametestlib.GameTestLib;
import ca.fxco.api.gametestlib.config.binder.ConfigBinder;
import lombok.NoArgsConstructor;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This will handle changing the config options, no matter how you implemented them
 */
@NoArgsConstructor
public class ConfigManager {

    private static final Logger LOGGER = Logger.getLogger(GameTestLib.MOD_ID);

    private final Map<String, ParsedValue<?>> values = new HashMap<>();

    public void loadBinder(ConfigBinder binder) {
        this.values.putAll(binder.registerConfigValues());
    }

    public Optional<ParsedValue<?>> get(String valueName) {
        if (this.values.containsKey(valueName)) {
            return Optional.of(this.values.get(valueName));
        }
        LOGGER.log(Level.WARNING, "Config Option `" + valueName + "` does not exist and will be skipped!");
        return Optional.empty();
    }

}
