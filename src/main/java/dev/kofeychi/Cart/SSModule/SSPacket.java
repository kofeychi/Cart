package dev.kofeychi.Cart.SSModule;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record SSPacket(String data,String type) implements CustomPayload {
    public static final CustomPayload.Id<SSPacket> ID = new CustomPayload.Id<>(SSHandler.SSPacketID);
    public static final PacketCodec<RegistryByteBuf, SSPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, SSPacket::data,
            PacketCodecs.STRING, SSPacket::type
            , SSPacket::new);
    // should you need to send more data, add the appropriate record parameters and change your codec:
    // public static final PacketCodec<RegistryByteBuf, BlockHighlightPayload> CODEC = PacketCodec.tuple(
    //         BlockPos.PACKET_CODEC, BlockHighlightPayload::blockPos,
    //         PacketCodecs.INTEGER, BlockHighlightPayload::myInt,
    //         Uuids.PACKET_CODEC, BlockHighlightPayload::myUuid,
    //         BlockHighlightPayload::new
    // );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
