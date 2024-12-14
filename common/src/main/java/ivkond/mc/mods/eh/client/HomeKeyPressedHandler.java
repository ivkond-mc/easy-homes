package ivkond.mc.mods.eh.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

public class HomeKeyPressedHandler {
    public static void handle(Minecraft minecraft) {
        if (KeyMappings.TP_TO_DEFAULT_HOME.consumeClick()) {
            sendCommand(minecraft, "home");
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
