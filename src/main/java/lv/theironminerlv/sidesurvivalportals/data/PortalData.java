package lv.theironminerlv.sidesurvivalportals.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;

public class PortalData
{
    private SideSurvivalPortals plugin;
    public Map<String, Portal> CACHED_PORTALS = new ConcurrentHashMap<>();

    public PortalData(SideSurvivalPortals plugin) {
        this.plugin = plugin;
    }

    public void addPortal(Portal portal) {
        CACHED_PORTALS.put(portal.getId(), portal);
    }

    public void removePortal(Portal portal) {
        CACHED_PORTALS.remove(portal.getId());
    }
}