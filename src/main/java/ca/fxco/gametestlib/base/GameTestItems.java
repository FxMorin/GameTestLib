package ca.fxco.gametestlib.base;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.BiFunction;
import java.util.function.Function;

public class GameTestItems {
    public static final BlockItem PULSE_STATE_BLOCK = registerBlock(GameTestBlocks.PULSE_STATE_BLOCK);
    public static final BlockItem CHECK_STATE_BLOCK = registerBlock(GameTestBlocks.CHECK_STATE_BLOCK);
    public static final BlockItem TEST_TRIGGER_BLOCK = registerBlock(GameTestBlocks.TEST_TRIGGER_BLOCK);
    public static final BlockItem GAMETEST_REDSTONE_BLOCK = registerBlock(GameTestBlocks.GAMETEST_REDSTONE_BLOCK);
    public static final BlockItem ENTITY_INTERACTION_BLOCK = registerBlock(GameTestBlocks.ENTITY_INTERACTION_BLOCK);
    public static final BlockItem ENTITY_INSIDE_BLOCK = registerBlock(GameTestBlocks.ENTITY_INSIDE_BLOCK);

    private static BlockItem registerBlock(Block block) {
        return registerBlock(block, new Item.Properties());
    }

    private static BlockItem registerBlock(Block block, Item.Properties itemProperties) {
        return registerBlock(BuiltInRegistries.BLOCK.getKey(block), BlockItem::new, block, itemProperties);
    }

    public static <T extends Item> T registerBlock(ResourceLocation id, BiFunction<Block, Item.Properties, T> function,
                                                   Block block, Item.Properties properties) {
        ResourceKey<Item> resourceKey = ResourceKey.create(Registries.ITEM, id);
        T item = function.apply(block, properties.setId(resourceKey));
        return Registry.register(BuiltInRegistries.ITEM, resourceKey, item);
    }

    public static <T extends Item> T register(ResourceLocation id, Function<Item.Properties, T> function,
                                              Item.Properties properties) {
        ResourceKey<Item> resourceKey = ResourceKey.create(Registries.ITEM, id);
        T item = function.apply(properties.setId(resourceKey));
        return Registry.register(BuiltInRegistries.ITEM, resourceKey, item);
    }

    public static void boostrap() {}
}
