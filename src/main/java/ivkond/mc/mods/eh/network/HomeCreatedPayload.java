package ivkond.mc.mods.eh.network;

import ivkond.mc.mods.eh.domain.HomeLocation;
//? if >=1.20.5 {
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
//? }

//? if <1.20.5 {
/*// Pre-1.20.5 fabric networking: plain channel payloads, no CustomPacketPayload (1.18.2-1.20.4)
public final class HomeCreatedPayload {
    public static final net.minecraft.resources.ResourceLocation ID =
            new net.minecraft.resources.ResourceLocation("easy_homes:home_created");

    private final String name;
    private final HomeLocation location;

    public HomeCreatedPayload(String name, HomeLocation location) {
        this.name = name;
        this.location = location;
    }

    public HomeCreatedPayload(String name, String dimension, double x, double y, double z, float rotX, float rotY) {
        this(name, new HomeLocation(dimension, x, y, z, rotX, rotY));
    }

    public String name() {
        return name;
    }

    public HomeLocation location() {
        return location;
    }

    public static HomeCreatedPayload read(net.minecraft.network.FriendlyByteBuf buf) {
        return new HomeCreatedPayload(
                buf.readUtf(), // name
                buf.readUtf(), // dimension
                buf.readDouble(), // x
                buf.readDouble(), // y
                buf.readDouble(), // z
                buf.readFloat(), // rotX
                buf.readFloat() // rotY
        );
    }

    public static void write(HomeCreatedPayload payload, net.minecraft.network.FriendlyByteBuf buf) {
        buf.writeUtf(payload.name);
        buf.writeUtf(payload.location.dimension());
        buf.writeDouble(payload.location.x());
        buf.writeDouble(payload.location.y());
        buf.writeDouble(payload.location.z());
        buf.writeFloat(payload.location.rotX());
        buf.writeFloat(payload.location.rotY());
    }
}*/
//? } else {
public record HomeCreatedPayload(String name, HomeLocation location) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<HomeCreatedPayload> ID = new CustomPacketPayload.Type<>(Identifier.parse("easy_homes:home_created"));
    public static final StreamCodec<FriendlyByteBuf, HomeCreatedPayload> CODEC = CustomPacketPayload.codec(HomeCreatedPayload::write, HomeCreatedPayload::new);

    private HomeCreatedPayload(FriendlyByteBuf buf) {
        this(
                buf.readUtf(), // name
                buf.readUtf(), // dimension
                buf.readDouble(), // x
                buf.readDouble(), // y
                buf.readDouble(), // z
                buf.readFloat(), // rotX
                buf.readFloat() // rotY
        );
    }

    public HomeCreatedPayload(String name, String dimension, double x, double y, double z, float rotX, float rotY) {
        this(name, new HomeLocation(dimension, x, y, z, rotX, rotY));
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.name);
        buf.writeUtf(this.location.dimension());
        buf.writeDouble(this.location.x());
        buf.writeDouble(this.location.y());
        buf.writeDouble(this.location.z());
        buf.writeFloat(this.location.rotX());
        buf.writeFloat(this.location.rotY());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
//? }
