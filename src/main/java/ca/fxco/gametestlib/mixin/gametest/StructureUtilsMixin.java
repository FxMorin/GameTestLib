package ca.fxco.gametestlib.mixin.gametest;

import ca.fxco.gametestlib.GameTestLibMod;
import ca.fxco.gametestlib.Utils.Utils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.gametest.framework.StructureUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(StructureUtils.class)
public class StructureUtilsMixin {
    @WrapOperation(
            method = "getStartCorner",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;" +
                    "get(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;"
            )
    )
    private static Optional<StructureTemplate> gtl$getTemplate(StructureTemplateManager instance,
                                                               ResourceLocation resourceLocation,
                                                               Operation<Optional<StructureTemplate>> original,
                                                               @Local(argsOnly = true) ServerLevel serverLevel) {

        if (GameTestLibMod.GAMETEST_ACTIVE) {
            try {
                return Optional.of(Utils.getStructureTemplate(resourceLocation.getPath(), serverLevel));
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
            return Optional.empty();
        }
        return instance.get(resourceLocation);
    }

    @WrapOperation(
            method = "prepareTestStructure",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;" +
                    "get(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;"
            )
    )
    private static Optional<StructureTemplate> gtl$getTemplate2(StructureTemplateManager instance,
                                                               ResourceLocation resourceLocation,
                                                               Operation<Optional<StructureTemplate>> original,
                                                               @Local(argsOnly = true) ServerLevel serverLevel) {

        if (GameTestLibMod.GAMETEST_ACTIVE) {
            try {
                return Optional.of(Utils.getStructureTemplate(resourceLocation.getPath(), serverLevel));
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
            return Optional.empty();
        }
        return instance.get(resourceLocation);
    }
}
