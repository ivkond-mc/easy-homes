package ru.ivkond.md.mods.easy_homes.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import ru.ivkond.md.mods.easy_homes.SimpleHomesMod;
import ru.ivkond.md.mods.easy_homes.client.KeyMappings;

public final class SimpleHomesModFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(KeyMappings.TP_TO_DEFAULT_HOME);

        ClientTickEvents.END_CLIENT_TICK.register(SimpleHomesMod::onClientTick);
    }
}
