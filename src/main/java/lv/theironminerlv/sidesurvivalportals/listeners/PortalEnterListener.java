package lv.theironminerlv.sidesurvivalportals.listeners;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.MenuManager;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;
import lv.theironminerlv.sidesurvivalportals.utils.ConvertUtils;

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

        if (to != null && portalManager.isPortalAt(from) && !portalManager.isPortalAt(to)) {
            BukkitTask task = portalManager.tasks.get(event.getPlayer().getUniqueId());
            if (task != null) {
                task.cancel();
                portalManager.tasks.remove(event.getPlayer().getUniqueId());
                portalManager.fakePortalBlocks(event.getPlayer(), portalManager.getPortalAt(from), false);
                // event.getPlayer().removePotionEffect(PotionEffectType.CONFUSION);
            }
            return;
        }

        ProtectedRegion toRegion = portalManager.getRegionAt(to);
        if ((toRegion == null) || (toRegion.equals(portalManager.getRegionAt(from))))
            return;

        if (!portalManager.isPortalAt(to))
            return;

        menuManager.openMain(event.getPlayer(), portalManager.getPortalAt(to));
        //Bukkit.broadcastMessage("[debug yes] " + event.getPlayer().getName() + " entered region " + toRegion.getId());
    }
}