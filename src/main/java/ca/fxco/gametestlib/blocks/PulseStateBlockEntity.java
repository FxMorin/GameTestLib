package ca.fxco.gametestlib.blocks;

import ca.fxco.gametestlib.base.GameTestBlockEntities;
import lombok.Getter;
import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@Setter
@Getter
public class PulseStateBlockEntity extends BlockEntity {

    private int delay;
    private int duration;
    private boolean disableFirstBlockUpdates = false;
    private BlockState firstBlockState = Blocks.AIR.defaultBlockState();
    private BlockState pulseBlockState = Blocks.REDSTONE_BLOCK.defaultBlockState();
    private BlockState lastBlockState = Blocks.AIR.defaultBlockState();

    public PulseStateBlockEntity(BlockPos pos, BlockState state) {
        this(GameTestBlockEntities.PULSE_STATE_BLOCK_ENTITY, pos, state);
    }

    public PulseStateBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Environment(EnvType.CLIENT)
    public boolean usedBy(Player player) {
        if (!player.canUseGameMasterBlocks()) {
            return false;
        }
        if (player instanceof LocalPlayer localPlayer) {
            localPlayer.minecraft.setScreen(new PulseStateScreen(this));
        }
        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putInt("delay", delay);
        compoundTag.putInt("duration", duration);
        if (disableFirstBlockUpdates) {
            compoundTag.putBoolean("firstBlockUpdate", true);
        }
        if (firstBlockState != null) {
            compoundTag.put("firstBS", NbtUtils.writeBlockState(firstBlockState));
        }
        if (pulseBlockState != null) {
            compoundTag.put("pulseBS", NbtUtils.writeBlockState(pulseBlockState));
        }
        if (lastBlockState != null) {
            compoundTag.put("lastBS", NbtUtils.writeBlockState(lastBlockState));
        }
    }

    @Override
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        this.delay = compoundTag.getInt("delay");
        this.duration = compoundTag.getInt("duration");
        this.disableFirstBlockUpdates = compoundTag.contains("firstBlockUpdate");
        HolderLookup<Block> holderLookup = BuiltInRegistries.BLOCK.asLookup();
        if (compoundTag.contains("firstBS")) {
            this.firstBlockState = NbtUtils.readBlockState(holderLookup, compoundTag.getCompound("firstBS"));
        }
        if (compoundTag.contains("pulseBS")) {
            this.pulseBlockState = NbtUtils.readBlockState(holderLookup, compoundTag.getCompound("pulseBS"));
        }
        if (compoundTag.contains("lastBS")) {
            this.lastBlockState = NbtUtils.readBlockState(holderLookup, compoundTag.getCompound("lastBS"));
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
