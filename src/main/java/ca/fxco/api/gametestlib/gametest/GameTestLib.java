package ca.fxco.api.gametestlib.gametest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used on either classes or fields within a gametest config class.
 * When applied to the class, it's applied to all fields within that class.
 *
 * @author FX
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GameTestLib {

    /**
     * Required config fields, these config fields are enabled before running the test.
     * If {@link #inverted}, these config
     */
    String[] value() default {};

    /**
     * Variants are normal config fields.
     * However, instead of being required, they test all the variants of the option.
     * All strings must be a valid config field name.
     */
    String[] variants() default {};

    /**
     * Pass config annotations to specify how variants should work
     */
    Config[] config() default {};

    /**
     * If config fields from {@link #value} should be inverted
     */
    boolean inverted() default false;

    /**
     * If the custom gametest blocks should be used for this test
     */
    boolean customBlocks() default true;
}
