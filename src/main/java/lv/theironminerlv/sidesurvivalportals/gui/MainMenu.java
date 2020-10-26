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
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.data.PortalData;
import lv.theironminerlv.sidesurvivalportals.managers.PermissionManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.ConvertUtils;

public class MainMenu implements InventoryProvider
{
    private static SideSurvivalPortals plugin = SideSurvivalPortals.getInstance();
    private InventoryManager invManager = plugin.getInvManager();
    private PermissionManager permissionManager = plugin.getPermissionManager();
    private SmartInventory inventory;
    private Portal portal;

    public MainMenu(Portal portal) {
        this.portal = portal;
    }

    private void load(Portal portal) {
        this.inventory = SmartInventory.builder()
            .manager(invManager)
            .id("portal")
            .provider(new MainMenu(portal))
            .size(3, 9)
            .title("Portāls")
            .build();
    }

    public void open(Player player, Portal portal) {
        this.portal = portal;
        this.load(portal);
        this.inventory.open(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        ItemStack item;
        ItemMeta meta;
        int offset = 0;

        contents.fillBorders(ClickableItem.empty(MenuItems.grayPane));
        contents.set(0, 1, ClickableItem.empty(MenuItems.blackPane));
        contents.set(0, 4, ClickableItem.empty(MenuItems.lightGrayPane));
        contents.set(0, 7, ClickableItem.empty(MenuItems.blackPane));
        contents.set(1, 0, ClickableItem.empty(MenuItems.blackPane));
        contents.set(1, 8, ClickableItem.empty(MenuItems.blackPane));
        contents.set(2, 1, ClickableItem.empty(MenuItems.blackPane));
        contents.set(2, 4, ClickableItem.empty(MenuItems.lightGrayPane));
        contents.set(2, 7, ClickableItem.empty(MenuItems.blackPane));

        if (!permissionManager.canEditPortal(player, portal.getLand())) {
            offset = 1;
        }

        if (player.getWorld().getEnvironment().equals(Environment.NETHER))
            contents.set(1, 1 + offset, ClickableItem.of(MenuItems.goSpawn, e -> teleportToSpawn(player)));
        else
            contents.set(1, 1 + offset, ClickableItem.of(MenuItems.goNetherSpawn, e -> teleportToNetherSpawn(player)));
        

        item = SkullCreator.itemFromUuid(player.getUniqueId());
        meta = item.getItemMeta();
        meta.setDisplayName(ConvertUtils.color("&5&lPrivātie portāli"));
        item.setItemMeta(meta);
        contents.set(1, 3 + offset, ClickableItem.of(item, e -> plugin.getMenuManager().openPrivate(player)));

        contents.set(1, 5 + offset, ClickableItem.of(MenuItems.pubPortals, e -> plugin.getMenuManager().openPublic(player)));;

        if (offset == 0) {    
            contents.set(1, 7, ClickableItem.of(MenuItems.portalSettings, e -> plugin.getMenuManager().openEditPortal(player, portal)));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void teleportToSpawn(Player player) {
        Location loc = PortalData.getSpawnLocation();
        player.teleport(loc);
    }

    public void teleportToNetherSpawn(Player player) {
        Location loc = PortalData.getNetherSpawnLocation();
        player.teleport(loc);
    }
}