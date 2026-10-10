//? if fabric {
package ivkond.mc.mods.eh.fabric.client;

import ivkond.mc.mods.eh.client.KeyPressedHandler;
import ivkond.mc.mods.eh.client.KeyMappings;
import ivkond.mc.mods.eh.integration.xaero.XaerosMinimapIntegration;
import ivkond.mc.mods.eh.network.HomeCreatedPayload;
import ivkond.mc.mods.eh.network.HomeDeletedPayload;
import ivkond.mc.mods.eh.network.HomeRenamedPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//? if <26.1 {
/*import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
*///? } else {
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
//? }
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class EasyHomesModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //? if <26.1 {
        /*KeyBindingHelper.registerKeyBinding(KeyMappings.TP_TO_DEFAULT_HOME);
        KeyBindingHelper.registerKeyBinding(KeyMappings.SET_NEW_HOME);
        *///? } else {
        KeyMappingHelper.registerKeyMapping(KeyMappings.TP_TO_DEFAULT_HOME);
        KeyMappingHelper.registerKeyMapping(KeyMappings.SET_NEW_HOME);
        //? }

        ClientTickEvents.END_CLIENT_TICK.register(KeyPressedHandler::handle);

        ClientPlayConnectionEvents.INIT.register((handler, client) -> {
            //? if <1.20.5 {
            /*ClientPlayNetworking.registerReceiver(HomeCreatedPayload.ID, (client1, handler1, buf, responseSender) -> {
                HomeCreatedPayload payload = HomeCreatedPayload.read(buf);
                client1.execute(() -> XaerosMinimapIntegration.onHomeCreated(payload.name(), payload.location()));
            });

            ClientPlayNetworking.registerReceiver(HomeDeletedPayload.ID, (client1, handler1, buf, responseSender) -> {
                HomeDeletedPayload payload = HomeDeletedPayload.read(buf);
                client1.execute(() -> XaerosMinimapIntegration.onHomeDeleted(payload.name()));
            });

            ClientPlayNetworking.registerReceiver(HomeRenamedPayload.ID, (client1, handler1, buf, responseSender) -> {
                HomeRenamedPayload payload = HomeRenamedPayload.read(buf);
                client1.execute(() -> XaerosMinimapIntegration.onHomeRenamed(payload.oldName(), payload.newName()));
            });*/
            //? } else {
            ClientPlayNetworking.registerReceiver(HomeCreatedPayload.ID, (payload, ctx) ->
                    client.execute(() -> XaerosMinimapIntegration.onHomeCreated(payload.name(), payload.location()))
            );

            ClientPlayNetworking.registerReceiver(HomeDeletedPayload.ID, (payload, ctx) ->
                    client.execute(() -> XaerosMinimapIntegration.onHomeDeleted(payload.name()))
            );

            ClientPlayNetworking.registerReceiver(HomeRenamedPayload.ID, (payload, ctx) ->
                    client.execute(() -> XaerosMinimapIntegration.onHomeRenamed(payload.oldName(), payload.newName()))
            );
            //? }
        });
    }
}
//? }
