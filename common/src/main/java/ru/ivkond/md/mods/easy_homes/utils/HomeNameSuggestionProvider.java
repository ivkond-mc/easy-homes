package ru.ivkond.md.mods.easy_homes.utils;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import ru.ivkond.md.mods.easy_homes.domain.PlayerHomes;
import ru.ivkond.md.mods.easy_homes.storage.HomeRepository;

import java.util.concurrent.CompletableFuture;

public class HomeNameSuggestionProvider implements SuggestionProvider<CommandSourceStack> {
    public static HomeNameSuggestionProvider INSTANCE = new HomeNameSuggestionProvider();

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String filter = builder.getRemainingLowerCase();

        PlayerHomes homes = HomeRepository.INSTANCE.getHomes(player.getStringUUID());
        boolean useFilter = filter != null && !filter.isBlank();
        homes.getAll().keySet().forEach(name -> {
            if (!useFilter || name.toLowerCase().startsWith(filter)) {
                builder.suggest(name);
            }
        });

        return builder.buildFuture();
    }
}
