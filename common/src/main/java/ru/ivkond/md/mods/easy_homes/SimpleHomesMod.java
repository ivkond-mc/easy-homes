package ru.ivkond.md.mods.easy_homes;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import ru.ivkond.md.mods.easy_homes.commands.DelHomeCommand;
import ru.ivkond.md.mods.easy_homes.commands.HomeCommand;
import ru.ivkond.md.mods.easy_homes.commands.HomesCommand;
import ru.ivkond.md.mods.easy_homes.commands.SetHomeCommand;

public final class SimpleHomesMod {
    public static final String MOD_ID = "easy_homes";

    public static void init() {
        // Write common init code here.
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        HomeCommand.register(dispatcher);
        HomesCommand.register(dispatcher, buildContext);
        SetHomeCommand.register(dispatcher, buildContext);
        DelHomeCommand.register(dispatcher, buildContext);
    }
}
