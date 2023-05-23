package ca.fxco.gametestlib.datagen;

import ca.fxco.gametestlib.base.GameTestBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        LOGGER.info("Generating blockstate definitions and models...");

        generator.createTrivialCube(GameTestBlocks.PULSE_STATE_BLOCK);
        generator.createTrivialCube(GameTestBlocks.CHECK_STATE_BLOCK);

        registerInvertedBlock(generator, GameTestBlocks.TEST_TRIGGER_BLOCK);
        registerPoweredBlock(generator, GameTestBlocks.GAMETEST_REDSTONE_BLOCK);

    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {}

    public static void registerCubeTextureMap(BlockModelGenerators generator, Block block,
                                              ResourceLocation baseTexture, @Nullable String suffix) {
        TextureMapping halfBlockTextureMap = new TextureMapping().put(TextureSlot.ALL, baseTexture);
        if (suffix == null) {
            ModelTemplates.CUBE_ALL.create(block, halfBlockTextureMap, generator.modelOutput);
        } else {
            ModelTemplates.CUBE_ALL.createWithSuffix(block, suffix, halfBlockTextureMap, generator.modelOutput);
        }
    }

    public static void registerPoweredBlock(BlockModelGenerators generator, Block block) {
        ResourceLocation powerOff = ModelLocationUtils.getModelLocation(block);
        ResourceLocation powerOn = ModelLocationUtils.getModelLocation(block, "_on");
        registerBlockWithCustomStates(generator, block,
                PropertyDispatch.property(BlockStateProperties.POWERED)
                        .select(false, Variant.variant().with(VariantProperties.MODEL, powerOff))
                        .select(true, Variant.variant().with(VariantProperties.MODEL, powerOn)));
        registerCubeTextureMap(generator, block, powerOff, null);
        registerCubeTextureMap(generator, block, powerOn, "_on");
    }

    public static void registerInvertedBlock(BlockModelGenerators generator, Block block) {
        ResourceLocation defaultRes = ModelLocationUtils.getModelLocation(block);
        ResourceLocation invertedRes = ModelLocationUtils.getModelLocation(block, "_inverted");
        registerBlockWithCustomStates(generator, block,
                PropertyDispatch.property(BlockStateProperties.INVERTED)
                        .select(false, Variant.variant().with(VariantProperties.MODEL, defaultRes))
                        .select(true, Variant.variant().with(VariantProperties.MODEL, invertedRes)));
        registerCubeTextureMap(generator, block, defaultRes, null);
        registerCubeTextureMap(generator, block, invertedRes, "_inverted");
    }

    public static void registerBlockWithCustomStates(BlockModelGenerators generator, Block halfBlock, PropertyDispatch customStates) {
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(halfBlock, Variant.variant()
                .with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(halfBlock))).with(customStates)
        );
    }
}
