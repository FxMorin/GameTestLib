package ca.fxco.gametestlib.gametest.expansion;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Unused currently
public @interface Config {

    /**
     * Strings must be a valid config field names
     */
    String[] value();

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

    @Getter
    @AllArgsConstructor
    enum GameTestChanges {

        /**
         * Nothing should change
         */
        NONE(false, false, false),

        /**
         * TestTrigger Blocks will give the opposite result
         */
        FLIP_TRIGGERS(true, false, false),

        /**
         * CheckState Blocks will flip their FailOnFound state
         */
        FLIP_CHECKS(false, true, false),

        /**
         * EntityInteraction Blocks will give the opposite result
         */
        FLIP_INTERACTIONS(false, false, true),

        /**
         * Flip all custom gametest blocks
         */
        FLIP_ALL(true, true, true);

        private final boolean flipTriggers;
        private final boolean flipChecks;
        private final boolean flipInteractions;
    }
}
