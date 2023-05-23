package ca.fxco.gametestlib.Utils;

import lombok.experimental.UtilityClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.lang.reflect.Constructor;
import java.util.Collection;

@UtilityClass
public class Utils {

    public static Direction fromDirection(BlockPos blockPos, BlockPos fromPos) {
        return fromPos.getY() == blockPos.getY() ?
                (fromPos.getX() == blockPos.getX() ?
                        fromPos.getZ() > blockPos.getZ() ? Direction.SOUTH : Direction.NORTH :
                        fromPos.getX() > blockPos.getX() ? Direction.EAST : Direction.WEST) :
                fromPos.getY() > blockPos.getY() ? Direction.UP : Direction.DOWN;
    }

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
