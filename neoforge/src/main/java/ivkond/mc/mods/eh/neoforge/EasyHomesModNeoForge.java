package ivkond.mc.mods.eh.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import ivkond.mc.mods.eh.EasyHomesMod;
import ivkond.mc.mods.eh.integration.xaero.XaerosMinimapIntegration;
import ivkond.mc.mods.eh.neoforge.impl.NeoForgePlatform;
import ivkond.mc.mods.eh.network.HomeCreatedPayload;
import ivkond.mc.mods.eh.network.HomeDeletedPayload;
import ivkond.mc.mods.eh.network.HomeRenamedPayload;
import ivkond.mc.mods.eh.utils.Platform;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(EasyHomesMod.MOD_ID)
public final class EasyHomesModNeoForge {
    private static final Platform PLATFORM = new NeoForgePlatform();

    public EasyHomesModNeoForge(ModContainer container) {
        EasyHomesMod.init(PLATFORM);

        if (FMLEnvironment.dist.isClient()) {
            initConfigurationScreen(container);
        }
    }

    private static void initConfigurationScreen(ModContainer container) {
        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (c, parent) -> EasyHomesMod.createConfigurationScreen(parent)
        );
    }

    @EventBusSubscriber(modid = EasyHomesMod.MOD_ID)
    public static class CommonEventBusSubscriber {
        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
            EasyHomesMod.registerCommands(dispatcher);
        }

        @SubscribeEvent
        public static void onServerStarted(ServerStartedEvent event) {
            EasyHomesMod.onServerStarted(event.getServer());
        }

        @SubscribeEvent
        public static void onServerStopping(ServerStoppingEvent event) {
            EasyHomesMod.onServerStopping();
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            Player player = event.getEntity();
            if (player instanceof ServerPlayer serverPlayer) {
                EasyHomesMod.onPlayerLoggedIn(serverPlayer);
            }
        }

        @SubscribeEvent
        public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
            Player player = event.getEntity();
            if (player instanceof ServerPlayer serverPlayer) {
                EasyHomesMod.onPlayerLoggedOut(serverPlayer);
            }
        }
    }

    @EventBusSubscriber(modid = EasyHomesMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class ModEventBusSubscriber {
        @SubscribeEvent
        public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
            PayloadRegistrar registrar = event.registrar("1");

            registrar.playToClient(HomeCreatedPayload.ID, HomeCreatedPayload.CODEC, (payload, context) ->
                    context.enqueueWork(() -> XaerosMinimapIntegration.onHomeCreated(payload.name(), payload.location())));
            registrar.playToClient(HomeDeletedPayload.ID, HomeDeletedPayload.CODEC, (payload, context) ->
                    context.enqueueWork(() -> XaerosMinimapIntegration.onHomeDeleted(payload.name())));
            registrar.playToClient(HomeRenamedPayload.ID, HomeRenamedPayload.CODEC, (payload, context) ->
                    context.enqueueWork(() -> XaerosMinimapIntegration.onHomeRenamed(payload.oldName(), payload.newName())));
        }
    }
}