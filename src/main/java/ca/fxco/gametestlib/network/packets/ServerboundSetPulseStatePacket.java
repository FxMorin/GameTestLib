package ca.fxco.gametestlib.network.packets;

import ca.fxco.gametestlib.blocks.PulseStateBlockEntity;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static ca.fxco.gametestlib.GameTestLibMod.id;

public record ServerboundSetPulseStatePacket(
        BlockPos blockPos,
        int delay,
        int duration,
        BlockState firstBlockState,
        BlockState pulseBlockState,
        BlockState lastBlockState
) implements GameTestPacket {

    private static final StreamCodec<ByteBuf, BlockState> BLOCKSTATE_STREAM_CODEC =
            ByteBufCodecs.fromCodec(BlockState.CODEC);

    public static final StreamCodec<FriendlyByteBuf, ServerboundSetPulseStatePacket> STREAM_CODEC =
            StreamCodec.composite(
                    BlockPos.STREAM_CODEC,
                    ServerboundSetPulseStatePacket::blockPos,
                    ByteBufCodecs.INT,
                    ServerboundSetPulseStatePacket::delay,
                    ByteBufCodecs.INT,
                    ServerboundSetPulseStatePacket::duration,
                    BLOCKSTATE_STREAM_CODEC,
                    ServerboundSetPulseStatePacket::firstBlockState,
                    BLOCKSTATE_STREAM_CODEC,
                    ServerboundSetPulseStatePacket::pulseBlockState,
                    BLOCKSTATE_STREAM_CODEC,
                    ServerboundSetPulseStatePacket::lastBlockState,
                    ServerboundSetPulseStatePacket::new
            );

    public static final CustomPacketPayload.Type<ServerboundSetPulseStatePacket> TYPE =
            new CustomPacketPayload.Type<>(id("pulse_state_block"));

    @Override
    public void handleServer(MinecraftServer server, ServerPlayer fromPlayer, PacketSender packetSender) {
        if (fromPlayer.canUseGameMasterBlocks()) {
            BlockEntity blockEntity = fromPlayer.level().getBlockEntity(this.blockPos);
            if (blockEntity instanceof PulseStateBlockEntity pulseStateBlockEntity) {
                pulseStateBlockEntity.setDelay(this.delay);
                pulseStateBlockEntity.setDuration(this.duration);
                pulseStateBlockEntity.setFirstBlockState(this.firstBlockState);
                pulseStateBlockEntity.setPulseBlockState(this.pulseBlockState);
                pulseStateBlockEntity.setLastBlockState(this.lastBlockState);

                // TODO: Add checks to make sure the values are valid?

                pulseStateBlockEntity.setChanged();
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
