package lv.theironminerlv.sidesurvivalportals.gui;

import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.dbassett.skullcreator.SkullCreator;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.SmartInvsPlugin;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.data.PortalData;
import lv.theironminerlv.sidesurvivalportals.managers.PermissionManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.ConvertUtils;

public class EditPortalMenu implements InventoryProvider
{
    private static SideSurvivalPortals plugin = SideSurvivalPortals.getInstance();
    private InventoryManager invManager = plugin.getInvManager();
    private PermissionManager permissionManager = plugin.getPermissionManager();
    private SmartInventory inventory;
    private Portal portal;

    public EditPortalMenu(Portal portal) {
        this.portal = portal;
    }

    private void load(Portal portal) {
        this.inventory = SmartInventory.builder()
            .manager(invManager).
            id("portal")
            .provider(new EditPortalMenu(portal))
            .size(3, 9)
            .title("Portāla iestatījumi")
            .build();
    }

    public void open(Player player, Portal portal) {
        this.portal = portal;
        this.load(portal);
        player.closeInventory();
        this.inventory.open(player);
        plugin.handleClose.add(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        ItemStack item;
        ItemMeta meta;

        contents.fillBorders(ClickableItem.empty(MenuItems.grayPane));
        contents.set(0, 1, ClickableItem.empty(MenuItems.blackPane));
        contents.set(0, 4, ClickableItem.empty(MenuItems.lightGrayPane));
        contents.set(0, 7, ClickableItem.empty(MenuItems.blackPane));
        contents.set(1, 0, ClickableItem.empty(MenuItems.blackPane));
        contents.set(1, 8, ClickableItem.empty(MenuItems.blackPane));
        contents.set(2, 1, ClickableItem.empty(MenuItems.blackPane));
        contents.set(2, 4, ClickableItem.empty(MenuItems.lightGrayPane));
        contents.set(2, 7, ClickableItem.empty(MenuItems.blackPane));

        contents.set(1, 2, ClickableItem.of(MenuItems.editPortalAccess, e -> editPortalAccess(player, portal)));
        contents.set(1, 4, ClickableItem.of(MenuItems.editPortalDescr, e -> editPortalDescr(player, portal)));

        item = portal.getIcon().clone();
        meta = item.getItemMeta();
        meta.setDisplayName(ConvertUtils.color("&a&lMainīt ikonu"));
        item.setItemMeta(meta);
        contents.set(1, 6, ClickableItem.of(item, e -> plugin.getMenuManager().openEditPortalIcon(player, portal)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void editPortalAccess(Player player, Portal portal) {
        player.sendMessage("Atver krutu menu");
    }

    public void editPortalDescr(Player player, Portal portal) {
        player.closeInventory();
        player.sendMessage(ConvertUtils.color("&7Raksti komandu &f/p apraksts <jaunais apraksts>&7, lai mainītu portāla aprakstu!"));
    }

    public Portal getPortal() {
        return portal;
    }
}