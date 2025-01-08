package ivkond.mc.mods.eh.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record HomeDeletedPayload(String name) implements CustomPacketPayload {
    public static final Type<HomeDeletedPayload> ID = new Type<>(ResourceLocation.parse("easy_homes:home_deleted"));
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
