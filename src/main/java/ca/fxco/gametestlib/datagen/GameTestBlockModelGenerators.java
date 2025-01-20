package ca.fxco.gametestlib.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.BlockStateGenerator;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GameTestBlockModelGenerators extends BlockModelGenerators {

    public static List<Block> PROCESSED_BLOCKS = new ArrayList<>();

    public GameTestBlockModelGenerators(BlockModelGenerators generators) {
        super(
                wrapGeneratorConsumer(generators.blockStateOutput),
                generators.itemModelOutput,
                generators.modelOutput
        );
        PROCESSED_BLOCKS.clear();
    }

    private static Consumer<BlockStateGenerator> wrapGeneratorConsumer(Consumer<BlockStateGenerator> consumer) {
        return generator -> {
            PROCESSED_BLOCKS.add(generator.getBlock());
            consumer.accept(generator);
        };
    }
}
