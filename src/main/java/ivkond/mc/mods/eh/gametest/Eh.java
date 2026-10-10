package ivkond.mc.mods.eh.gametest;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import ivkond.mc.mods.eh.network.HomeCreatedPayload;
import ivkond.mc.mods.eh.network.HomeDeletedPayload;
import ivkond.mc.mods.eh.network.HomeRenamedPayload;
import ivkond.mc.mods.eh.config.EasyHomesConfig;
import ivkond.mc.mods.eh.storage.HomeRepository;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.MinecraftServer;
//? if >=1.20.2 {
import net.minecraft.server.level.ClientInformation;
//? }
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
//? if >=1.20.2 {
import net.minecraft.server.network.CommonListenerCookie;
//? }
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

import java.util.Set;
import java.util.UUID;

/**
 * Shared harness for the Easy Homes game tests: fake-player login, command
 * execution and state assertions. Everything here runs against the real
 * server-side command pipeline, the same code a player's client would hit.
 */
public final class Eh {
    private Eh() {
    }

    /**
     * Creates a detached ServerPlayer backed by a black-hole connection whose
     * {@code send} does nothing. On NeoForge the connection additionally gets a
     * sink netty channel and the mod's payload channels registered as ad-hoc,
     * because NeoForge reads connection state from netty channel attributes
     * and rejects undeclared custom payloads. The player is never added to the
     * world, does not tick, and gets a fresh UUID so tests never share
     * repository state with each other.
     */
    public static ServerPlayer player(GameTestHelper helper, GameType gameMode, String name) {
        MinecraftServer server = helper.getLevel().getServer();
        GameProfile profile = new GameProfile(UUID.randomUUID(), name);
        Connection connection = new Connection(PacketFlow.SERVERBOUND) {
            //? if >=1.21.8 {
            @Override
            public void send(net.minecraft.network.protocol.Packet<?> packet,
                             io.netty.channel.ChannelFutureListener listener, boolean flush) {
            }
            //? } else if <1.19.1 {
            /*@Override
            public void send(net.minecraft.network.protocol.Packet<?> packet) {
            }*/
            //? } else {
            /*@Override
            public void send(net.minecraft.network.protocol.Packet<?> packet,
                             net.minecraft.network.PacketSendListener listener, boolean flush) {
            }*/
            //? }
        };
        //? if neoforge {
        /*try {
            var channelField = Connection.class.getDeclaredField("channel");
            channelField.setAccessible(true);
            channelField.set(connection, new io.netty.channel.embedded.EmbeddedChannel());
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to attach sink channel to test connection", e);
        }
        var adHocChannels = net.neoforged.neoforge.network.registration.ChannelAttributes.getOrCreateAdHocChannels(connection);
        adHocChannels.add(HomeCreatedPayload.ID.id());
        adHocChannels.add(HomeDeletedPayload.ID.id());
        adHocChannels.add(HomeRenamedPayload.ID.id());*/
        //? }
        //? if <1.20.2 {
        /*// 1.18.2: no ClientInformation / CommonListenerCookie yet
        ServerPlayer player = new ServerPlayer(server, helper.getLevel(), profile);
        player.connection = new ServerGamePacketListenerImpl(server, connection, player);
        player.setGameMode(gameMode);
        return player;*/
        //? } else {
        CommonListenerCookie cookie = CommonListenerCookie.createInitial(profile, false);
        ServerPlayer player = new ServerPlayer(server, helper.getLevel(), profile, ClientInformation.createDefault());
        player.connection = new ServerGamePacketListenerImpl(server, connection, player, cookie);
        player.setGameMode(gameMode);
        return player;
        //? }
    }

    /** Places the player on a block of the test structure with the given rotation. */
    public static void place(GameTestHelper helper, ServerPlayer player, int x, int y, int z, float yaw, float pitch) {
        BlockPos pos = helper.absolutePos(new BlockPos(x, y, z));
        //? if >=1.21.5 {
        player.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, yaw, pitch);
        //? } else {
        /*player.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, yaw, pitch);*/
        //? }
    }

    /** Teleports the player within the test level, bypassing mod commands (test setup only). */
    public static void move(ServerPlayer player, ServerLevel level, double x, double y, double z, float yaw, float pitch) {
        //? if <1.19 {
        /*player.teleportTo(level, x, y, z, yaw, pitch);*/
        //? } else {
        player.teleportTo(level, x, y, z, Set.of(), yaw, pitch, false);
        //? }
    }

    /** Runs a command as the player through the live command dispatcher and returns its exit code. */
    public static int run(GameTestHelper helper, ServerPlayer player, String command) {
        try {
            return helper.getLevel().getServer().getCommands().getDispatcher()
                    .execute(command, player.createCommandSourceStack());
        } catch (CommandSyntaxException e) {
            throw new IllegalStateException("Command failed to parse: " + command, e);
        }
    }

    /**
     * Fails the test with a message. 1.21.4 only accepts strings, 1.21.5+
     * only components (the String overload returned in 1.21.9, but this
     * covers every node uniformly).
     */
    public static void fail(GameTestHelper helper, String message) {
        //? if <1.21.5 {
        /*helper.fail(message);*/
        //? } else {
        helper.fail(Component.literal(message));
        //? }
    }

    public static void assertCommand(GameTestHelper helper, ServerPlayer player, String command, int expected) {
        int result = run(helper, player, command);
        if (result != expected) {
            fail(helper, "Command '%s' returned %d, expected %d".formatted(command, result, expected));
        }
    }

    public static void assertPosition(GameTestHelper helper, ServerPlayer player, double x, double y, double z) {
        Vec3 pos = player.position();
        if (Math.abs(pos.x - x) > 0.001 || Math.abs(pos.y - y) > 0.001 || Math.abs(pos.z - z) > 0.001) {
            fail(helper, "Player at %s, expected [%s, %s, %s]".formatted(pos, x, y, z));
        }
    }

    public static void assertAt(GameTestHelper helper, ServerPlayer player, BlockPos rel) {
        BlockPos abs = helper.absolutePos(rel);
        assertPosition(helper, player, abs.getX() + 0.5, abs.getY(), abs.getZ() + 0.5);
    }

    public static void assertRotation(GameTestHelper helper, ServerPlayer player, float yaw, float pitch) {
        if (Math.abs(player.getYRot() - yaw) > 0.01 || Math.abs(player.getXRot() - pitch) > 0.01) {
            fail(helper, "Player rotation yaw=%s pitch=%s, expected %s/%s"
                    .formatted(player.getYRot(), player.getXRot(), yaw, pitch));
        }
    }

    /** Isolates repository state and pins config values for the duration of a test. */
    public static void reset(ServerPlayer player, int cooldown, int maxHomes) {
        HomeRepository.INSTANCE.forgetConfig(player.getStringUUID());
        EasyHomesConfig.cooldown = cooldown;
        EasyHomesConfig.maxHomes = maxHomes;
    }

    public static HomeRepository repo() {
        return HomeRepository.INSTANCE;
    }
}
