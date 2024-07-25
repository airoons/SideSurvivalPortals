package lv.theironminerlv.sidesurvivalportals.listeners;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

import lv.theironminerlv.sidesurvivalportals.SurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.MenuManager;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;

public class PortalEnterListener implements Listener {

    private static PortalManager portalManager;
    private static MenuManager menuManager;

    public PortalEnterListener(SurvivalPortals plugin) {
        portalManager = plugin.getPortalManager();
        menuManager = plugin.getMenuManager();
    }

    @EventHandler
    public void onPortalEvent(PlayerMoveEvent event) {
        Location to = event.getTo(), from = event.getFrom();
        if (to.equals(from))
            return;

        if (portalManager.isPortalAt(from) && !portalManager.isPortalAt(to)) {
            BukkitTask task = portalManager.tasks.get(event.getPlayer().getUniqueId());
            if (task != null) {
                task.cancel();
                portalManager.tasks.remove(event.getPlayer().getUniqueId());
                portalManager.fakePortalBlocks(event.getPlayer(), portalManager.getPortalAt(from), false);
                // event.getPlayer().removePotionEffect(PotionEffectType.CONFUSION);
            }
            return;
        }

        ProtectedRegion toRegion = portalManager.getPortalRegionAt(to);
        if ((toRegion == null) || (toRegion.equals(portalManager.getPortalRegionAt(from))))
            return;

        if (!portalManager.isPortalAt(to))
            return;

        menuManager.openMain(event.getPlayer(), portalManager.getPortalAt(to));
    }
}