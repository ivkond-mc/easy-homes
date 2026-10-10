package ivkond.mc.mods.eh.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;

import static ivkond.mc.mods.eh.gametest.Eh.assertAt;
import static ivkond.mc.mods.eh.gametest.Eh.fail;
import static ivkond.mc.mods.eh.gametest.Eh.assertCommand;
import static ivkond.mc.mods.eh.gametest.Eh.place;
import static ivkond.mc.mods.eh.gametest.Eh.player;
import static ivkond.mc.mods.eh.gametest.Eh.reset;

/**
 * {@code /back} scenarios (docs/gametest-scenarios.md, section 4.5).
 *
 * <p>Uses creative players so the shared teleport cooldown never gets in the
 * way of the history assertions.
 */
//? if neoforge && <1.21.5 {
/*@net.neoforged.neoforge.gametest.GameTestHolder("easy_homes")
@net.neoforged.neoforge.gametest.PrefixGameTestTemplate(false)*/
//? }
public final class BackScenarios {

    /** BAC-1: /back without teleport history fails. */
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
    void backWithoutHistory(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.CREATIVE, "back_none");
        reset(player, 5, 5);
        place(helper, player, 3, 1, 3, 0, 0);
        assertCommand(helper, player, "back", 0);
        helper.succeed();
    }

    /** BAC-2: after visiting {@code a} then {@code b}, /back returns to {@code a}. */
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
    void backReturnsToPreviousHome(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.CREATIVE, "back_prev");
        reset(player, 5, 5);
        place(helper, player, 1, 1, 1, 0, 0);
        assertCommand(helper, player, "sethome a", 1);
        place(helper, player, 5, 1, 5, 0, 0);
        assertCommand(helper, player, "sethome b", 1);

        place(helper, player, 3, 1, 3, 0, 0);
        assertCommand(helper, player, "home a", 1);
        assertCommand(helper, player, "home b", 1);
        assertCommand(helper, player, "back", 1);
        assertAt(helper, player, new BlockPos(1, 1, 1));
        helper.succeed();
    }

    /** BAC-3: repeated /back toggles between the two most recent distinct homes. */
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
    void backAlternates(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.CREATIVE, "back_alt");
        reset(player, 5, 5);
        place(helper, player, 1, 1, 1, 0, 0);
        assertCommand(helper, player, "sethome a", 1);
        place(helper, player, 5, 1, 5, 0, 0);
        assertCommand(helper, player, "sethome b", 1);

        place(helper, player, 3, 1, 3, 0, 0);
        assertCommand(helper, player, "home a", 1);
        assertCommand(helper, player, "home b", 1);

        assertCommand(helper, player, "back", 1);
        assertAt(helper, player, new BlockPos(1, 1, 1));
        assertCommand(helper, player, "back", 1);
        assertAt(helper, player, new BlockPos(5, 1, 5));
        assertCommand(helper, player, "back", 1);
        assertAt(helper, player, new BlockPos(1, 1, 1));
        helper.succeed();
    }

    /**
     * BAC-4: teleporting to the same home twice in a row does not push it into
     * history — /back still targets the previous <em>distinct</em> home.
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
    void backIgnoresRepeatedHome(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.CREATIVE, "back_repeat");
        reset(player, 5, 5);
        place(helper, player, 1, 1, 1, 0, 0);
        assertCommand(helper, player, "sethome a", 1);
        place(helper, player, 5, 1, 5, 0, 0);
        assertCommand(helper, player, "sethome b", 1);

        place(helper, player, 3, 1, 3, 0, 0);
        assertCommand(helper, player, "home a", 1);
        place(helper, player, 3, 1, 3, 0, 0);
        assertCommand(helper, player, "home a", 1);
        // Only 'a' was ever visited — there is no previous distinct home yet
        assertCommand(helper, player, "back", 0);

        assertCommand(helper, player, "home b", 1);
        place(helper, player, 3, 1, 3, 0, 0);
        assertCommand(helper, player, "home b", 1);
        // Repeated 'b' must not hide 'a' in history
        assertCommand(helper, player, "back", 1);
        assertAt(helper, player, new BlockPos(1, 1, 1));
        helper.succeed();
    }
}
