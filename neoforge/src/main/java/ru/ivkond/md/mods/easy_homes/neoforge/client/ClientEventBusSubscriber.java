package ru.ivkond.md.mods.easy_homes.neoforge.client;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import ru.ivkond.md.mods.easy_homes.SimpleHomesMod;
import ru.ivkond.md.mods.easy_homes.client.KeyMappings;

public class ClientEventBusSubscriber {
    @EventBusSubscriber(modid = SimpleHomesMod.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
    public static class ForgeEventSubscriber {
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            Minecraft instance = Minecraft.getInstance();
            SimpleHomesMod.onClientTick(instance);
        }
    }

    @EventBusSubscriber(modid = SimpleHomesMod.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ModEventSubscriber {
        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(KeyMappings.TP_TO_DEFAULT_HOME);
        }
    }
}
