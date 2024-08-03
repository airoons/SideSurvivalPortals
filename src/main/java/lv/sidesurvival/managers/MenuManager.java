package lv.sidesurvival.managers;

import lv.sidesurvival.SurvivalPortals;
import lv.sidesurvival.gui.*;
import lv.sidesurvival.objects.ClaimOwner;
import lv.sidesurvival.utils.Messages;
import org.bukkit.entity.Player;

import lv.sidesurvival.data.PortalData;
import lv.sidesurvival.objects.Portal;

public class MenuManager {

    private final SurvivalPortals plugin;
    private final PermissionManager permissionManager;

    public MenuManager(SurvivalPortals plugin) {
        this.plugin = plugin;
        permissionManager = this.plugin.getPermissionManager();
    }

    public boolean portalPermCheck(Player player, Portal portal) {
        if (!PortalData.portalExists(portal)) {
            player.sendMessage(Messages.get(player, "chat.portal-doesnt-exist-anymore"));
            plugin.handleClose.remove(player);
            player.closeInventory();
            return false;
        }

        ClaimOwner owner = ClaimManager.get().getOwnerById(portal.getOwner());
        if (owner == null || !permissionManager.canEditPortal(player, owner, portal.getPos1())) {
            player.sendMessage(Messages.get(player, "chat.no-edit-permission"));
            return false;
        }
        
        return true;
    }

    public void openMain(Player player, Portal portal) {
        if (portal == null || portal.getId() == null) {
            player.sendMessage(Messages.get(player, "chat.portal-doesnt-exist-anymore"));
            plugin.handleClose.remove(player);
            player.closeInventory();
            return;
        }

        MainMenu gui = new MainMenu(portal);
        gui.open(player, portal);
    }

    public void openPrivate(Player player) {
        PrivatePortalsMenu gui = new PrivatePortalsMenu();
        gui.open(player);
    }

    public void openPublic(Player player) {
        PublicPortalsMenu gui = new PublicPortalsMenu();
        gui.open(player);
    }

    public void openEditPortal(Player player, Portal portal) {
        if (!portalPermCheck(player, portal))
            return;

        EditPortalMenu gui = new EditPortalMenu(portal);
        gui.open(player, portal);
    }

    public void openEditPortalIcon(Player player, Portal portal) {
        if (!portalPermCheck(player, portal))
            return;

        plugin.handleClose.remove(player);

        EditPortalIcon gui = new EditPortalIcon(portal);
        gui.open(player, portal);
    }

    public void openEditPortalColor(Player player, Portal portal) {
        if (!portalPermCheck(player, portal))
            return;

        plugin.handleClose.remove(player);

        EditPortalColor gui = new EditPortalColor(portal);
        gui.open(player, portal);
    }

    public void openEditPortalAccess(Player player, Portal portal) {
        if (!portalPermCheck(player, portal))
            return;

        plugin.handleClose.remove(player);

        EditPortalAccess gui = new EditPortalAccess(portal);
        gui.open(player, portal);
    }

    public void openPortalGroupAccess(Player player, Portal portal) {
        if (!portalPermCheck(player, portal))
            return;
        
        plugin.handleClose.remove(player);

        PortalAccessGroups gui = new PortalAccessGroups(portal);
        gui.open(player, portal);
    }

    public void openPortalPlayerAccess(Player player, Portal portal) {
        if (!portalPermCheck(player, portal))
            return;

        plugin.handleClose.remove(player);

        PortalAccessPlayers gui = new PortalAccessPlayers(portal);
        gui.open(player, portal);
    }
}