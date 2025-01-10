package ca.fxco.api.gametestlib.config;

/**
 * The format used to specify a parsed value.
 * Unlike the name, it does not need to be parsed. Heck the value does not even need to be here
 */
public abstract class ResolvedValue<T> {

    /**
     * @return An array of all values that should be attempted
     */
    public abstract T[] getTestingValues();

    /**
     * Set the value
     */
    public abstract void setValue(T value);

    /**
     * Set the value to its default state
     */
    public abstract void setDefault();

}
