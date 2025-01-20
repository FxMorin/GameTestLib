package ca.fxco.gametestlib.network.packets;

import ca.fxco.gametestlib.blocks.CheckStateBlockEntity;
import ca.fxco.gametestlib.gametest.block.BlockStateExp;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

import static ca.fxco.gametestlib.GameTestLibMod.id;

public record ServerboundSetCheckStatePacket(
        BlockPos blockPos,
        int tick,
        boolean failOnFound,
        Direction direction,
        BlockStateExp blockStateExp
) implements GameTestPacket {

    public static final CustomPacketPayload.Type<ServerboundSetCheckStatePacket> TYPE =
            new CustomPacketPayload.Type<>(id("check_state_block"));

    public static final StreamCodec<FriendlyByteBuf, ServerboundSetCheckStatePacket> STREAM_CODEC = StreamCodec
            .composite(
                    BlockPos.STREAM_CODEC,
                    ServerboundSetCheckStatePacket::blockPos,
                    ByteBufCodecs.INT,
                    ServerboundSetCheckStatePacket::tick,
                    ByteBufCodecs.BOOL,
                    ServerboundSetCheckStatePacket::failOnFound,
                    Direction.STREAM_CODEC,
                    ServerboundSetCheckStatePacket::direction,
                    BlockStateExp.STREAM_CODEC,
                    ServerboundSetCheckStatePacket::blockStateExp,
                    ServerboundSetCheckStatePacket::new

    );

    @Override
    public void handleServer(MinecraftServer server, ServerPlayer fromPlayer, PacketSender packetSender) {
        if (fromPlayer.canUseGameMasterBlocks()) {
            BlockEntity blockEntity = fromPlayer.level().getBlockEntity(this.blockPos);
            if (blockEntity instanceof CheckStateBlockEntity checkStateBlockEntity) {
                checkStateBlockEntity.setTick(this.tick);
                checkStateBlockEntity.setFailOnFound(this.failOnFound);
                checkStateBlockEntity.setDirection(this.direction);
                checkStateBlockEntity.setBlockStateExp(this.blockStateExp);

                // TODO: Add checks to make sure the values are valid?

                checkStateBlockEntity.setChanged();
            }
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
