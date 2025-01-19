package ca.fxco.gametestlib.datagen;

import ca.fxco.gametestlib.GameTestLibMod;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    protected ModBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> lookup) {
        super(dataOutput, lookup);
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
}
