package ivkond.mc.mods.eh.network;

//? if >=1.19.4 {
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
//? }

//? if <1.19.4 {
/*// Pre-1.19.4 fabric networking: plain channel payloads, no CustomPacketPayload
public final class HomeRenamedPayload {
    public static final net.minecraft.resources.ResourceLocation ID =
            new net.minecraft.resources.ResourceLocation("easy_homes:home_renamed");

    private final String oldName;
    private final String newName;

    public HomeRenamedPayload(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    public String oldName() {
        return oldName;
    }

    public String newName() {
        return newName;
    }

    public static HomeRenamedPayload read(net.minecraft.network.FriendlyByteBuf buf) {
        return new HomeRenamedPayload(
                buf.readUtf(), // oldName
                buf.readUtf() // newName
        );
    }

    public static void write(HomeRenamedPayload payload, net.minecraft.network.FriendlyByteBuf buf) {
        buf.writeUtf(payload.oldName);
        buf.writeUtf(payload.newName);
    }
}*/
//? } else {
public record HomeRenamedPayload(String oldName, String newName) implements CustomPacketPayload {
    public static final Type<HomeRenamedPayload> ID = new Type<>(Identifier.parse("easy_homes:home_renamed"));
    public static final StreamCodec<FriendlyByteBuf, HomeRenamedPayload> CODEC = CustomPacketPayload.codec(HomeRenamedPayload::write, HomeRenamedPayload::new);

    private HomeRenamedPayload(FriendlyByteBuf buf) {
        this(
                buf.readUtf(), // oldName
                buf.readUtf() // newName
        );
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.oldName);
        buf.writeUtf(this.newName);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
//? }
