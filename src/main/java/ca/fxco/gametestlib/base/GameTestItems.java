package ca.fxco.gametestlib.base;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class GameTestItems {
    public static final BlockItem PULSE_STATE_BLOCK = registerBlock(GameTestBlocks.PULSE_STATE_BLOCK);
    public static final BlockItem CHECK_STATE_BLOCK = registerBlock(GameTestBlocks.CHECK_STATE_BLOCK);
    public static final BlockItem TEST_TRIGGER_BLOCK = registerBlock(GameTestBlocks.TEST_TRIGGER_BLOCK);
    public static final BlockItem GAMETEST_REDSTONE_BLOCK = registerBlock(GameTestBlocks.GAMETEST_REDSTONE_BLOCK);

    private static BlockItem registerBlock(Block block) {
        return registerBlock(block, new Item.Properties());
    }

    private static BlockItem registerBlock(Block block, Item.Properties itemProperties) {
        return register(BuiltInRegistries.BLOCK.getKey(block), new BlockItem(block, itemProperties));
    }

    private static <T extends Item> T register(ResourceLocation id, T item) {
        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }

    public static void boostrap() { }
}
