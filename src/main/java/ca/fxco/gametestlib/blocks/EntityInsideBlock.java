package ca.fxco.gametestlib.blocks;

import ca.fxco.gametestlib.base.GameTestProperties;
import ca.fxco.gametestlib.gametest.GameTestUtil;
import ca.fxco.api.gametestlib.gametest.GameTestActionBlock;
import ca.fxco.api.gametestlib.gametest.GameTestChanges;
import ca.fxco.gametestlib.gametest.expansion.GameTestGroupConditions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EntityInsideBlock extends BaseEntityBlock implements GameMasterBlock, GameTestActionBlock {

    public static final EntityTypeTest<Entity, ?> ANY_TYPE = new EntityTypeTest<>() {
        public Entity tryCast(Entity entity) {
            return entity;
        }
        public Class<? extends Entity> getBaseClass() {
            return Entity.class;
        }
    };

    public static final EntityTypeTest<Entity, ?> LIVING_ENTITY = EntityTypeTest.forClass(LivingEntity.class);

    public static final IntegerProperty DELAY = GameTestProperties.DELAY_32;
    public static final EnumProperty<EntityInsideBlock.EntityType> TYPE = GameTestProperties.ENTITY_TYPE;

    public EntityInsideBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(DELAY, 0)
                .setValue(TYPE, EntityType.ALL)
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EntityInsideBlockEntity(blockPos, blockState);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.empty();
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player,
                                 InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (player.canUseGameMasterBlocks()) {
            if (player.isShiftKeyDown()) {
                level.setBlock(blockPos, blockState.cycle(TYPE), Block.UPDATE_INVISIBLE | Block.UPDATE_CLIENTS);
            } else {
                level.setBlock(blockPos, blockState.cycle(DELAY), Block.UPDATE_INVISIBLE | Block.UPDATE_CLIENTS);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DELAY, TYPE);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.BLOCK;
    }

    //
    // GameTest Action Logic
    //

    @Nullable
    @Override
    public GameTestGroupConditions.TestCondition addTestCondition(GameTestHelper helper, BlockState state,
                                                                  BlockPos blockPos, GameTestChanges changes) {
        EntityInsideBlock.EntityType entityType = state.getValue(GameTestProperties.ENTITY_TYPE);
        EntityInsideCondition condition = new EntityInsideCondition(
                blockPos,
                entityType,
                changes.shouldFlip(GameTestChanges.FLIP_INSIDE)
        );
        Minecraft.getInstance().debugRenderer.gameTestDebugRenderer.addMarker(
                blockPos,
                0xffffff77,
                entityType.getSerializedName(),
                Integer.MAX_VALUE
        );
        GameTestUtil.setBlock(helper, blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_INVISIBLE);
        return condition;
    }

    public static class EntityInsideCondition extends GameTestGroupConditions.BasicTestCondition {
        private final BlockPos blockPos;
        private final boolean flip;

        private final EntityInsideBlock.EntityType entityType;

        public EntityInsideCondition(BlockPos blockPos, EntityInsideBlock.EntityType entityType, boolean flip) {
            this.blockPos = blockPos.immutable();
            this.entityType = entityType;
            this.flip = flip;
        }

        @Override
        public Boolean runCheck(GameTestHelper helper) {
            Level level = helper.getLevel();
            AABB aabb = new AABB(helper.absolutePos(blockPos));
            List<? extends Entity> list = switch (this.entityType) {
                case ALL -> level.getEntities(ANY_TYPE, aabb, Entity::isAlive);
                case ITEM -> level.getEntities(net.minecraft.world.entity.EntityType.ITEM, aabb, Entity::isAlive);
                case PLAYER -> level.getEntities(net.minecraft.world.entity.EntityType.PLAYER, aabb, Entity::isAlive);
                case LIVING_ENTITY -> level.getEntities(LIVING_ENTITY, aabb, Entity::isAlive);
                case NONE_LIVING_ENTITY -> level.getEntities(ANY_TYPE, aabb, entity ->
                        entity.isAlive() && !(entity instanceof LivingEntity)
                );
            };
            if (list.size() != 0) { // There is an entity touching the area
                if (this.flip) {
                    helper.fail("Entity Inside at " + this.blockPos.toShortString());
                    return false;
                }
                return true;
            }
            return null;
        }
    }

    @AllArgsConstructor
    public enum EntityType implements StringRepresentable {
        ALL("all"),
        LIVING_ENTITY("living"),
        NONE_LIVING_ENTITY("none_living"),
        ITEM("item"),
        PLAYER("player");

        @Getter
        private final String serializedName;

        public String toString() {
            return this.serializedName;
        }
    }
}
