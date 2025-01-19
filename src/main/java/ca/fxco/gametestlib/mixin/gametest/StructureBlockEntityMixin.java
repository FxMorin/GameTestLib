package ca.fxco.gametestlib.mixin.gametest;

import ca.fxco.gametestlib.GameTestLibMod;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(StructureBlockEntity.class)
public abstract class StructureBlockEntityMixin extends BlockEntity {

    @Shadow @Nullable protected abstract StructureTemplate getStructureTemplate(ServerLevel serverLevel);

    public StructureBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Redirect(
            method = {
                    "isStructureLoadable",
                    "placeStructureIfSameSize",
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;" +
                            "get(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;"
            )
    )
    private Optional<StructureTemplate> getFromTestStructures(StructureTemplateManager instance,
                                                              ResourceLocation resourceLocation) {
        if (GameTestLibMod.GAMETEST_ACTIVE && this.getLevel() instanceof ServerLevel serverLevel) {
            try {
                return Optional.of(getStructureTemplate(serverLevel));
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
            return Optional.empty();
        }
        return instance.get(resourceLocation);
    }
}
