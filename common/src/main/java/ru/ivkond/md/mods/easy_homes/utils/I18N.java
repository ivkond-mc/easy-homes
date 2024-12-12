package ru.ivkond.md.mods.easy_homes.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import ru.ivkond.md.mods.easy_homes.domain.PlayerHome;

import java.util.List;

public class I18N {
    private I18N() {
    }

    public static Component commandHomeSuccess() {
        return Component.translatable("easy_homes.commands.teleport.success");
    }

    public static Component commandHomesList(List<PlayerHome> homes) {
        if (homes == null || homes.isEmpty()) {
            return Component.translatable("easy_homes.commands.list_homes.empty");
        }

        MutableComponent response = Component.translatable("easy_homes.commands.list_homes.header");
        for (int i = 0; i < homes.size(); i++) {
            PlayerHome home = homes.get(i);
            Component description = Component.translatable("easy_homes.commands.list_homes.header", i, home.name(), home.dimension(), home.coordinates());
            response.append(description);
        }

        return response;
    }

    public static Component commandSetHomeSuccess() {
        return Component.translatable("easy_homes.commands.set_home.success");
    }

    public static Component commandDelHomeSuccess() {
        return Component.translatable("easy_homes.commands.del_home.success");
    }
}
