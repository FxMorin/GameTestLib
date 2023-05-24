package ca.fxco.gametestlib.base;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static ca.fxco.gametestlib.GameTestLibMod.id;

public class GameTestCreativeModeTabs {

    public static final CreativeModeTab GENERAL = FabricItemGroup.builder(id("main"))
            .icon(() -> new ItemStack(GameTestBlocks.GAMETEST_REDSTONE_BLOCK))
            .displayItems((featureFlags, output, hasPermissions) -> {
                output.accept(GameTestItems.GAMETEST_REDSTONE_BLOCK);
                output.accept(GameTestItems.TEST_TRIGGER_BLOCK);
                output.accept(GameTestItems.PULSE_STATE_BLOCK);
                output.accept(GameTestItems.CHECK_STATE_BLOCK);
                output.accept(GameTestItems.ENTITY_INTERACTION_BLOCK);
            })
            .build();

    public static void bootstrap() { }

}
