//? if fabric {
package ivkond.mc.mods.eh.fabric;

import ivkond.mc.mods.eh.EasyHomesMod;
import ivkond.mc.mods.eh.fabric.impl.FabricPlatform;
import ivkond.mc.mods.eh.network.HomeCreatedPayload;
import ivkond.mc.mods.eh.network.HomeDeletedPayload;
import ivkond.mc.mods.eh.network.HomeRenamedPayload;
import ivkond.mc.mods.eh.utils.Platform;
import net.fabricmc.api.ModInitializer;
//? if <1.19.2 {
/*import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
*///? } else {
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
//? }
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
//? if >=1.20.5 {
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
//? }
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

        //? if <1.19.2 {
        /*CommandRegistrationCallback.EVENT.register(
                (dispatcher, dedicated) -> EasyHomesMod.registerCommands(dispatcher)
        );*/
        //? } else {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, buildContext, commandSelection) ->
                        EasyHomesMod.registerCommands(dispatcher)
        );
        //? }

        //? if <1.20.5 {
        /*// Pre-1.20.5 fabric networking: S2C channels need no server-side
        // registration, the client registers receivers for the raw channels
        *///? } else {
        //? if <26.1 {
        /*PayloadTypeRegistry.playS2C().register(HomeCreatedPayload.ID, HomeCreatedPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(HomeDeletedPayload.ID, HomeDeletedPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(HomeRenamedPayload.ID, HomeRenamedPayload.CODEC);
        *///? } else {
        PayloadTypeRegistry.clientboundPlay().register(HomeCreatedPayload.ID, HomeCreatedPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(HomeDeletedPayload.ID, HomeDeletedPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(HomeRenamedPayload.ID, HomeRenamedPayload.CODEC);
        //? }
        //? }
    }
}
//? }
