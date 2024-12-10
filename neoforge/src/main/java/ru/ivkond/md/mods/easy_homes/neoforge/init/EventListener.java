package ru.ivkond.md.mods.easy_homes.neoforge.init;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import ru.ivkond.md.mods.easy_homes.SimpleHomesMod;

@EventBusSubscriber
public class EventListener {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        CommandBuildContext buildContext = event.getBuildContext();
        SimpleHomesMod.registerCommands(dispatcher, buildContext);
    }
}
