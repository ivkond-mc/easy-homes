package ru.ivkond.md.mods.easy_homes.fabric.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuConfiguration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        // TODO: Init screen
        return ModMenuApi.super.getModConfigScreenFactory();
    }
}
