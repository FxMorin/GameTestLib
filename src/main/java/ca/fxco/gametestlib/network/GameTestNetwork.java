package ca.fxco.gametestlib.network;

import ca.fxco.gametestlib.network.packets.GameTestPacket;
import ca.fxco.gametestlib.network.packets.ServerboundSetCheckStatePacket;
import ca.fxco.gametestlib.network.packets.ServerboundSetPulseStatePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ca.fxco.gametestlib.GameTestLib.id;

public class GameTestNetwork {

    // Clientbound = S2C
    // Serverbound = C2S

    private static final HashMap<Class<? extends GameTestPacket>, ResourceLocation> CLIENTBOUND_PACKET_TYPES = new HashMap<>();
    private static final HashMap<Class<? extends GameTestPacket>, ResourceLocation> SERVERBOUND_PACKET_TYPES = new HashMap<>();

    public static void initialize() {
        EnvType envType = FabricLoader.getInstance().getEnvironmentType();
        registerServerReceiver(envType, "pulse_state_block", ServerboundSetPulseStatePacket.class, ServerboundSetPulseStatePacket::new);
        registerServerReceiver(envType, "check_state_block", ServerboundSetCheckStatePacket.class, ServerboundSetCheckStatePacket::new);
    }

    //
    // Registering Packets
    //

    private static <T extends GameTestPacket> void registerClientReceiver(EnvType envType, String id, Class<T> type,
                                                                          Supplier<T> packetGen) {
        ResourceLocation resourceId = id(id);
        CLIENTBOUND_PACKET_TYPES.put(type, resourceId);
        if (envType == EnvType.CLIENT) {
            ClientPlayNetworking.registerGlobalReceiver(resourceId, (client, handler, buf, packetSender) -> {
                T packet = packetGen.get();
                packet.read(buf);
                client.execute(() -> packet.handleClient(client, packetSender));
            });
        }
    }

    private static <T extends GameTestPacket> void registerServerReceiver(EnvType envType, String id, Class<T> type,
                                                                          Supplier<T> packetGen) {
        ResourceLocation resourceId = id(id);
        SERVERBOUND_PACKET_TYPES.put(type, resourceId);
        if (envType == EnvType.SERVER) {
            ServerPlayNetworking.registerGlobalReceiver(resourceId, (server, player, listener, buf, packetSender) -> {
                T packet = packetGen.get();
                packet.read(buf);
                server.execute(() -> packet.handleServer(server, player, packetSender));
            });
        }
    }

    //
    // Sending Packets
    //

    @Environment(EnvType.CLIENT)
    public static void sendToServer(GameTestPacket packet) {
        ResourceLocation id = getPacketId(packet, EnvType.CLIENT);
        ClientPlayNetworking.send(id, packet.writeAsBuffer());
    }

    //
    // Validation
    //

    private static ResourceLocation getPacketId(GameTestPacket packet, EnvType envType) {
        ResourceLocation id = (envType == EnvType.SERVER ? CLIENTBOUND_PACKET_TYPES : SERVERBOUND_PACKET_TYPES).get(packet.getClass());
        if (id == null) {
            // Used to create the exception to throw, gets the other list to check if it's there
            ResourceLocation inWrongBounds = (envType != EnvType.SERVER ? CLIENTBOUND_PACKET_TYPES : SERVERBOUND_PACKET_TYPES).get(packet.getClass());
            if (inWrongBounds != null) {
                throw new IllegalArgumentException(
                        (envType == EnvType.SERVER ?
                                "Cannot send C2S packet to clients - " : "Cannot send S2C packet to server - ") +
                                packet.getClass().getSimpleName()
                );
            } else {
                throw new IllegalArgumentException("Invalid packet type!");
            }
        }
        return id;
    }
}
