package ivkond.mc.mods.eh.gametest;

import ivkond.mc.mods.eh.domain.HomeLocation;
import ivkond.mc.mods.eh.utils.PathUtils;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

import java.nio.file.Files;
import java.nio.file.Path;

import static ivkond.mc.mods.eh.gametest.Eh.assertCommand;
import static ivkond.mc.mods.eh.gametest.Eh.fail;
import static ivkond.mc.mods.eh.gametest.Eh.assertRotation;
import static ivkond.mc.mods.eh.gametest.Eh.place;
import static ivkond.mc.mods.eh.gametest.Eh.player;
import static ivkond.mc.mods.eh.gametest.Eh.repo;
import static ivkond.mc.mods.eh.gametest.Eh.reset;

/**
 * {@code /sethome} scenarios (docs/gametest-scenarios.md, section 4.1).
 *
 * <p>Doubles as the {@code fabric-gametest} entrypoint on Fabric nodes and is
 * registered through {@code RegisterGameTestsEvent} on NeoForge 1.21.4.
 */
//? if neoforge && <1.21.5 {
/*@net.neoforged.neoforge.gametest.GameTestHolder("easy_homes")
@net.neoforged.neoforge.gametest.PrefixGameTestTemplate(false)*/
//? }
public final class SetHomeScenarios
        // The 1.19.4-era fabric gametest api only discovers classes implementing FabricGameTest
        //? if fabric && =1.19.4 {
        /*implements net.fabricmc.fabric.api.gametest.v1.FabricGameTest*/
        //? }
        {

    /** SET-1: bare /sethome stores the default home {@code main} at the player's position. */
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
    void setDefaultHome(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.SURVIVAL, "sethome_default");
        reset(player, 5, 5);
        place(helper, player, 1, 1, 1, 10, 20);

        assertCommand(helper, player, "sethome", 1);

        HomeLocation home = repo().findHome(player.getStringUUID(), "main");
        if (home == null) {
            fail(helper, "Default home 'main' was not stored");
        }
        assertRotation(helper, player, home.rotY(), home.rotX());
        helper.succeed();
    }

    /** SET-2: named home keeps position and rotation. */
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
    void setNamedHome(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.SURVIVAL, "sethome_named");
        reset(player, 5, 5);
        place(helper, player, 2, 1, 3, -30, 45);

        assertCommand(helper, player, "sethome base", 1);

        HomeLocation home = repo().findHome(player.getStringUUID(), "base");
        if (home == null) {
            fail(helper, "Home 'base' was not stored");
        }
        var pos = player.position();
        if (home.x() != pos.x || home.y() != pos.y || home.z() != pos.z) {
            fail(helper, "Stored position %s does not match player %s".formatted(home.coordinates(), pos));
        }
        helper.succeed();
    }

    /** SET-3: setting an existing name overwrites the stored location. */
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
    void overwriteHome(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.SURVIVAL, "sethome_overwrite");
        reset(player, 5, 5);

        place(helper, player, 1, 1, 1, 0, 0);
        assertCommand(helper, player, "sethome base", 1);
        place(helper, player, 5, 1, 5, 0, 0);
        assertCommand(helper, player, "sethome base", 1);

        HomeLocation home = repo().findHome(player.getStringUUID(), "base");
        var pos = player.position();
        if (home == null || home.x() != pos.x || home.z() != pos.z) {
            fail(helper, "Home was not overwritten with the new position");
        }
        helper.succeed();
    }

    /** SET-4: names with spaces, over 64 chars or non-ascii characters are rejected. */
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
    void invalidNames(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.SURVIVAL, "sethome_invalid");
        reset(player, 5, 5);

        String tooLong = "a".repeat(65);
        assertCommand(helper, player, "sethome foo bar", 0);
        assertCommand(helper, player, "sethome " + tooLong, 0);
        assertCommand(helper, player, "sethome дом", 0);

        var stored = repo().getHomes(player.getStringUUID());
        if (stored != null && !stored.getAllHomes().isEmpty()) {
            fail(helper, "An invalid name was stored");
        }
        helper.succeed();
    }

    /** SET-5: {@code /sethome -} generates a name from dimension and coordinates. */
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
    void autoGeneratedName(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.SURVIVAL, "sethome_auto");
        reset(player, 5, 5);
        place(helper, player, 3, 1, 3, 0, 0);

        assertCommand(helper, player, "sethome -", 1);

        var pos = player.position();
        String dimension = player.level().dimension().identifier().getPath();
        String expected = String.format("%s_%.0f_%.0f_%.0f", dimension, pos.x, pos.y, pos.z);
        if (repo().findHome(player.getStringUUID(), expected) == null) {
            fail(helper, "Auto-generated home '" + expected + "' was not stored");
        }
        helper.succeed();
    }

    /**
     * SET-6: survival players are capped at {@code maxHomes}; overwriting an
     * existing name is still allowed at the cap.
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
    void maxHomesLimit(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.SURVIVAL, "sethome_max");
        reset(player, 5, 2);

        assertCommand(helper, player, "sethome one", 1);
        assertCommand(helper, player, "sethome two", 1);
        assertCommand(helper, player, "sethome three", 0);

        if (repo().findHome(player.getStringUUID(), "three") != null) {
            fail(helper, "Home beyond the limit was stored");
        }

        // Overwriting an existing home stays allowed at the cap
        assertCommand(helper, player, "sethome one", 1);
        helper.succeed();
    }

    /** SET-7: creative players are not limited. */
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
    void creativeIgnoresLimit(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.CREATIVE, "sethome_creative");
        reset(player, 5, 2);

        assertCommand(helper, player, "sethome one", 1);
        assertCommand(helper, player, "sethome two", 1);
        assertCommand(helper, player, "sethome three", 1);
        assertCommand(helper, player, "sethome four", 1);
        helper.succeed();
    }

    /**
     * SET-8: homes are written to {@code <world>/data/easy_homes/<uuid>.json}
     * and survive a simulated relogin (logout drops the cache, login reloads).
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
    void persistenceRoundtrip(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.SURVIVAL, "sethome_persist");
        reset(player, 5, 5);
        place(helper, player, 2, 1, 2, 0, 0);

        assertCommand(helper, player, "sethome base", 1);

        Path file = PathUtils.getOrCreateDataDir(helper.getLevel().getServer())
                .resolve(player.getStringUUID() + ".json");
        if (!Files.isRegularFile(file)) {
            fail(helper, "Player homes file was not written: " + file);
        }

        repo().forgetConfig(player.getStringUUID());
        repo().loadConfig(player.getStringUUID());
        if (repo().findHome(player.getStringUUID(), "base") == null) {
            fail(helper, "Home did not survive the relogin roundtrip");
        }
        helper.succeed();
    }
}
