package ivkond.mc.mods.eh.neoforge.client;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import ivkond.mc.mods.eh.EasyHomesMod;
import ivkond.mc.mods.eh.client.KeyMappings;
import net.neoforged.neoforge.event.TickEvent;

public class ClientEventBusSubscriber {
    @Mod.EventBusSubscriber(modid = EasyHomesMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEventSubscriber {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            Minecraft instance = Minecraft.getInstance();
            if (event.phase == TickEvent.Phase.END) {
                EasyHomesMod.onClientTick(instance);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = EasyHomesMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventSubscriber {
        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(KeyMappings.TP_TO_DEFAULT_HOME);
            event.register(KeyMappings.SET_NEW_HOME);
        }
    }
}
