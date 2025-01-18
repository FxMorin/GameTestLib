package ca.fxco.gametestlib.base;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static ca.fxco.gametestlib.GameTestLibMod.id;

public class GameTestCreativeModeTabs {
    public static final ResourceKey<CreativeModeTab> GENERAL = ResourceKey.create(Registries.CREATIVE_MODE_TAB, id("main"));

    public static void bootstrap() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, GENERAL, FabricItemGroup.builder()
                .icon(() -> new ItemStack(GameTestBlocks.GAMETEST_REDSTONE_BLOCK))
                .title(Component.translatable("itemGroup.gametestlib.main"))
                .displayItems((displayParameters, output) -> {
                    output.accept(GameTestItems.GAMETEST_REDSTONE_BLOCK);
                    output.accept(GameTestItems.TEST_TRIGGER_BLOCK);
                    output.accept(GameTestItems.PULSE_STATE_BLOCK);
                    output.accept(GameTestItems.CHECK_STATE_BLOCK);
                    output.accept(GameTestItems.ENTITY_INTERACTION_BLOCK);
                    output.accept(GameTestItems.ENTITY_INSIDE_BLOCK);
                })
                .build());
    }

}
