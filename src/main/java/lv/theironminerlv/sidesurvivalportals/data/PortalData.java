package lv.theironminerlv.sidesurvivalportals.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.DataManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;

public class PortalData
{
    private SideSurvivalPortals plugin;
    private static DataManager dataManager;
    public static Map<String, Portal> CACHED_PORTALS = new ConcurrentHashMap<>();

    public PortalData(SideSurvivalPortals plugin) {
        this.plugin = plugin;
        dataManager = plugin.getDataManager();
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
}