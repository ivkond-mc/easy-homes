package ivkond.mc.mods.eh.gametest;

import ivkond.mc.mods.eh.domain.HomeLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
//? if >=26.2 {
/*import net.minecraft.world.entity.EntityTypes;
*///? } else {
import net.minecraft.world.entity.EntityType;
//? }
//? if >=1.21.11 {
import net.minecraft.world.entity.vehicle.boat.Boat;
//? } else {
/*import net.minecraft.world.entity.vehicle.Boat;
*///? }
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;

import static ivkond.mc.mods.eh.gametest.Eh.assertAt;
import static ivkond.mc.mods.eh.gametest.Eh.fail;
import static ivkond.mc.mods.eh.gametest.Eh.assertCommand;
import static ivkond.mc.mods.eh.gametest.Eh.assertPosition;
import static ivkond.mc.mods.eh.gametest.Eh.assertRotation;
import static ivkond.mc.mods.eh.gametest.Eh.place;
import static ivkond.mc.mods.eh.gametest.Eh.player;
import static ivkond.mc.mods.eh.gametest.Eh.repo;
import static ivkond.mc.mods.eh.gametest.Eh.reset;

/**
 * {@code /home} scenarios (docs/gametest-scenarios.md, section 4.2).
 */
//? if neoforge && <1.21.5 {
/*@net.neoforged.neoforge.gametest.GameTestHolder("easy_homes")
@net.neoforged.neoforge.gametest.PrefixGameTestTemplate(false)*/
//? }
public final class HomeScenarios {

    /** HOM-1: teleports to a named home, restoring position and rotation. */
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
    void teleportToNamedHome(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.CREATIVE, "home_teleport");
        reset(player, 5, 5);
        place(helper, player, 1, 1, 1, 30, -45);
        assertCommand(helper, player, "sethome base", 1);

        place(helper, player, 5, 1, 5, 0, 0);
        assertCommand(helper, player, "home base", 1);

        assertAt(helper, player, new BlockPos(1, 1, 1));
        assertRotation(helper, player, 30, -45);
        helper.succeed();
    }

    /** HOM-2: bare /home teleports to the default home {@code main}. */
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
    void teleportToDefaultHome(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.CREATIVE, "home_default");
        reset(player, 5, 5);
        place(helper, player, 2, 1, 2, 0, 0);
        assertCommand(helper, player, "sethome", 1);

        place(helper, player, 5, 1, 5, 0, 0);
        assertCommand(helper, player, "home", 1);

        assertAt(helper, player, new BlockPos(2, 1, 2));
        helper.succeed();
    }

    /** HOM-3: unknown home fails and leaves the player where they are. */
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
    void unknownHome(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.CREATIVE, "home_unknown");
        reset(player, 5, 5);
        place(helper, player, 3, 1, 3, 0, 0);
        var before = player.position();

        assertCommand(helper, player, "home nowhere", 0);
        assertPosition(helper, player, before.x, before.y, before.z);
        helper.succeed();
    }

    /** HOM-4: invalid home names are rejected without a teleport. */
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
    void invalidHomeName(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.CREATIVE, "home_invalid");
        reset(player, 5, 5);
        place(helper, player, 3, 1, 3, 0, 0);

        assertCommand(helper, player, "home foo bar", 0);
        helper.succeed();
    }

    /**
     * HOM-5: with {@code cooldown = 1} a survival player's second teleport is
     * blocked, and succeeds again once the cooldown has expired (~30 ticks).
     */
    //? if <1.21.5 && fabric {
    /*@net.minecraft.gametest.framework.GameTest(template = Templates.PLATFORM, timeoutTicks = 200)*/
    //? }
    //? if <1.21.5 && neoforge {
    /*@net.minecraft.gametest.framework.GameTest(template = "platform", timeoutTicks = 200)*/
    //? }
    //? if fabric && >=1.21.5 {
    @net.fabricmc.fabric.api.gametest.v1.GameTest(structure = Templates.PLATFORM, maxTicks = 200)
    //? }
    public
    //? if fabric && >=1.21.5 {
    //? } else {
    /*static*/
    //? }
    void cooldownBlocksThenExpires(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.SURVIVAL, "home_cooldown");
        reset(player, 1, 5);
        place(helper, player, 1, 1, 1, 0, 0);
        assertCommand(helper, player, "sethome a", 1);
        place(helper, player, 5, 1, 5, 0, 0);
        assertCommand(helper, player, "sethome b", 1);

        place(helper, player, 3, 1, 3, 0, 0);
        assertCommand(helper, player, "home a", 1);
        // Still on cooldown — blocked
        assertCommand(helper, player, "home b", 0);
        assertAt(helper, player, new BlockPos(1, 1, 1));

        // The game test server accelerates ticks faster than wall-clock time,
        // so simulate the second having passed by rewinding the teleport time.
        repo().getHomes(player.getStringUUID())
                .setLastTeleportation(java.time.OffsetDateTime.now().minusSeconds(5));
        assertCommand(helper, player, "home b", 1);
        assertAt(helper, player, new BlockPos(5, 1, 5));
        helper.succeed();
    }

    /** HOM-6: creative players teleport without waiting out the cooldown. */
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
    void creativeIgnoresCooldown(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.CREATIVE, "home_creative_cd");
        reset(player, 10, 5);
        place(helper, player, 1, 1, 1, 0, 0);
        assertCommand(helper, player, "sethome a", 1);
        place(helper, player, 5, 1, 5, 0, 0);
        assertCommand(helper, player, "sethome b", 1);

        place(helper, player, 3, 1, 3, 0, 0);
        assertCommand(helper, player, "home a", 1);
        assertCommand(helper, player, "home b", 1);
        assertAt(helper, player, new BlockPos(5, 1, 5));
        helper.succeed();
    }

    /**
     * HOM-7: a home saved in the nether brings an overworld player back into
     * the nether at the stored coordinates.
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
    void crossDimensionalHome(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.CREATIVE, "home_crossdim");
        reset(player, 5, 5);
        ServerLevel nether = helper.getLevel().getServer().getLevel(Level.NETHER);
        if (nether == null) {
            fail(helper, "Nether level is not loaded");
        }

        Eh.move(player, nether, 8, 64, 8, 0, 0);
        assertCommand(helper, player, "sethome hell", 1);

        Eh.move(player, helper.getLevel(), 100, 64, 100, 0, 0);
        assertCommand(helper, player, "home hell", 1);

        if (player.level().dimension() != Level.NETHER) {
            fail(helper, "Player is in " + player.level().dimension().identifier() + ", expected the nether");
        }
        assertPosition(helper, player, 8, 64, 8);
        helper.succeed();
    }

    /** HOM-8: a home whose dimension no longer resolves fails politely. */
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
    void unknownDimensionHome(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.CREATIVE, "home_unk_dim");
        reset(player, 5, 5);
        place(helper, player, 3, 1, 3, 0, 0);
        var before = player.position();

        repo().setHome(player.getStringUUID(), "ghost", new HomeLocation("easy_homes:void", 0, 64, 0, 0, 0));
        assertCommand(helper, player, "home ghost", 0);
        assertPosition(helper, player, before.x, before.y, before.z);
        helper.succeed();
    }

    /** HOM-9: a home outside spawnable bounds is refused. */
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
    void outOfBoundsPosition(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.CREATIVE, "home_oob");
        reset(player, 5, 5);
        place(helper, player, 3, 1, 3, 0, 0);
        var before = player.position();

        repo().setHome(player.getStringUUID(), "sky", new HomeLocation("minecraft:overworld", 0, 40_000_000, 0, 0, 0));
        assertCommand(helper, player, "home sky", 0);
        assertPosition(helper, player, before.x, before.y, before.z);
        helper.succeed();
    }

    /** HOM-10: a player controlling a vehicle is not teleported. */
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
    void mountedPlayerRejected(GameTestHelper helper) {
        ServerPlayer player = player(helper, GameType.CREATIVE, "home_mounted");
        reset(player, 5, 5);
        place(helper, player, 1, 1, 1, 0, 0);
        assertCommand(helper, player, "sethome base", 1);

        //? if >=26.2 {
        /*Boat boat = helper.spawn(EntityTypes.OAK_BOAT, 3, 1, 3);
        *///? } else {
        Boat boat = helper.spawn(EntityType.OAK_BOAT, 3, 1, 3);
        //? }
        player.startRiding(boat);
        //? if <1.19.3 {
        /*if (!player.isPassenger()) {
            fail(helper, "Precondition failed: player does not control the boat");
        }*/
        //? } else {
        if (!player.isPassenger() || !player.canControlVehicle()) {
            fail(helper, "Precondition failed: player does not control the boat");
        }
        //? }
        var before = player.position();

        assertCommand(helper, player, "home base", 0);
        assertPosition(helper, player, before.x, before.y, before.z);
        helper.succeed();
    }
}
