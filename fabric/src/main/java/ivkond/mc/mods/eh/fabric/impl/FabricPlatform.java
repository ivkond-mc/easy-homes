package ivkond.mc.mods.eh.fabric.impl;

import ivkond.mc.mods.eh.utils.Platform;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatform implements Platform {
    @Override
    public boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }
}
