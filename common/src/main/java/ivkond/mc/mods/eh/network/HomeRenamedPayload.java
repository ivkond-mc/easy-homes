package ivkond.mc.mods.eh.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record HomeRenamedPayload(String oldName, String newName) implements CustomPacketPayload {
    public static final Type<HomeRenamedPayload> ID = new Type<>(ResourceLocation.parse("easy_homes:home_renamed"));
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
