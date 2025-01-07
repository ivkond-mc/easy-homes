package ivkond.mc.mods.eh.fabric.client;

import ivkond.mc.mods.eh.EasyHomesMod;
import ivkond.mc.mods.eh.client.KeyMappings;
import ivkond.mc.mods.eh.integration.xaero.XaerosMinimapIntegration;
import ivkond.mc.mods.eh.network.HomeCreatedPayload;
import ivkond.mc.mods.eh.network.HomeDeletedPayload;
import ivkond.mc.mods.eh.network.HomeRenamedPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class EasyHomesModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(KeyMappings.TP_TO_DEFAULT_HOME);
        KeyBindingHelper.registerKeyBinding(KeyMappings.SET_NEW_HOME);

        ClientTickEvents.END_CLIENT_TICK.register(EasyHomesMod::onClientTick);

        ClientPlayConnectionEvents.INIT.register((handler, client) -> {
            ClientPlayNetworking.registerReceiver(HomeCreatedPayload.ID, (payload, ctx) ->
                    client.execute(() -> XaerosMinimapIntegration.onHomeCreated(payload.name(), payload.location()))
            );

            ClientPlayNetworking.registerReceiver(HomeDeletedPayload.ID, (payload, ctx) ->
                    client.execute(() -> XaerosMinimapIntegration.onHomeDeleted(payload.name()))
            );

            ClientPlayNetworking.registerReceiver(HomeRenamedPayload.ID, (payload, ctx) ->
                    client.execute(() -> XaerosMinimapIntegration.onHomeRenamed(payload.oldName(), payload.newName()))
            );
        });
    }
}
