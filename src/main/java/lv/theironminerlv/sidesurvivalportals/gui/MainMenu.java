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
        this.inventory = SmartInventory.builder().manager(invManager).id("portal").provider(new MainMenu(portal))
                .size(3, 9).title("Portāls").build();
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

        if (player.getWorld().getEnvironment().equals(Environment.NETHER)) {
            item = MenuItems.miniGrass;
            meta = item.getItemMeta();
            meta.setDisplayName(ConvertUtils.color("&eDoties uz spawn"));
            item.setItemMeta(meta);

            contents.set(1, 1 + offset, ClickableItem.of(item, e -> teleportToSpawn(player)));
        } else {
            item = MenuItems.miniNetherrack;
            meta = item.getItemMeta();
            meta.setDisplayName(ConvertUtils.color("&eDoties uz Nether"));
            item.setItemMeta(meta);

            contents.set(1, 1 + offset, ClickableItem.of(item, e -> teleportToNetherSpawn(player)));
        }

        item = SkullCreator.itemFromUuid(player.getUniqueId());
        meta = item.getItemMeta();
        meta.setDisplayName(ConvertUtils.color("&5Privātie portāli"));
        item.setItemMeta(meta);

        contents.set(1, 3 + offset, ClickableItem.of(item, e -> plugin.getMenuManager().openPrivate(player)));

        item = MenuItems.miniGlobe;
        meta = item.getItemMeta();
        meta.setDisplayName(ConvertUtils.color("&ePubliskie portāli"));
        item.setItemMeta(meta);

        contents.set(1, 5 + offset, ClickableItem.of(item, e -> player.sendMessage(ChatColor.BOLD + "Dodas uz spawn")));

        if (offset == 0) {
            item = new ItemStack(Material.WRITABLE_BOOK);
            meta = item.getItemMeta();
            meta.setDisplayName(ConvertUtils.color("&fPortāla iestatījumi"));
            item.setItemMeta(meta);
    
            contents.set(1, 7, ClickableItem.of(item, e -> plugin.getMenuManager().openEditPortal(player, portal)));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    // @Override
    // public void update(Player player, InventoryContents contents) {
    // int state = contents.property("state", 0);
    // contents.setProperty("state", state + 1);

    // ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

    // if (state % 5 != 0) {
    // glass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
    // }

    // contents.fillBorders(ClickableItem.empty(glass));
    // }

    public void teleportToSpawn(Player player) {
        Location loc = PortalData.getSpawnLocation();
        player.teleport(loc);
    }

    public void teleportToNetherSpawn(Player player) {
        Location loc = PortalData.getNetherSpawnLocation();
        player.teleport(loc);
    }
}