package ivkond.mc.mods.eh.utils;

import ivkond.mc.mods.eh.domain.HomeLocation;
import ivkond.mc.mods.eh.domain.PlayerHomes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
//? if <1.19 {
/*import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
*///? }

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

public class I18N {
    private I18N() {
    }

    // Component.literal/translatable static factories only exist since 1.19
    //? if <1.19 {
    /*private static MutableComponent text(String content) {
        return new TextComponent(content);
    }

    private static MutableComponent tr(String key, Object... args) {
        return new TranslatableComponent(key, args);
    }*/
    //? } else {
    private static MutableComponent text(String content) {
        return Component.literal(content);
    }

    private static MutableComponent tr(String key, Object... args) {
        return Component.translatable(key, args);
    }
    //? }

    public static Component commandHomeSuccess(String name) {
        return tr("easy_homes.commands.home.success", formatHome(name));
    }

    public static Component commandHomeLocked(Duration lockDuration) {
        String expiration = Long.toString(lockDuration.getSeconds());
        Component expirationComponent = text(expiration).withStyle(ChatFormatting.GOLD);
        return tr("easy_homes.commands.home.too_fast", expirationComponent)
                .withStyle(ChatFormatting.RED);
    }

    public static Component commandHomesList(PlayerHomes playerHomes) {
        Map<String, HomeLocation> homes = playerHomes.getAllHomes();

        if (homes == null || homes.isEmpty()) {
            Component command = text("/sethome <name>").withStyle(ChatFormatting.DARK_AQUA);
            return tr("easy_homes.commands.list_homes.empty", command);
        }

        String lastVisitedHome = Objects.requireNonNullElse(playerHomes.getLastVisitedHome(), "n/a");
        MutableComponent response = text("===== ")
                .append(tr("easy_homes.commands.list_homes.header"))
                .append(" =====\n")
                .append(tr("easy_homes.commands.list_homes.last_visited", formatHome(lastVisitedHome)))
                .append("\n");
        homes.forEach((name, home) -> {
            Component coordinatesComponent = text(home.coordinates()).withStyle(ChatFormatting.DARK_AQUA);

            Component item = text(" - ")
                    .append(tr("easy_homes.commands.list_homes.item", formatHome(name), home.dimension(), coordinatesComponent))
                    .append("\n");
            response.append(item);
        });

        return response;
    }

    public static Component commandSetHomeSuccess(String homeName, boolean existingHome) {
        return tr(
                existingHome ? "easy_homes.commands.set_home.success.existing" : "easy_homes.commands.set_home.success.new",
                formatHome(homeName)
        );
    }

    public static Component commandSetHomeMaxHomesReached() {
        return tr("easy_homes.commands.set_home.max_homes_reached")
                .withStyle(ChatFormatting.RED);
    }

    public static Component commandRenHomeSuccess(String oldName, String newName) {
        return tr("easy_homes.commands.ren_home.success", formatHome(oldName), formatHome(newName));
    }

    public static Component commandDelHomeSuccess(String homeName) {
        return tr("easy_homes.commands.del_home.success", formatHome(homeName));
    }

    public static Component errorHomeNotFound(String name) {
        return tr("easy_homes.commands.errors.home_not_found", formatHome(name))
                .withStyle(ChatFormatting.RED);
    }

    public static Component errorUnknownLevel(String name) {
        Component nameComponent = text(name).withStyle(ChatFormatting.DARK_AQUA);
        return tr("easy_homes.commands.errors.level_not_found", nameComponent)
                .withStyle(ChatFormatting.RED);
    }

    public static Component errorInvalidPosition() {
        return tr("easy_homes.commands.errors.invalid_position")
                .withStyle(ChatFormatting.RED);
    }

    public static Component errorInvalidHomeName(String homeName) {
        return tr("easy_homes.commands.errors.invalid_home_name", formatHome(homeName))
                .withStyle(ChatFormatting.RED);
    }

    public static Component errorNoLastVisitedHome() {
        return tr("easy_homes.commands.back.no_last_visited")
                .withStyle(ChatFormatting.RED);
    }

    public static Component errorPlayerMounted() {
        return tr("easy_homes.commands.home.player_mounted")
                .withStyle(ChatFormatting.RED);
    }

    public static Component formatHome(String homeName) {
        return text(homeName).withStyle(ChatFormatting.GOLD);
    }
}
