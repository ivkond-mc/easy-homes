package ivkond.mc.mods.eh.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import ivkond.mc.mods.eh.EasyHomesMod;

public final class EasyHomesModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        EasyHomesMod.init();

        ServerLifecycleEvents.SERVER_STARTED.register(EasyHomesMod::onServerStared);
        ServerLifecycleEvents.SERVER_STOPPING.register(event -> EasyHomesMod.onServerStopping());
        ServerPlayConnectionEvents.JOIN.register((packetListener, packetSender, minecraftServer) ->
                EasyHomesMod.onPlayerLoggedIn(packetListener.getPlayer())
        );
        ServerPlayConnectionEvents.DISCONNECT.register((packetListener, minecraftServer) ->
                EasyHomesMod.onPlayerLoggedOut(packetListener.getPlayer())
        );

        CommandRegistrationCallback.EVENT.register(
                (dispatcher, buildContext, commandSelection) ->
                        EasyHomesMod.registerCommands(dispatcher)
        );
    }
}
