package ivkond.mc.mods.eh.network;

import ivkond.mc.mods.eh.domain.HomeLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record HomeCreatedPayload(String name, HomeLocation location) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<HomeCreatedPayload> ID = new CustomPacketPayload.Type<>(ResourceLocation.parse("easy_homes:home_created"));
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
