package ivkond.mc.mods.eh.neoforge.impl;

import ivkond.mc.mods.eh.utils.Platform;
import net.neoforged.fml.ModList;

public class NeoForgePlatform implements Platform {
    @Override
    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
