package ivkond.mc.mods.eh.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import ivkond.mc.mods.eh.domain.HomeLocation;
import ivkond.mc.mods.eh.domain.PlayerHomes;

import java.util.Map;

public class I18N {
    private I18N() {
    }

    public static Component commandHomeSuccess(String name) {
        return Component.translatable("easy_homes.commands.teleport.success", formatHome(name));
    }

    public static Component commandHomesList(PlayerHomes playerHomes) {
        Map<String, HomeLocation> homes = playerHomes.getAll();

        if (homes == null || homes.isEmpty()) {
            Component command = Component.literal("/sethome <name>").withStyle(ChatFormatting.DARK_AQUA);
            return Component.translatable("easy_homes.commands.list_homes.empty", command);
        }

        MutableComponent response = Component.literal("===== ")
                .append(Component.translatable("easy_homes.commands.list_homes.header"))
                .append(" =====\n");
        homes.forEach((name, home) -> {
            Component coordinatesComponent = Component.literal(home.coordinates()).withStyle(ChatFormatting.DARK_AQUA);

            Component item = Component.literal(" - ")
                    .append(Component.translatable("easy_homes.commands.list_homes.item", formatHome(name), home.dimension(), coordinatesComponent))
                    .append("\n");
            response.append(item);
        });

        return response;
    }

    public static Component commandSetHomeSuccess(String homeName, boolean existingHome) {
        return Component.translatable(
                existingHome ? "easy_homes.commands.set_home.success.existing" : "easy_homes.commands.set_home.success.new",
                formatHome(homeName)
        );
    }

    public static Component commandDelHomeSuccess(String homeName) {
        return Component.translatable("easy_homes.commands.del_home.success", formatHome(homeName));
    }

    public static Component errorHomeNotFound(String name) {
        return Component.translatable("easy_homes.commands.errors.home_not_found", formatHome(name))
                .withStyle(ChatFormatting.RED);
    }

    public static Component errorUnknownLevel(String name) {
        Component nameComponent = Component.literal(name).withStyle(ChatFormatting.DARK_AQUA);
        return Component.translatable("easy_homes.commands.errors.level_not_found", nameComponent)
                .withStyle(ChatFormatting.RED);
    }

    public static Component errorInvalidPosition() {
        return Component.translatable("easy_homes.commands.errors.invalid_position")
                .withStyle(ChatFormatting.RED);
    }

    public static Component formatHome(String homeName) {
        return Component.literal(homeName).withStyle(ChatFormatting.GOLD);
    }
}
