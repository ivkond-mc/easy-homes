package ivkond.mc.mods.eh.network;

//? if fabric && <1.20.5 {
/*import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import io.netty.buffer.Unpooled;

// Pre-1.20.5 fabric networking: S2C channels are raw ResourceLocation + FriendlyByteBuf
public class PacketSender {
    public static void send(ServerPlayer player, HomeCreatedPayload payload) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        HomeCreatedPayload.write(payload, buf);
        ServerPlayNetworking.send(player, HomeCreatedPayload.ID, buf);
    }

    public static void send(ServerPlayer player, HomeDeletedPayload payload) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        HomeDeletedPayload.write(payload, buf);
        ServerPlayNetworking.send(player, HomeDeletedPayload.ID, buf);
    }

    public static void send(ServerPlayer player, HomeRenamedPayload payload) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        HomeRenamedPayload.write(payload, buf);
        ServerPlayNetworking.send(player, HomeRenamedPayload.ID, buf);
    }
}*/
//? } else {
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class PacketSender {
    public static void send(ServerPlayer player, CustomPacketPayload payload) {
        player.connection.send(new ClientboundCustomPayloadPacket(payload));
    }
}
//? }
