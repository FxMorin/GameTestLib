package ca.fxco.gametestlib.network;

import ca.fxco.gametestlib.network.packets.GameTestPacket;
import ca.fxco.gametestlib.network.packets.ServerboundSetCheckStatePacket;
import ca.fxco.gametestlib.network.packets.ServerboundSetPulseStatePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;

public class GameTestNetwork {

    // Clientbound = S2C
    // Serverbound = C2S

    public static void initializeServer() {
        registerServerReceiver(ServerboundSetPulseStatePacket.TYPE, ServerboundSetPulseStatePacket.STREAM_CODEC);
        registerServerReceiver(ServerboundSetCheckStatePacket.TYPE, ServerboundSetCheckStatePacket.STREAM_CODEC);
    }

    public static void initializeClient() {
        // Add client receivers here
    }

    //
    // Registering Packets
    //

    private static <T extends GameTestPacket> void registerClientReceiver(CustomPacketPayload.Type<T> type,
                                                      StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        PayloadTypeRegistry.playS2C().register(type, streamCodec);
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
            context.client().execute(() ->
                    ((GameTestPacket) payload).handleClient(context.client(), context.responseSender()));
        });
    }

    private static <T extends GameTestPacket> void registerServerReceiver(CustomPacketPayload.Type<T> type,
                                                      StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        PayloadTypeRegistry.playC2S().register(type, streamCodec);
        ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
            MinecraftServer server = context.player().getServer();
            server.execute(() ->
                    ((GameTestPacket) payload).handleServer(server, context.player(), context.responseSender()));
        });
    }

    //
    // Sending Packets
    //

    @Environment(EnvType.CLIENT)
    public static void sendToServer(GameTestPacket packet) {
        ClientPlayNetworking.send(packet);
    }
}
