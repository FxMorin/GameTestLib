package ca.fxco.gametestlib.Utils;

import java.lang.reflect.Constructor;
import java.util.Collection;

public class Utils {

    public static <T> boolean containsAny(Collection<T> collection, Collection<T> anyOf) {
        for (T val : anyOf)
            if (collection.contains(val))
                return true;
        return false;
    }

    public static <T> T createInstance(Class<T> clazz) {
        try {
            Constructor<T> c = clazz.getDeclaredConstructor();
            c.setAccessible(true);
            return c.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
