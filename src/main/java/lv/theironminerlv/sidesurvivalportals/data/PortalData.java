package lv.theironminerlv.sidesurvivalportals.data;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.DataManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.LocationSerialization;
import me.angeschossen.lands.api.land.Land;

public class PortalData
{
    private SideSurvivalPortals plugin;
    private static DataManager dataManager;
    public static Map<String, Portal> CACHED_PORTALS = new ConcurrentHashMap<>();

    private static Location worldSpawn;
    private static Location netherSpawn;

    public PortalData(SideSurvivalPortals plugin) {
        this.plugin = plugin;
        dataManager = plugin.getDataManager();

        worldSpawn = LocationSerialization.getLocationFromString(plugin.getConfiguration().getString("teleportLocs.spawn"));
        netherSpawn = LocationSerialization.getLocationFromString(plugin.getConfiguration().getString("teleportLocs.netherspawn"));
    }

    public static void addPortal(Portal portal, boolean save) {
        CACHED_PORTALS.put(portal.getId(), portal);
        if (save)
            dataManager.save(portal);
    }

    public static void removePortal(Portal portal) {
        CACHED_PORTALS.remove(portal.getId());
        dataManager.delete(portal);
    }

    public static Map<String, Portal> getByWorld(World world) {
        return CACHED_PORTALS.entrySet().stream()
        .filter(map -> map.getValue().getWorld().equals(world))
        .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    public static Map<String, Portal> getByLand(Land land) {
        return CACHED_PORTALS.entrySet().stream()
        .filter(map -> map.getValue().getLand().equals(land))
        .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    public static Location getSpawnLocation() {
        return worldSpawn;
    }

    public static Location getNetherSpawnLocation() {
        return netherSpawn;
    }

	public static Map<String, Portal> getAccessablePortalsByLand(Land land) {
		return CACHED_PORTALS.entrySet().stream()
        .filter(map -> map.getValue().getAllowedLands().contains(land.getId()))
        .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }
    
    public static Map<String, Portal> getAccessablePortalsByPlayer(UUID uuid) {
		return CACHED_PORTALS.entrySet().stream()
        .filter(map -> map.getValue().getAllowedPlayers().contains(uuid))
        .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
	}

	public static Map<String, Portal> getAllPublic() {
		return CACHED_PORTALS.entrySet().stream()
        .filter(map -> map.getValue().getIsPublic())
        .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }
    
    public static boolean portalExists(Portal portal) {
        return CACHED_PORTALS.containsKey(portal.getId());
    }

    public static boolean portalExists(String id) {
        return CACHED_PORTALS.containsKey(id);
    }
}