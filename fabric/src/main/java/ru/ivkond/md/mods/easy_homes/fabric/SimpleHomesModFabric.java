package ru.ivkond.md.mods.easy_homes.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import ru.ivkond.md.mods.easy_homes.SimpleHomesMod;

public final class SimpleHomesModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SimpleHomesMod.init();

        ServerLifecycleEvents.SERVER_STARTED.register(SimpleHomesMod::onServerStared);
        ServerLifecycleEvents.SERVER_STOPPING.register(event -> SimpleHomesMod.onServerStopping());
        ServerPlayConnectionEvents.JOIN.register((packetListener, packetSender, minecraftServer) ->
                SimpleHomesMod.onPlayerLoggedIn(packetListener.getPlayer())
        );
        ServerPlayConnectionEvents.DISCONNECT.register((packetListener, minecraftServer) ->
                SimpleHomesMod.onPlayerLoggedOut(packetListener.getPlayer())
        );

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, buildContext, commandSelection) ->
                        SimpleHomesMod.registerCommands(dispatcher)
        );
    }
}
