package ca.fxco.api.gametestlib.config.binder;

import ca.fxco.api.gametestlib.config.ParsedValue;

import java.util.Map;

/**
 * Binders will allow you to register all the config value you want to use within your tests
 */
public interface ConfigBinder {

    /**
     * Register all the config values
     */
    Map<String, ParsedValue<?>> registerConfigValues();
}
