package ivkond.mc.mods.eh.domain;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlayerHomes {
    // Map<Name, Location>
    private final Map<String, HomeLocation> homes = new HashMap<>();
    private OffsetDateTime lastTeleportation;

    public HomeLocation findHome(String name) {
        return homes.get(name);
    }

    public void setHome(String name, HomeLocation home) {
        homes.put(name, home);
    }

    public void removeHome(String name) {
        homes.remove(name);
    }

    public Map<String, HomeLocation> getAllHomes() {
        return Collections.unmodifiableMap(homes);
    }

    public OffsetDateTime getLastTeleportation() {
        return lastTeleportation;
    }

    public void setLastTeleportation(OffsetDateTime lastTeleportation) {
        this.lastTeleportation = lastTeleportation;
    }
}
