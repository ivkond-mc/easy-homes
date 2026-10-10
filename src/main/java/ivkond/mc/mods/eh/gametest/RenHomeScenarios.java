package ivkond.mc.mods.eh.gametest;

import ivkond.mc.mods.eh.domain.HomeLocation;
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
 * {@code /renhome} scenarios (docs/gametest-scenarios.md, section 4.4).
 */
//? if neoforge && <1.21.5 {
/*@net.neoforged.neoforge.gametest.GameTestHolder("easy_homes")
@net.neoforged.neoforge.gametest.PrefixGameTestTemplate(false)*/
//? }
public final class RenHomeScenarios
        // The 1.19.4-era fabric gametest api only discovers classes implementing FabricGameTest
        //? if fabric && =1.19.4 {
        /*implements net.fabricmc.fabric.api.gametest.v1.FabricGameTest*/
        //? }
        {

    /** REN-1: renaming moves the home under the new name, keeping its location. */
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
    void renameHome(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.SURVIVAL, "renhome_rename");
        reset(player, 5, 5);
        place(helper, player, 1, 1, 1, 0, 0);
        assertCommand(helper, player, "sethome base", 1);
        HomeLocation original = repo().findHome(player.getStringUUID(), "base");

        assertCommand(helper, player, "renhome base fort", 1);

        if (repo().findHome(player.getStringUUID(), "base") != null) {
            fail(helper, "Old name is still present after rename");
        }
        HomeLocation renamed = repo().findHome(player.getStringUUID(), "fort");
        if (renamed == null || !renamed.equals(original)) {
            fail(helper, "Renamed home does not carry the original location");
        }

        repo().forgetConfig(player.getStringUUID());
        repo().loadConfig(player.getStringUUID());
        if (repo().findHome(player.getStringUUID(), "fort") == null) {
            fail(helper, "Rename was not persisted");
        }
        helper.succeed();
    }

    /** REN-2: renaming a non-existent home fails. */
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
    void renameUnknownHome(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.SURVIVAL, "renhome_unknown");
        reset(player, 5, 5);
        assertCommand(helper, player, "renhome nothing newname", 0);
        helper.succeed();
    }

    /** REN-3: an invalid new name leaves the old home untouched. */
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
    void renameToInvalidName(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.SURVIVAL, "renhome_invalid");
        reset(player, 5, 5);
        place(helper, player, 1, 1, 1, 0, 0);
        assertCommand(helper, player, "sethome base", 1);

        assertCommand(helper, player, "renhome base foo bar", 0);

        if (repo().findHome(player.getStringUUID(), "base") == null) {
            fail(helper, "Original home was lost on a failed rename");
        }
        helper.succeed();
    }

    /**
     * REN-4: renaming onto an existing name replaces the target's location —
     * expected behavior (review decision 1), locked in by this test.
     */
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
    void renameOverExistingHome(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.SURVIVAL, "renhome_over");
        reset(player, 5, 5);
        place(helper, player, 1, 1, 1, 0, 0);
        assertCommand(helper, player, "sethome a", 1);
        HomeLocation a = repo().findHome(player.getStringUUID(), "a");
        place(helper, player, 5, 1, 5, 0, 0);
        assertCommand(helper, player, "sethome b", 1);

        assertCommand(helper, player, "renhome a b", 1);

        if (repo().findHome(player.getStringUUID(), "a") != null) {
            fail(helper, "Source home is still present after rename");
        }
        HomeLocation b = repo().findHome(player.getStringUUID(), "b");
        if (b == null || !b.equals(a)) {
            fail(helper, "Target home should now hold the source location");
        }
        helper.succeed();
    }
}
