package ivkond.mc.mods.eh.integration.xaero;

import ivkond.mc.mods.eh.domain.HomeLocation;
import ivkond.mc.mods.eh.utils.Log;
import ivkond.mc.mods.eh.utils.Platform;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.hud.minimap.waypoint.WaypointColor;
import xaero.hud.minimap.waypoint.WaypointPurpose;
import xaero.minimap.XaeroMinimap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        WaypointsManager waypointsManager = XaeroMinimapCore.currentSession.getWaypointsManager();
        ArrayList<Waypoint> waypoints = waypointsManager.getWaypoints().getList();

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
            waypoints.add(newWaypoint);
        }

        saveWaypoints(waypointsManager);
    }

    public static void onHomeRenamed(String oldHomeName, String newHomeName) {
        if (skip) {
            return;
        }

        WaypointsManager waypointsManager = XaeroMinimapCore.currentSession.getWaypointsManager();
        ArrayList<Waypoint> waypoints = waypointsManager.getWaypoints().getList();

        Waypoint waypoint = findWaypointByHomeName(waypoints, oldHomeName);
        if (waypoint == null) {
            return;
        }

        waypoint.setName(WAYPOINT_NAME.formatted(newHomeName));
        waypoint.setInitials(newHomeName.substring(0, 1));

        saveWaypoints(waypointsManager);
    }

    public static void onHomeDeleted(String homeName) {
        if (skip) {
            return;
        }

        WaypointsManager waypointsManager = XaeroMinimapCore.currentSession.getWaypointsManager();
        List<Waypoint> waypoints = waypointsManager.getWaypoints().getList();

        String waypointName = WAYPOINT_NAME.formatted(homeName);
        waypoints.removeIf(waypoint -> waypoint.getName().equals(waypointName));

        saveWaypoints(waypointsManager);
    }

    private static void saveWaypoints(WaypointsManager waypointsManager) {
        try {
            XaeroMinimap.instance.getSettings().saveWaypoints(waypointsManager.getCurrentWorld());
        } catch (IOException e) {
            Log.error("Unable to save waypoints", e);
        }
    }

    private static Waypoint findWaypointByHomeName(List<Waypoint> waypoints, String homeName) {
        String waypointName = WAYPOINT_NAME.formatted(homeName);
        for (Waypoint waypoint : waypoints) {
            if (waypoint.getComparisonName().equals(waypointName)) {
                return waypoint;
            }
        }
        return null;
    }
}
