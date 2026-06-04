package ivkond.mc.mods.eh.integration.xaero;

import ivkond.mc.mods.eh.domain.HomeLocation;
import ivkond.mc.mods.eh.utils.Log;
import ivkond.mc.mods.eh.utils.Platform;
import xaero.common.XaeroMinimapSession;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.WaypointColor;
import xaero.hud.minimap.waypoint.WaypointPurpose;
import xaero.hud.minimap.waypoint.set.WaypointSet;
import xaero.hud.minimap.world.MinimapWorld;

import java.io.IOException;
import java.util.ArrayList;

public class XaerosMinimapIntegration {
    public static final String MOD_ID = "xaerominimap";

    private static final String WAYPOINT_NAME = "%s-home";
    private static boolean skip;

    public static void init(Platform platform) {
        skip = !platform.isModLoaded(MOD_ID);
    }

    public static void onHomeCreated(String homeName, HomeLocation location) {
        if (skip) {
            return;
        }

        MinimapWorld world = getCurrentWorld();
        if (world == null) {
            return;
        }

        WaypointSet waypointSet = world.getCurrentWaypointSet();
        ArrayList<Waypoint> waypoints = copyWaypoints(waypointSet);

        Waypoint existing = findWaypointByHomeName(waypoints, homeName);
        if (existing != null) {
            existing.setName(WAYPOINT_NAME.formatted(homeName));
            existing.setInitials(homeName.substring(0, 1));
            existing.setX((int) location.x());
            existing.setY((int) location.y());
            existing.setZ((int) location.z());
        } else {
            Waypoint newWaypoint = new Waypoint(
                    (int) location.x(),
                    (int) location.y(),
                    (int) location.z(),
                    WAYPOINT_NAME.formatted(homeName),
                    homeName.substring(0, 1),
                    WaypointColor.getRandom(),
                    WaypointPurpose.NORMAL,
                    false
            );
            waypointSet.add(newWaypoint);
        }

        saveWaypoints(world);
    }

    public static void onHomeRenamed(String oldHomeName, String newHomeName) {
        if (skip) {
            return;
        }

        MinimapWorld world = getCurrentWorld();
        if (world == null) {
            return;
        }

        Waypoint waypoint = findWaypointByHomeName(copyWaypoints(world.getCurrentWaypointSet()), oldHomeName);
        if (waypoint == null) {
            return;
        }

        waypoint.setName(WAYPOINT_NAME.formatted(newHomeName));
        waypoint.setInitials(newHomeName.substring(0, 1));

        saveWaypoints(world);
    }

    public static void onHomeDeleted(String homeName) {
        if (skip) {
            return;
        }

        MinimapWorld world = getCurrentWorld();
        if (world == null) {
            return;
        }

        WaypointSet waypointSet = world.getCurrentWaypointSet();
        String waypointName = WAYPOINT_NAME.formatted(homeName);
        for (Waypoint waypoint : copyWaypoints(waypointSet)) {
            if (waypoint.getComparisonName().equals(waypointName)) {
                waypointSet.remove(waypoint);
            }
        }

        saveWaypoints(world);
    }

    private static MinimapWorld getCurrentWorld() {
        MinimapSession session = getMinimapSession();
        if (session == null) {
            return null;
        }
        return session.getWorldManager().getCurrentWorld();
    }

    private static MinimapSession getMinimapSession() {
        XaeroMinimapSession session = XaeroMinimapSession.getCurrentSession();
        if (session == null || session.getMinimapProcessor() == null) {
            return null;
        }
        return session.getMinimapProcessor().getSession();
    }

    private static ArrayList<Waypoint> copyWaypoints(WaypointSet waypointSet) {
        ArrayList<Waypoint> waypoints = new ArrayList<>();
        waypointSet.getWaypoints().forEach(waypoints::add);
        return waypoints;
    }

    private static void saveWaypoints(MinimapWorld world) {
        MinimapSession session = getMinimapSession();
        if (session == null) {
            return;
        }

        try {
            session.getWorldManagerIO().saveWorld(world);
        } catch (IOException e) {
            Log.error("Unable to save waypoints", e);
        }
    }

    private static Waypoint findWaypointByHomeName(Iterable<Waypoint> waypoints, String homeName) {
        String waypointName = WAYPOINT_NAME.formatted(homeName);
        for (Waypoint waypoint : waypoints) {
            if (waypoint.getComparisonName().equals(waypointName)) {
                return waypoint;
            }
        }
        return null;
    }
}
