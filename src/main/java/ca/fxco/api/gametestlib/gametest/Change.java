package ca.fxco.api.gametestlib.gametest;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used within {@link Config} annotations to specify how options should work.
 *
 * @see Config
 * @author FX
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface Change {

    /**
     * Values that should apply this change
     */
    String[] value() default {};

    /**
     * The changes that this test does, when the value matches {@link #value}
     */
    GameTestChanges change() default GameTestChanges.NONE;
}
