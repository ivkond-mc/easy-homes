package ivkond.mc.mods.eh.fabric;

import ivkond.mc.mods.eh.EasyHomesMod;
import ivkond.mc.mods.eh.fabric.impl.FabricPlatform;
import ivkond.mc.mods.eh.network.HomeCreatedPayload;
import ivkond.mc.mods.eh.network.HomeDeletedPayload;
import ivkond.mc.mods.eh.network.HomeRenamedPayload;
import ivkond.mc.mods.eh.utils.Platform;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public final class EasyHomesModFabric implements ModInitializer {
    private static final Platform PLATFORM = new FabricPlatform();

    @Override
    public void onInitialize() {
        EasyHomesMod.init(PLATFORM);

        ServerLifecycleEvents.SERVER_STARTED.register(EasyHomesMod::onServerStarted);
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

        PayloadTypeRegistry.clientboundPlay().register(HomeCreatedPayload.ID, HomeCreatedPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(HomeDeletedPayload.ID, HomeDeletedPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(HomeRenamedPayload.ID, HomeRenamedPayload.CODEC);
    }
}
