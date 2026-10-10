package ivkond.mc.mods.eh.gametest;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

import static ivkond.mc.mods.eh.gametest.Eh.assertCommand;
import static ivkond.mc.mods.eh.gametest.Eh.fail;
import static ivkond.mc.mods.eh.gametest.Eh.place;
import static ivkond.mc.mods.eh.gametest.Eh.player;
import static ivkond.mc.mods.eh.gametest.Eh.repo;
import static ivkond.mc.mods.eh.gametest.Eh.reset;

/**
 * {@code /delhome} scenarios (docs/gametest-scenarios.md, section 4.3).
 */
//? if neoforge && <1.21.5 {
/*@net.neoforged.neoforge.gametest.GameTestHolder("easy_homes")
@net.neoforged.neoforge.gametest.PrefixGameTestTemplate(false)*/
//? }
public final class DelHomeScenarios {

    /** DEL-1: deletes an existing home from memory and from disk. */
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
    void deleteExistingHome(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.SURVIVAL, "delhome_existing");
        reset(player, 5, 5);
        place(helper, player, 1, 1, 1, 0, 0);
        assertCommand(helper, player, "sethome base", 1);

        assertCommand(helper, player, "delhome base", 1);

        if (repo().findHome(player.getStringUUID(), "base") != null) {
            fail(helper, "Home was not removed from the repository");
        }
        repo().forgetConfig(player.getStringUUID());
        repo().loadConfig(player.getStringUUID());
        if (repo().findHome(player.getStringUUID(), "base") != null) {
            fail(helper, "Home was not removed from disk");
        }
        helper.succeed();
    }

    /** DEL-2: deleting a non-existent home fails. */
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
    void deleteUnknownHome(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.SURVIVAL, "delhome_unknown");
        reset(player, 5, 5);
        assertCommand(helper, player, "delhome nothing", 0);
        helper.succeed();
    }

    /** DEL-3: deleting with an invalid name fails. */
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
    void deleteInvalidName(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.SURVIVAL, "delhome_invalid");
        reset(player, 5, 5);
        assertCommand(helper, player, "delhome foo bar", 0);
        helper.succeed();
    }
}
