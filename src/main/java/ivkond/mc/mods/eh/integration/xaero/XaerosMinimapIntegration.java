package ivkond.mc.mods.eh.integration.xaero;

import ivkond.mc.mods.eh.domain.HomeLocation;
import ivkond.mc.mods.eh.utils.Log;
import ivkond.mc.mods.eh.utils.Platform;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Syncs homes into Xaero's Minimap waypoints.
 * <p>
 * Xaero's internal API is not stable: the waypoint access path and even Waypoint
 * methods differ between releases, and builds of both eras are installable on the
 * same Minecraft versions (see issue #14). Because of that this class talks to
 * Xaero entirely through reflection and picks the access path at runtime, so a
 * mismatched Xaero release degrades to a logged warning instead of crashing
 * the client with a {@link NoSuchMethodError}.
 */
public final class XaerosMinimapIntegration {
    public static final String MOD_ID = "xaerominimap";

    private static final String WAYPOINT_NAME = "%s-home";
    private static boolean skip;

    public static void init(Platform platform) {
        skip = !platform.isModLoaded(MOD_ID);
    }

    public static void onHomeCreated(String homeName, HomeLocation location) {
        run(() -> {
            Object waypoints = getWritableWaypoints();
            if (waypoints == null) {
                return;
            }
            Object waypoint = findWaypoint(homeName);
            if (waypoint != null) {
                invoke(waypoint, "setName", WAYPOINT_NAME.formatted(homeName));
                setInitials(waypoint, homeName.substring(0, 1));
                invoke(waypoint, "setX", (int) location.x());
                invoke(waypoint, "setY", (int) location.y());
                invoke(waypoint, "setZ", (int) location.z());
            } else {
                invoke(waypoints, "add", createWaypoint(homeName, location));
            }
            saveWaypoints();
        });
    }

    public static void onHomeRenamed(String oldHomeName, String newHomeName) {
        run(() -> {
            Object waypoint = findWaypoint(oldHomeName);
            if (waypoint == null) {
                return;
            }
            invoke(waypoint, "setName", WAYPOINT_NAME.formatted(newHomeName));
            setInitials(waypoint, newHomeName.substring(0, 1));
            saveWaypoints();
        });
    }

    public static void onHomeDeleted(String homeName) {
        run(() -> {
            Object waypoints = getWritableWaypoints();
            if (waypoints == null) {
                return;
            }
            String waypointName = WAYPOINT_NAME.formatted(homeName);
            for (Object waypoint : copyWaypoints()) {
                if (waypointName.equals(nameOf(waypoint))) {
                    invoke(waypoints, "remove", waypoint);
                }
            }
            saveWaypoints();
        });
    }

    // The Xaero eras, resolved once on first use:
    // - modern (25.3+): MinimapSession -> world manager -> current waypoint set
    // - legacy: XaeroMinimapCore.currentSession -> WaypointsManager -> waypoint list
    private static final int ERA_UNRESOLVED = 0;
    private static final int ERA_MODERN = 1;
    private static final int ERA_LEGACY = 2;
    private static int era = ERA_UNRESOLVED;

    private static int era() throws Exception {
        if (era == ERA_UNRESOLVED) {
            if (available("xaero.hud.minimap.module.MinimapSession")) {
                era = ERA_MODERN;
            } else if (available("xaero.common.core.XaeroMinimapCore")) {
                era = ERA_LEGACY;
            } else {
                throw new IllegalStateException("Unsupported Xaero's Minimap build");
            }
        }
        return era;
    }

    // null means the minimap session is not ready yet (e.g. player not in a world);
    // the sync is skipped silently and retried on the next home event
    private static Object getWritableWaypoints() throws Exception {
        if (era() == ERA_MODERN) {
            return modernWaypointSet();
        }
        Object manager = invoke(currentLegacySession(), "getWaypointsManager");
        return invoke(invoke(manager, "getWaypoints"), "getList");
    }

    private static List<Object> copyWaypoints() throws Exception {
        List<Object> copy = new ArrayList<>();
        Iterable<?> waypoints = (Iterable<?>) getReadableWaypoints();
        if (waypoints != null) {
            for (Object waypoint : waypoints) {
                copy.add(waypoint);
            }
        }
        return copy;
    }

    private static Object getReadableWaypoints() throws Exception {
        if (era() == ERA_MODERN) {
            Object set = modernWaypointSet();
            return set != null ? invoke(set, "getWaypoints") : null;
        }
        Object manager = invoke(currentLegacySession(), "getWaypointsManager");
        return invoke(invoke(manager, "getWaypoints"), "getList");
    }

    private static void saveWaypoints() throws Exception {
        if (era() == ERA_MODERN) {
            Object session = modernSession();
            if (session == null) {
                return;
            }
            Object world = invoke(invoke(session, "getWorldManager"), "getCurrentWorld");
            invoke(invoke(session, "getWorldManagerIO"), "saveWorld", world);
            return;
        }
        Object manager = invoke(currentLegacySession(), "getWaypointsManager");
        Object settings = invoke(field(Class.forName("xaero.minimap.XaeroMinimap"), "instance"), "getSettings");
        invoke(settings, "saveWaypoints", invoke(manager, "getCurrentWorld"));
    }

    private static Object modernWaypointSet() throws Exception {
        Object world = modernWorld();
        return world != null ? invoke(world, "getCurrentWaypointSet") : null;
    }

    private static Object modernWorld() throws Exception {
        Object session = modernSession();
        if (session == null) {
            return null;
        }
        return invoke(invoke(session, "getWorldManager"), "getCurrentWorld");
    }

    private static Object modernSession() throws Exception {
        Object xaeroSession = invoke(Class.forName("xaero.common.XaeroMinimapSession"), "getCurrentSession");
        if (xaeroSession == null) {
            return null;
        }
        Object processor = invoke(xaeroSession, "getMinimapProcessor");
        if (processor == null) {
            return null;
        }
        return invoke(processor, "getSession");
    }

    private static Object currentLegacySession() throws Exception {
        return field(Class.forName("xaero.common.core.XaeroMinimapCore"), "currentSession");
    }

    private static Object createWaypoint(String homeName, HomeLocation location) throws Exception {
        String name = WAYPOINT_NAME.formatted(homeName);
        String initials = homeName.substring(0, 1);
        int x = (int) location.x();
        int y = (int) location.y();
        int z = (int) location.z();
        Class<?> waypointClass = Class.forName("xaero.common.minimap.waypoints.Waypoint");
        for (Constructor<?> constructor : waypointClass.getConstructors()) {
            Class<?>[] params = constructor.getParameterTypes();
            if (params.length == 8 && params[0] == int.class) {
                Object color = params[5] == int.class ? (int) (Math.random() * 16)
                        : invoke(Class.forName("xaero.hud.minimap.waypoint.WaypointColor"), "getRandom");
                Object purpose = params[6] == int.class ? 0
                        : Enum.valueOf((Class<? extends Enum>) Class.forName("xaero.hud.minimap.waypoint.WaypointPurpose"), "NORMAL");
                return constructor.newInstance(x, y, z, name, initials, color, purpose, false);
            }
        }
        throw new IllegalStateException("No suitable Waypoint constructor found");
    }

    private static Object findWaypoint(String homeName) throws Exception {
        String waypointName = WAYPOINT_NAME.formatted(homeName);
        for (Object waypoint : copyWaypoints()) {
            if (waypointName.equals(nameOf(waypoint))) {
                return waypoint;
            }
        }
        return null;
    }

    private static String nameOf(Object waypoint) throws Exception {
        try {
            return (String) invoke(waypoint, "getComparisonName");
        } catch (NoSuchMethodException e) {
            return (String) invoke(waypoint, "getName");
        }
    }

    private static void setInitials(Object waypoint, String initials) throws Exception {
        try {
            invoke(waypoint, "setInitials", initials);
        } catch (NoSuchMethodException e) {
            // Xaero 26.6+ dropped setInitials in favor of setSymbol
            invoke(waypoint, "setSymbol", initials);
        }
    }

    // Reflection helpers

    private static void run(Task task) {
        if (skip) {
            return;
        }
        try {
            task.run();
        } catch (Throwable t) {
            skip = true;
            Log.error("Xaero's Minimap integration failed, disabling it", t);
        }
    }

    private interface Task {
        void run() throws Exception;
    }

    private static boolean available(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static Object invoke(Object target, String method, Object... args) throws Exception {
        // a Class target means a static method call
        Class<?> owner = target instanceof Class<?> cls ? cls : target.getClass();
        Object receiver = target instanceof Class<?> ? null : target;
        for (Method candidate : owner.getMethods()) {
            if (!candidate.getName().equals(method) || !matches(candidate.getParameterTypes(), args)) {
                continue;
            }
            return candidate.invoke(receiver, args);
        }
        throw new NoSuchMethodException(method + " on " + owner.getName());
    }

    private static Object field(Class<?> owner, String name) throws Exception {
        Field field = owner.getField(name);
        return field.get(null);
    }

    private static boolean matches(Class<?>[] params, Object[] args) {
        if (params.length != args.length) {
            return false;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null && !wrap(params[i]).isInstance(args[i])) {
                return false;
            }
        }
        return true;
    }

    private static Class<?> wrap(Class<?> primitive) {
        if (!primitive.isPrimitive()) {
            return primitive;
        }
        if (primitive == int.class) return Integer.class;
        if (primitive == long.class) return Long.class;
        if (primitive == boolean.class) return Boolean.class;
        if (primitive == double.class) return Double.class;
        if (primitive == float.class) return Float.class;
        if (primitive == short.class) return Short.class;
        if (primitive == byte.class) return Byte.class;
        return Character.class;
    }
}
