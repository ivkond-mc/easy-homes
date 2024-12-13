package ivkond.mc.mods.eh.fabric.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import ivkond.mc.mods.eh.EasyHomesMod;

@Environment(EnvType.CLIENT)
public class ModMenuConfiguration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return EasyHomesMod::createConfigurationScreen;
    }
}
