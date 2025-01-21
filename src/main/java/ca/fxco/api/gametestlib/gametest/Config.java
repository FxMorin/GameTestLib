package ca.fxco.api.gametestlib.gametest;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation used within {@link GameTestLib}, which is used to specify
 * different configurations for variant config fields used.
 *
 * @see GameTestLib
 * @author FX
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {

    /**
     * Array of variant ids that this config should affect.
     * Variant id's must have been defined in the variants.
     */
    String[] name() default {};

    /**
     * Array of string values to test.
     * If left empty, all the default test values are used.
     */
    String[] testValues() default {};

    /**
     * Array of changes that need to be applied for each value.
     * Specifies what should change for these values.
     */
    Change[] changes() default {};
}
