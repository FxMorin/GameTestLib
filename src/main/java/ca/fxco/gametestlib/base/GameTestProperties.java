package ca.fxco.gametestlib.base;

import ca.fxco.gametestlib.blocks.EntityInteractionBlock;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class GameTestProperties {

    public static final EnumProperty<EntityInteractionBlock.InteractionType> INTERACTION_TYPE = EnumProperty.create("interaction", EntityInteractionBlock.InteractionType.class);
}
