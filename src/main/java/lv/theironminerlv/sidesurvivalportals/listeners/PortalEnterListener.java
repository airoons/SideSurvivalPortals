package lv.theironminerlv.sidesurvivalportals.listeners;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.MenuManager;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;

public class PortalEnterListener implements Listener
{
    private SideSurvivalPortals plugin;
    private static PortalManager portalManager;
    private static MenuManager menuManager;

    public PortalEnterListener(SideSurvivalPortals plugin) {
        this.plugin = plugin;
        portalManager = this.plugin.getPortalManager();
        menuManager = this.plugin.getMenuManager();
    }

    @EventHandler
    public void onPortalEvent(PlayerMoveEvent event)
    {
        Location to = event.getTo(), from = event.getFrom();
        if (to != null && to.equals(from))
            return;

        ProtectedRegion toRegion = portalManager.getRegionAt(event.getTo());
        if ((toRegion == null) || (toRegion.equals(portalManager.getRegionAt(event.getFrom()))))
            return;

        if (!portalManager.isPortalAt(event.getTo()))
            return;

        menuManager.openMain(event.getPlayer(), portalManager.getPortalAt(event.getTo()));
        //Bukkit.broadcastMessage("[debug yes] " + event.getPlayer().getName() + " entered region " + toRegion.getId());
    }
}