package ca.fxco.api.gametestlib.config.binder;

import ca.fxco.api.gametestlib.config.ParsedValue;
import net.minecraft.server.MinecraftServer;

import java.util.Map;

/**
 * Binders will allow you to register all the config value you want to use within your tests
 */
public abstract class ConfigBinder {

    /**
     * Register all the config values
     */
    public abstract Map<String, ParsedValue<?>> registerConfigValues(MinecraftServer server);
}
