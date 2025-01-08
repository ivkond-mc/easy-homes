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
            connection.sendCommand(command);
        }
    }
}
