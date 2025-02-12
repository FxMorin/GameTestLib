package ca.fxco.gametestlib.blocks;

import ca.fxco.gametestlib.base.GameTestBlockEntities;
import ca.fxco.gametestlib.gametest.block.BlockStateExp;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Setter
@Getter
public class CheckStateBlockEntity extends BlockEntity {

    private boolean failOnFound = false;
    private int tick = -1; // -1 = no onTick conditions
    private Direction direction = Direction.UP; // TODO: Switch to use a blockstate direction once it has a texture! Maybe? May be cleaner this way
    private BlockStateExp blockStateExp = BlockStateExp.EMPTY;

    public CheckStateBlockEntity(BlockPos pos, BlockState state) {
        this(GameTestBlockEntities.CHECK_STATE_BLOCK_ENTITY, pos, state);
    }

    public CheckStateBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Environment(EnvType.CLIENT)
    public boolean usedBy(Player player) {
        if (!player.canUseGameMasterBlocks()) {
            return false;
        }
        if (player instanceof LocalPlayer localPlayer) {
            localPlayer.minecraft.setScreen(new CheckStateScreen(this));
        }
        return true;
    }

    public Boolean runGameTestChecks(GameTestHelper helper, BlockPos checkPos) {
        BlockState checkState = helper.getBlockState(checkPos);
        if (this.getBlockStateExp().matches(checkState)) {
            if (this.isFailOnFound()) {
                helper.fail("Block at position " + checkPos.toShortString() + " is: " + checkState);
                return false;
            }
            return true;
        } else if (this.tick > -1) {
            if (this.isFailOnFound()) {
                return true;
            }
            helper.fail("Block at position " + checkPos.toShortString() + ": " + checkState + " did not match the required expression, on tick: " + helper.getTick());
            return false;
        }
        return null;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putBoolean("fail", failOnFound);
        compoundTag.putByte("dir", (byte) direction.ordinal());
        if (tick != -1) {
            compoundTag.putInt("tick", tick);
        }
        if (blockStateExp != null) {
            compoundTag.put("BS", blockStateExp.write());
        }
    }

    @Override
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        this.failOnFound = compoundTag.getBoolean("fail");
        this.direction = Direction.values()[compoundTag.getByte("dir")];
        this.tick = compoundTag.contains("tick") ? compoundTag.getInt("tick") : -1;
        if (compoundTag.contains("BS")) {
            this.blockStateExp = BlockStateExp.read(compoundTag.getCompound("BS"));
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }
}
