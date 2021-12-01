package lv.theironminerlv.sidesurvivalportals.listeners;

import lv.sidesurvival.events.ClaimOwner.ClaimOwnerUnclaimEvent;
import lv.sidesurvival.events.GroupDisbandEvent;
import lv.sidesurvival.managers.ClaimManager;
import lv.sidesurvival.objects.Claim;
import lv.sidesurvival.objects.ClaimOwner;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import lv.theironminerlv.sidesurvivalportals.SurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.PermissionManager;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;

public class PortalBreakListener implements Listener {

    private SurvivalPortals plugin;
    private static PortalManager portalManager;
    private static PermissionManager permissionManager;

    public PortalBreakListener(SurvivalPortals plugin) {
        this.plugin = plugin;
        portalManager = this.plugin.getPortalManager();
        permissionManager = this.plugin.getPermissionManager();
    }

    @EventHandler
    public void onPortalBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.PURPLE_STAINED_GLASS_PANE) {
            if (portalManager.isPortalAt(event.getBlock().getLocation())) {
                event.setCancelled(true);

                ClaimOwner owner = ClaimManager.get().getOwnerAt(new Claim(event.getBlock()));
                if (!permissionManager.canEditPortal(event.getPlayer(), owner, event.getBlock().getLocation()))
                    return;

                Portal portal = portalManager.getPortalAt(event.getBlock().getLocation());
                portalManager.remove(portal);
            }
        }
    }

    @EventHandler
    public void onChunkDelete(ClaimOwnerUnclaimEvent event) {
        portalManager.recheckPortals(event.getOwner());
    }

    @EventHandler
    public void onGroupDisband(GroupDisbandEvent event) {
        portalManager.recheckPortals(event.getGroup());
    }
}
