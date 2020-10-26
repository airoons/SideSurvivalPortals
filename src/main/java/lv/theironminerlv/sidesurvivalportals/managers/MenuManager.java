package lv.theironminerlv.sidesurvivalportals.managers;

import org.bukkit.entity.Player;

import fr.minuskube.inv.SmartInventory;
import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.gui.EditPortalIcon;
import lv.theironminerlv.sidesurvivalportals.gui.EditPortalMenu;
import lv.theironminerlv.sidesurvivalportals.gui.MainMenu;
import lv.theironminerlv.sidesurvivalportals.gui.PrivatePortalsMenu;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.ConvertUtils;

public class MenuManager
{
    private SideSurvivalPortals plugin;
    private PermissionManager permissionManager;

    public MenuManager(SideSurvivalPortals plugin) {
        this.plugin = plugin;
        permissionManager = this.plugin.getPermissionManager();
    }

    public void openMain(Player player, Portal portal) {
        MainMenu gui = new MainMenu(portal);
        gui.open(player, portal);
    }

    public void openPrivate(Player player) {
        PrivatePortalsMenu gui = new PrivatePortalsMenu();
        gui.open(player, true);
    }

    public void openEditPortal(Player player, Portal portal) {
        if (portal == null) {
            player.sendMessage(ConvertUtils.color("&cPortāls vairs neeksistē!"));
            return;
        }

        if (!permissionManager.canEditPortal(player, portal.getLand())) {
            player.sendMessage(ConvertUtils.color("&cTev nav atļauts labot portālu!"));
            return;
        }

        EditPortalMenu gui = new EditPortalMenu(portal);
        gui.open(player, portal);
    }

    public void openEditPortalIcon(Player player, Portal portal) {
        if (portal == null) {
            player.sendMessage(ConvertUtils.color("&cPortāls vairs neeksistē!"));
            return;
        }

        if (!permissionManager.canEditPortal(player, portal.getLand())) {
            player.sendMessage(ConvertUtils.color("&cTev nav atļauts labot portālu!"));
            return;
        }

        EditPortalIcon gui = new EditPortalIcon(portal);
        gui.open(player, portal);
    }
}