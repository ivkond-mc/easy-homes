package ivkond.mc.mods.eh.domain;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlayerHomes {
    // Map<Name, Location>
    private final Map<String, HomeLocation> homes = new HashMap<>();
    private final String[] lastVisitedHomes = new String[2];
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

    public String getLastVisitedHome() {
        return lastVisitedHomes[0];
    }

    public void setLastVisitedHome(String lastVisitedHome) {
        if (lastVisitedHome == null || lastVisitedHome.equals(lastVisitedHomes[1])) {
            return;
        }

        lastVisitedHomes[0] = lastVisitedHomes[1];
        lastVisitedHomes[1] = lastVisitedHome;
    }
}
