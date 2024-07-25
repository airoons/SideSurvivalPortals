package lv.theironminerlv.sidesurvivalportals.listeners;

import lv.sidesurvival.events.ClaimOwner.ClaimOwnerUnclaimEvent;
import lv.sidesurvival.events.GroupDisbandEvent;
import lv.sidesurvival.managers.ClaimManager;
import lv.sidesurvival.objects.Claim;
import lv.sidesurvival.objects.ClaimOwner;
import lv.theironminerlv.sidesurvivalportals.SurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.PermissionManager;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PortalBreakListener implements Listener {

    private static PortalManager portalManager;
    private static PermissionManager permissionManager;

    public PortalBreakListener(SurvivalPortals plugin) {
        portalManager = plugin.getPortalManager();
        permissionManager = plugin.getPermissionManager();
    }

    @EventHandler
    public void onPortalBlockBreak(BlockBreakEvent event) {
        if (portalManager.allowedBlocks.contains(event.getBlock().getType())) {
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
