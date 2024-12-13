package ivkond.mc.mods.eh.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import ivkond.mc.mods.eh.EasyHomesMod;
import ivkond.mc.mods.eh.client.KeyMappings;

public final class EasyHomesModFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(KeyMappings.TP_TO_DEFAULT_HOME);

        ClientTickEvents.END_CLIENT_TICK.register(EasyHomesMod::onClientTick);
    }
}
