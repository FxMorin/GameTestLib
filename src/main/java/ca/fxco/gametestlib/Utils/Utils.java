package ca.fxco.gametestlib.Utils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.experimental.UtilityClass;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.StructureUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;

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

    //TODO probably just temporarily until 1.21.5
    public static StructureTemplate getStructureTemplate(String string, ServerLevel serverLevel) {
        StructureTemplateManager structureTemplateManager = serverLevel.getStructureManager();
        Optional<StructureTemplate> optional = structureTemplateManager.get(ResourceLocation.parse(string));
        if (optional.isPresent()) {
            return (StructureTemplate)optional.get();
        } else {
            String string2 = string + ".snbt";
            Path path = Paths.get(StructureUtils.testStructuresDir, string2);
            CompoundTag compoundTag = tryLoadStructure(path);
            if (compoundTag == null) {
                throw new RuntimeException("Could not find structure file " + path + ", and the structure is not available in the world structures either.");
            } else {
                return structureTemplateManager.readStructure(compoundTag);
            }
        }
    }

    @Nullable
    public static CompoundTag tryLoadStructure(Path path) {
        try {
            BufferedReader bufferedReader = Files.newBufferedReader(path);
            String string = IOUtils.toString(bufferedReader);
            return NbtUtils.snbtToStructure(string);
        } catch (IOException var3) {
            return null;
        } catch (CommandSyntaxException var4) {
            throw new RuntimeException("Error while trying to load structure " + path, var4);
        }
    }
}
