package ca.fxco.api.gametestlib.gametest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameTestChanges {

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

    public boolean shouldFlip(GameTestChanges checkAgainst) {
        if (this == FLIP_ALL) {
            return true;
        }
        return this == checkAgainst;
    }
}
