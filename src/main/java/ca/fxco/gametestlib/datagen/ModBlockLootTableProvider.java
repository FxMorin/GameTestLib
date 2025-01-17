package ca.fxco.gametestlib.datagen;

import ca.fxco.gametestlib.GameTestLibMod;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiConsumer;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    protected ModBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        LOGGER.info("Generating block loot tables...");

        // Set dropSelf for all GameTestLib blocks
        BuiltInRegistries.BLOCK.entrySet().forEach(entry -> {
            if (entry.getKey().location().getNamespace().equals(GameTestLibMod.MOD_ID)) {
                dropSelf(entry.getValue());
            }
        });

        LOGGER.info("Finished generating block loot tables!");
    }

    // FabricLootTableProvider#accept needs to be overriden because
    // we're using Mojmaps. BlockLootSubProvider#generate is called
    // accept in Yarn and thus would provide that implementation.
    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> t) {
        this.generate(t);
    }
}
