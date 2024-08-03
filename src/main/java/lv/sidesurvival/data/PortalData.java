package lv.sidesurvival.data;

import lv.sidesurvival.SurvivalPortals;
import lv.sidesurvival.managers.DataManager;
import lv.sidesurvival.objects.Portal;
import lv.sidesurvival.utils.LocationSerialization;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PortalData {

    private static DataManager dataManager;
    public static Map<String, Portal> CACHED_PORTALS = new ConcurrentHashMap<>();

    private static Location worldSpawn;
    private static Location netherSpawn;

    public PortalData(SurvivalPortals plugin) {
        dataManager = plugin.getDataManager();

        worldSpawn = LocationSerialization.getLocationFromString(plugin.getConfiguration().getString("teleportLocs.spawn"));
        netherSpawn = LocationSerialization.getLocationFromString(plugin.getConfiguration().getString("teleportLocs.netherspawn"));
    }

    public static void addPortal(Portal portal, boolean save) {
        CACHED_PORTALS.put(portal.getId(), portal);
        if (save)
            dataManager.save(portal);
    }

    public static void updatePortal(Portal portal) {
        CACHED_PORTALS.put(portal.getId(), portal);
    }

    public static void removePortalById(String id, boolean db) {
        Portal portal = CACHED_PORTALS.remove(id);
        if (portal != null && db) {
            dataManager.delete(portal);
        }
    }

    public static void removePortal(Portal portal, boolean db) {
        CACHED_PORTALS.remove(portal.getId());
        if (db) {
            dataManager.delete(portal);
        }
    }

    public static Map<String, Portal> getByWorld(World world) {
        return CACHED_PORTALS.entrySet().stream()
        .filter(map -> map.getValue().getWorld().equals(world))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<String, Portal> getByOwner(String ownerId) {
        return CACHED_PORTALS.entrySet().stream()
        .filter(map -> map.getValue().getOwner().equals(ownerId))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Location getSpawnLocation() {
        return worldSpawn;
    }

    public static Location getNetherSpawnLocation() {
        return netherSpawn;
    }

	public static Map<String, Portal> getAccessablePortalsByGroup(String groupId) {
		return CACHED_PORTALS.entrySet().stream()
        .filter(map -> map.getValue().getAllowedGroups().contains(groupId))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    public static Map<String, Portal> getAccessablePortalsByPlayer(String uuid) {
		return CACHED_PORTALS.entrySet().stream()
        .filter(map -> map.getValue().getAllowedPlayers().contains(uuid))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public static Map<String, Portal> getAllPublic() {
		return CACHED_PORTALS.entrySet().stream()
        .filter(map -> map.getValue().getIsPublic())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    public static boolean portalExists(Portal portal) {
        return CACHED_PORTALS.containsKey(portal.getId());
    }

    public static boolean portalExists(String id) {
        return CACHED_PORTALS.containsKey(id);
    }
}