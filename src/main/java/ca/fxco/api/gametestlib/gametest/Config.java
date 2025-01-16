package ca.fxco.api.gametestlib.gametest;

public @interface Config {

    /**
     * The string value of the option what should be matched
     */
    String[] value();

    /**
     * Strings must be a valid config field names
     */
    String[] optionName();

    /**
     * Allows you to set this value as ignored.
     * If the Config is ignored, this will make the value required
     */
    // TODO: Implement a way to ignore specific values
    //  not absolutly needed at the moment since only one rule can run at a time
    //boolean invertRequire() default false;

    /**
     * Specify what should change about the test when these config values are used.
     */
    GameTestChanges changes() default GameTestChanges.NONE;
}
