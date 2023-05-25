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
        NONE,

        /**
         * TestTrigger Blocks will give the opposite result
         */
        FLIP_TRIGGERS,

        /**
         * CheckState Blocks will flip their FailOnFound state
         */
        FLIP_CHECKS,

        /**
         * EntityInteraction Blocks will give the opposite result
         */
        FLIP_INTERACTIONS,

        /**
         * EntityInside Blocks will give the opposite result
         */
        FLIP_INSIDE,

        /**
         * Flip all custom gametest blocks
         */
        FLIP_ALL;
    }
}
