package ivkond.mc.mods.eh.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class OverlayMessages {
    private OverlayMessages() {
    }

    public static void send(ServerPlayer player, Component message) {
        //? if >=26.1 {
        player.sendOverlayMessage(message);
        //? } else {
        /*player.displayClientMessage(message, true);
        *///? }
    }
}
