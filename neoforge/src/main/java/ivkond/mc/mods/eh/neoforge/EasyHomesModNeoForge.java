package ivkond.mc.mods.eh.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import ivkond.mc.mods.eh.EasyHomesMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@Mod(EasyHomesMod.MOD_ID)
public final class EasyHomesModNeoForge {
    public EasyHomesModNeoForge() {
        EasyHomesMod.init();
    }

    @Mod.EventBusSubscriber(modid = EasyHomesMod.MOD_ID)
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
