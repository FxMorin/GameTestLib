package ca.fxco.gametestlib.base;

import ca.fxco.gametestlib.blocks.EntityInsideBlock;
import ca.fxco.gametestlib.blocks.EntityInteractionBlock;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class GameTestProperties {

    public static final EnumProperty<EntityInteractionBlock.InteractionType> INTERACTION_TYPE = EnumProperty.create("interaction", EntityInteractionBlock.InteractionType.class);
    public static final EnumProperty<EntityInsideBlock.EntityType> ENTITY_TYPE = EnumProperty.create("entity", EntityInsideBlock.EntityType.class);
    public static final IntegerProperty DELAY_32 = IntegerProperty.create("delay", 0, 31);
}
