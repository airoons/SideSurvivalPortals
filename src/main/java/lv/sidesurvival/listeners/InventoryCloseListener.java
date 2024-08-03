package lv.sidesurvival.listeners;

import lv.sidesurvival.SurvivalPortals;
import lv.sidesurvival.managers.MenuManager;
import lv.sidesurvival.managers.PortalManager;
import lv.sidesurvival.objects.Portal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import lv.sidesurvival.gui.EditPortalAccess;
import lv.sidesurvival.gui.EditPortalIcon;
import lv.sidesurvival.gui.EditPortalMenu;
import lv.sidesurvival.gui.PortalAccessGroups;
import lv.sidesurvival.gui.PortalAccessPlayers;
import lv.sidesurvival.gui.PrivatePortalsMenu;
import lv.sidesurvival.gui.PublicPortalsMenu;

public class InventoryCloseListener implements Listener {

    private final SurvivalPortals plugin;
    private static PortalManager portalManager;
    private static InventoryManager invManager;
    private static MenuManager menuManager;

    public InventoryCloseListener(SurvivalPortals plugin) {
        this.plugin = plugin;
        portalManager = this.plugin.getPortalManager();
        invManager = this.plugin.getInvManager();
        menuManager = this.plugin.getMenuManager();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPortalBlockBreak(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (invManager.getInventory(player).isPresent()) {
            SmartInventory inv = invManager.getInventory(player).get();

            if ((inv.getProvider() instanceof PrivatePortalsMenu
            || inv.getProvider() instanceof PublicPortalsMenu
            || inv.getProvider() instanceof EditPortalMenu)
            && plugin.handleClose.contains(player)) {
                Portal portal = portalManager.getPortalAt(player.getLocation());

                if (portal != null) {
                    new BukkitRunnable(){
                        public void run() {
                            plugin.handleClose.remove(player);
                            menuManager.openMain(player, portal);
                        }
                    }.runTaskLater(plugin, 1);
                }
            } else if ((inv.getProvider() instanceof EditPortalIcon || inv.getProvider() instanceof EditPortalAccess) && plugin.handleClose.contains(player)) {
                Portal portal = portalManager.getPortalAt(player.getLocation());

                if (portal != null) {
                    new BukkitRunnable(){
                        public void run() {
                            plugin.handleClose.remove(player);
                            menuManager.openEditPortal(player, portal);
                        }
                    }.runTaskLater(plugin, 1);
                }
            } else if ((inv.getProvider() instanceof PortalAccessGroups || inv.getProvider() instanceof PortalAccessPlayers) && plugin.handleClose.contains(player)) {
                Portal portal = portalManager.getPortalAt(player.getLocation());

                if (portal != null) {
                    new BukkitRunnable(){
                        public void run() {
                            plugin.handleClose.remove(player);
                            menuManager.openEditPortalAccess(player, portal);
                        }
                    }.runTaskLater(plugin, 1);
                }
            }
        }
    }
}