package lv.theironminerlv.sidesurvivalportals.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.PermissionManager;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import me.angeschossen.lands.api.events.ChunkDeleteEvent;
import me.angeschossen.lands.api.integration.LandsIntegration;

public class PortalBreakListener implements Listener
{
    private SideSurvivalPortals plugin;
    private static PortalManager portalManager;
    private static PermissionManager permissionManager;
    private static LandsIntegration landsAPI;

    public PortalBreakListener(SideSurvivalPortals plugin) {
        this.plugin = plugin;
        portalManager = this.plugin.getPortalManager();
        permissionManager = this.plugin.getPermissionManager();
        landsAPI = this.plugin.getLandsAPI();
    }

    @EventHandler
    public void onPortalBlockBreak(BlockBreakEvent event)
    {
        if (event.getBlock().getType() == Material.PURPLE_STAINED_GLASS_PANE) {
            if (portalManager.isPortalAt(event.getBlock().getLocation())) {
                event.setCancelled(true);
                
                if (!permissionManager.canEditPortal(event.getPlayer(), landsAPI.getLand(event.getBlock().getLocation())))
                    return;

                Portal portal = portalManager.getPortalAt(event.getBlock().getLocation());
                portalManager.remove(portal);
            }
        }
    }

    @EventHandler
    public void onChunkDelete(ChunkDeleteEvent event)
    {
        portalManager.recheckPortals(event.getLand());
    }
}
