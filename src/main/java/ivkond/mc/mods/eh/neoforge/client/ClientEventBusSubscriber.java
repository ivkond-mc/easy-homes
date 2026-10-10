//? if neoforge {
/*package ivkond.mc.mods.eh.neoforge.client;

import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import ivkond.mc.mods.eh.EasyHomesMod;
import ivkond.mc.mods.eh.client.KeyPressedHandler;
import ivkond.mc.mods.eh.client.KeyMappings;

public class ClientEventBusSubscriber {

    // Registers the MidnightConfig configuration screen. Lives in this
    // client-only class because FML 9 (NeoForge 21.6-21.8) eagerly verifies
    // the mod class on the dedicated server and rejects the Screen-typed
    // config screen factory lambda in the main mod class.
    public static void registerConfigScreen(ModContainer container) {
        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (c, parent) -> MidnightConfig.getScreen(parent, EasyHomesMod.MOD_ID)
        );
    }
    //? if <1.21.6 {
    /^@EventBusSubscriber(modid = EasyHomesMod.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
    ^///? } else {
    @EventBusSubscriber(modid = EasyHomesMod.MOD_ID, value = Dist.CLIENT)
    //? }
    public static class ForgeEventSubscriber {
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            Minecraft instance = Minecraft.getInstance();
            KeyPressedHandler.handle(instance);
        }
    }

    //? if <1.21.6 {
    /^@EventBusSubscriber(modid = EasyHomesMod.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    ^///? } else {
    @EventBusSubscriber(modid = EasyHomesMod.MOD_ID, value = Dist.CLIENT)
    //? }
    public static class ModEventSubscriber {
        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(KeyMappings.TP_TO_DEFAULT_HOME);
            event.register(KeyMappings.SET_NEW_HOME);
        }
    }
}
*///? }
