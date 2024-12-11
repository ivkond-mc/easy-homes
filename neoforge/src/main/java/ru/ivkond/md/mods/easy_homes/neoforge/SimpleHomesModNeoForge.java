package ru.ivkond.md.mods.easy_homes.neoforge;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import ru.ivkond.md.mods.easy_homes.SimpleHomesMod;
import ru.ivkond.md.mods.easy_homes.neoforge.init.SimpleHomesForgeConfiguration;

@Mod(SimpleHomesMod.MOD_ID)
public final class SimpleHomesModNeoForge {
    public SimpleHomesModNeoForge(ModContainer container) {
        // Run our common setup.
        SimpleHomesMod.init();

        container.registerConfig(ModConfig.Type.SERVER, SimpleHomesForgeConfiguration.CONFIG_SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @EventBusSubscriber
    public static class EventListener {

        @SubscribeEvent
        public static void registerCommands(RegisterCommandsEvent event) {
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
            CommandBuildContext buildContext = event.getBuildContext();
            SimpleHomesMod.registerCommands(dispatcher, buildContext);
        }
    }
}
