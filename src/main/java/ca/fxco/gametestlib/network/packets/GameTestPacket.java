package ca.fxco.gametestlib.network.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

// GameTest Packet
public interface GameTestPacket extends CustomPacketPayload {

    /**
     * Called on the render thread!
     */
    default void handleClient(Minecraft client, PacketSender packetSender) {}

    /**
     * Called on the server thread!
     */
    default void handleServer(MinecraftServer server, ServerPlayer fromPlayer, PacketSender packetSender) {}

}
