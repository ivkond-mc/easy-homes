package ivkond.mc.mods.eh.neoforge;

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
import ivkond.mc.mods.eh.EasyHomesMod;

@Mod(EasyHomesMod.MOD_ID)
public final class EasyHomesModNeoForge {
    public EasyHomesModNeoForge(ModContainer container) {
        EasyHomesMod.init();

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
            EasyHomesMod.onServerStared(event.getServer());
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
}
