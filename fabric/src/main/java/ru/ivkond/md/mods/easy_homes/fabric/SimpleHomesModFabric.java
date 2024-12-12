package ru.ivkond.md.mods.easy_homes.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import ru.ivkond.md.mods.easy_homes.SimpleHomesMod;

public final class SimpleHomesModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // Run our common setup.
        SimpleHomesMod.init();

        ServerLifecycleEvents.SERVER_STARTED.register(SimpleHomesMod::onServerStared);

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, buildContext, commandSelection) ->
                        SimpleHomesMod.registerCommands(dispatcher)
        );
    }
}
