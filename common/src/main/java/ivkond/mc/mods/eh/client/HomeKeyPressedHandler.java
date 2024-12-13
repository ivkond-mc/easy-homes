package ivkond.mc.mods.eh.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

public class HomeKeyPressedHandler {
    public static void handle(Minecraft minecraft) {
        if (KeyMappings.TP_TO_DEFAULT_HOME.consumeClick()) {
            ClientPacketListener connection = minecraft.getConnection();
            if (connection != null) {
                connection.sendCommand("home");
            }
        }
    }
}
