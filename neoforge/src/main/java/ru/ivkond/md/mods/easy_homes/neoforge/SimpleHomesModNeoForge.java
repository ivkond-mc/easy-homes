package ru.ivkond.md.mods.easy_homes.neoforge;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
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
}
