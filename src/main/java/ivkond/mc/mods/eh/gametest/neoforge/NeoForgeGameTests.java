//? if neoforge {
/*package ivkond.mc.mods.eh.gametest.neoforge;

import ivkond.mc.mods.eh.EasyHomesMod;
import ivkond.mc.mods.eh.gametest.BackScenarios;
import ivkond.mc.mods.eh.gametest.DelHomeScenarios;
import ivkond.mc.mods.eh.gametest.HomeScenarios;
import ivkond.mc.mods.eh.gametest.HomesScenarios;
import ivkond.mc.mods.eh.gametest.RenHomeScenarios;
import ivkond.mc.mods.eh.gametest.Scenarios;
import ivkond.mc.mods.eh.gametest.SetHomeScenarios;
import ivkond.mc.mods.eh.gametest.Templates;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;

//? if <1.21.9 {
/^@EventBusSubscriber(modid = EasyHomesMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
^///? } else {
@EventBusSubscriber(modid = EasyHomesMod.MOD_ID)
//? }

public final class NeoForgeGameTests {
    private NeoForgeGameTests() {
    }

    //? if <1.21.5 {
    /^@SubscribeEvent
    public static void registerGameTests(RegisterGameTestsEvent event) {
        event.register(SetHomeScenarios.class);
        event.register(HomeScenarios.class);
        event.register(DelHomeScenarios.class);
        event.register(RenHomeScenarios.class);
        event.register(BackScenarios.class);
        event.register(HomesScenarios.class);
    }
    ^///? } else {
    @SubscribeEvent
    public static void registerTestFunctions(net.neoforged.neoforge.registries.RegisterEvent event) {
        if (!event.getRegistryKey().equals(net.minecraft.core.registries.Registries.TEST_FUNCTION)) {
            return;
        }
        for (Scenarios.Entry entry : Scenarios.ALL) {
            event.register(net.minecraft.core.registries.Registries.TEST_FUNCTION,
                    Identifier.parse("easy_homes:" + entry.name()), () -> entry.test());
        }
    }

    @SubscribeEvent
    public static void registerGameTests(RegisterGameTestsEvent event) {
        var environment = event.registerEnvironment(Identifier.parse("easy_homes:default"),
                new net.minecraft.gametest.framework.TestEnvironmentDefinition.AllOf(java.util.List.of()));
        for (Scenarios.Entry entry : Scenarios.ALL) {
            event.registerTest(Identifier.parse("easy_homes:" + entry.name()),
                    new net.minecraft.gametest.framework.FunctionGameTestInstance(
                            net.minecraft.resources.ResourceKey.create(
                                    net.minecraft.core.registries.Registries.TEST_FUNCTION,
                                    Identifier.parse("easy_homes:" + entry.name())),
                            new net.minecraft.gametest.framework.TestData<>(
                                    environment, Identifier.parse(Templates.PLATFORM), entry.maxTicks(), 0, true)));
        }
    }
    //? }
}
*///? }
