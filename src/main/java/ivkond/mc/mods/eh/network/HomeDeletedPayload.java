package ivkond.mc.mods.eh.network;

//? if >=1.20.5 {
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
//? }

//? if <1.20.5 {
/*// Pre-1.20.5 fabric networking: plain channel payloads, no CustomPacketPayload (1.18.2-1.20.4)
public final class HomeDeletedPayload {
    public static final net.minecraft.resources.ResourceLocation ID =
            new net.minecraft.resources.ResourceLocation("easy_homes:home_deleted");

    private final String name;

    public HomeDeletedPayload(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public static HomeDeletedPayload read(net.minecraft.network.FriendlyByteBuf buf) {
        return new HomeDeletedPayload(buf.readUtf());
    }

    public static void write(HomeDeletedPayload payload, net.minecraft.network.FriendlyByteBuf buf) {
        buf.writeUtf(payload.name);
    }
}*/
//? } else {
public record HomeDeletedPayload(String name) implements CustomPacketPayload {
    public static final Type<HomeDeletedPayload> ID = new Type<>(Identifier.parse("easy_homes:home_deleted"));
    public static final StreamCodec<FriendlyByteBuf, HomeDeletedPayload> CODEC = CustomPacketPayload.codec(HomeDeletedPayload::write, HomeDeletedPayload::new);

    private HomeDeletedPayload(FriendlyByteBuf buf) {
        this(buf.readUtf());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.name);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
//? }
