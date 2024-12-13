package ru.ivkond.md.mods.easy_homes.neoforge;

import com.mojang.brigadier.CommandDispatcher;
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
import ru.ivkond.md.mods.easy_homes.SimpleHomesMod;

@Mod(SimpleHomesMod.MOD_ID)
public final class SimpleHomesModNeoForge {
    public SimpleHomesModNeoForge(ModContainer container) {
        // Run our common setup.
        SimpleHomesMod.init();

        if (FMLEnvironment.dist.isClient()) {
            initConfigurationScreen(container);
        }
    }

    private static void initConfigurationScreen(ModContainer container) {
        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (c, parent) -> SimpleHomesMod.createConfigurationScreen(parent)
        );
    }

    @EventBusSubscriber(modid = SimpleHomesMod.MOD_ID)
    public static class CommonEventBusSubscriber {
        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
            SimpleHomesMod.registerCommands(dispatcher);
        }

        @SubscribeEvent
        public static void onServerStarted(ServerStartedEvent event) {
            SimpleHomesMod.onServerStared(event.getServer());
        }

        @SubscribeEvent
        public static void onServerStopping(ServerStoppingEvent event) {
            SimpleHomesMod.onServerStopping();
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            Player player = event.getEntity();
            if (player instanceof ServerPlayer serverPlayer) {
                SimpleHomesMod.onPlayerLoggedIn(serverPlayer);
            }
        }

        @SubscribeEvent
        public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
            Player player = event.getEntity();
            if (player instanceof ServerPlayer serverPlayer) {
                SimpleHomesMod.onPlayerLoggedOut(serverPlayer);
            }
        }
    }
}
