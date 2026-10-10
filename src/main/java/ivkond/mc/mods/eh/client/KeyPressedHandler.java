package ivkond.mc.mods.eh.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

public class KeyPressedHandler {
    public static void handle(Minecraft minecraft) {
        if (KeyMappings.TP_TO_DEFAULT_HOME.consumeClick()) {
            if (minecraft.player != null && minecraft.player.isShiftKeyDown()) {
                sendCommand(minecraft, "back");
            } else {
                sendCommand(minecraft, "home");
            }
        }

        if (KeyMappings.SET_NEW_HOME.consumeClick()) {
            sendCommand(minecraft, "sethome -");
        }
    }

    private static void sendCommand(Minecraft minecraft, String command) {
        ClientPacketListener connection = minecraft.getConnection();
        if (connection != null) {
            //? if <1.19 {
            /*// 1.18.2: no sendCommand helper, commands travel as chat packets
            connection.send(new net.minecraft.network.protocol.game.ServerboundChatPacket("/" + command));*/
            //? } else if <1.19.4 {
            /*// ClientPacketListener.sendCommand(String) only exists since 1.19.4;
            // 1.19.x needs a raw unsigned ServerboundChatCommandPacket
            connection.send(new net.minecraft.network.protocol.game.ServerboundChatCommandPacket(
                    command,
                    java.time.Instant.now(),
                    0L,
                    net.minecraft.commands.arguments.ArgumentSignatures.EMPTY,
                    false,
                    new net.minecraft.network.chat.LastSeenMessages.Update(
                            new net.minecraft.network.chat.LastSeenMessages(java.util.List.of()),
                            java.util.Optional.empty()
                    )
            ));*/
            //? } else {
            connection.sendCommand(command);
            //? }
        }
    }
}
