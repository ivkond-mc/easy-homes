package ivkond.mc.mods.eh.utils;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import ivkond.mc.mods.eh.domain.PlayerHomes;
import ivkond.mc.mods.eh.storage.HomeRepository;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("java:S6548")
public class HomeNameSuggestionProvider implements SuggestionProvider<CommandSourceStack> {
    public static final HomeNameSuggestionProvider INSTANCE = new HomeNameSuggestionProvider();

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        String filter = builder.getRemainingLowerCase();

        PlayerHomes homes = HomeRepository.INSTANCE.getHomes(player.getStringUUID());
        if (homes == null) {
            return builder.buildFuture();
        }
        boolean useFilter = filter != null && !filter.isBlank();
        homes.getAllHomes().keySet().forEach(name -> {
            if (!useFilter || name.toLowerCase().startsWith(filter)) {
                builder.suggest(name);
            }
        });

        return builder.buildFuture();
    }
}
