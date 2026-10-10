package ivkond.mc.mods.eh.gametest;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

import static ivkond.mc.mods.eh.gametest.Eh.assertCommand;
import static ivkond.mc.mods.eh.gametest.Eh.fail;
import static ivkond.mc.mods.eh.gametest.Eh.place;
import static ivkond.mc.mods.eh.gametest.Eh.player;
import static ivkond.mc.mods.eh.gametest.Eh.reset;

/**
 * {@code /homes} scenarios (docs/gametest-scenarios.md, section 4.6). The
 * command only renders a chat message, so both tests assert the exit code and
 * the absence of crashes.
 */
//? if neoforge && <1.21.5 {
/*@net.neoforged.neoforge.gametest.GameTestHolder("easy_homes")
@net.neoforged.neoforge.gametest.PrefixGameTestTemplate(false)*/
//? }
public final class HomesScenarios
        // The 1.19.4-era fabric gametest api only discovers classes implementing FabricGameTest
        //? if fabric && =1.19.4 {
        /*implements net.fabricmc.fabric.api.gametest.v1.FabricGameTest*/
        //? }
        {

    /** LST-1: listing several homes succeeds. */
    //? if <1.21.5 && fabric {
    /*@net.minecraft.gametest.framework.GameTest(template = Templates.PLATFORM, timeoutTicks = 100)*/
    //? }
    //? if <1.21.5 && neoforge {
    /*@net.minecraft.gametest.framework.GameTest(template = "platform", timeoutTicks = 100)*/
    //? }
    //? if fabric && >=1.21.5 {
    @net.fabricmc.fabric.api.gametest.v1.GameTest(structure = Templates.PLATFORM, maxTicks = 100)
    //? }
    public
    //? if fabric && >=1.21.5 {
    //? } else {
    /*static*/
    //? }
    void listHomes(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.SURVIVAL, "homes_list");
        reset(player, 5, 5);
        place(helper, player, 1, 1, 1, 0, 0);
        assertCommand(helper, player, "sethome a", 1);
        assertCommand(helper, player, "sethome b", 1);

        assertCommand(helper, player, "homes", 1);
        helper.succeed();
    }

    /** LST-2: listing with no homes takes the empty branch without crashing. */
    //? if <1.21.5 && fabric {
    /*@net.minecraft.gametest.framework.GameTest(template = Templates.PLATFORM, timeoutTicks = 100)*/
    //? }
    //? if <1.21.5 && neoforge {
    /*@net.minecraft.gametest.framework.GameTest(template = "platform", timeoutTicks = 100)*/
    //? }
    //? if fabric && >=1.21.5 {
    @net.fabricmc.fabric.api.gametest.v1.GameTest(structure = Templates.PLATFORM, maxTicks = 100)
    //? }
    public
    //? if fabric && >=1.21.5 {
    //? } else {
    /*static*/
    //? }
    void listEmptyHomes(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.SURVIVAL, "homes_empty");
        reset(player, 5, 5);
        assertCommand(helper, player, "homes", 1);
        helper.succeed();
    }
}
