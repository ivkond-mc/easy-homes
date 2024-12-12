package ru.ivkond.md.mods.easy_homes.fabric.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ru.ivkond.md.mods.easy_homes.SimpleHomesMod;

@Environment(EnvType.CLIENT)
public class ModMenuConfiguration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SimpleHomesMod::createConfigurationScreen;
    }
}
