package ru.ivkond.md.mods.easy_homes.fabric;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import ru.ivkond.md.mods.easy_homes.SimpleHomesMod;

public final class SimpleHomesModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        SimpleHomesMod.init();

        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, commandSelection) -> {
            SimpleHomesMod.registerCommands(dispatcher, buildContext);
        });
    }
}
