package lv.sidesurvival.gui;

import lv.sidesurvival.SurvivalPortals;
import lv.sidesurvival.managers.ClaimManager;
import lv.sidesurvival.managers.PermissionManager;
import lv.sidesurvival.managers.PortalManager;
import lv.sidesurvival.objects.ClaimOwner;
import lv.sidesurvival.objects.Portal;
import lv.sidesurvival.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.dbassett.skullcreator.SkullCreator;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

public class MainMenu implements InventoryProvider {

    private static final SurvivalPortals plugin = SurvivalPortals.getInstance();
    private final InventoryManager invManager = plugin.getInvManager();
    private final PermissionManager permissionManager = plugin.getPermissionManager();
    private final PortalManager portalManager = plugin.getPortalManager();
    private SmartInventory inventory;
    private Portal portal;

    public MainMenu(Portal portal) {
        this.portal = portal;
    }

    private void load(Player player, Portal portal) {
        this.inventory = SmartInventory.builder().manager(invManager).provider(new MainMenu(portal)).size(3, 9)
                .title(Messages.get(player, "gui.main-menu.gui-title")).build();
    }

    public void open(Player player, Portal portal) {
        this.portal = portal;
        this.load(player, portal);
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
        if (!player.hasPermission("group.bedrock"))
            contents.set(1, 0, ClickableItem.empty(MenuItems.blackPane));
        contents.set(1, 8, ClickableItem.empty(MenuItems.blackPane));
        contents.set(2, 1, ClickableItem.empty(MenuItems.blackPane));
        contents.set(2, 4, ClickableItem.empty(MenuItems.lightGrayPane));
        contents.set(2, 7, ClickableItem.empty(MenuItems.blackPane));

        ClaimOwner owner = ClaimManager.get().getOwnerById(portal.getOwner());
        if (owner == null || !permissionManager.canEditPortal(player, owner, portal.getPos1())) {
            offset = 1;
        }
        if (!player.hasPermission("group.bedrock")) {
            contents.set(1, 1 + offset, ClickableItem.of(MenuItems.goSpawn(player), e -> {
                if (e.isRightClick())
                    portalManager.teleportToSpawn(player, true);
                else
                    portalManager.teleportToSpawn(player, false);
            }));
        } else {
            contents.set(1, offset, ClickableItem.of(MenuItems.goNetherBedrock(player), e -> {
                portalManager.teleportToSpawn(player, true);
            }));
            contents.set(1, 1 + offset, ClickableItem.of(MenuItems.goSpawnBedrock(player), e -> {
                portalManager.teleportToSpawn(player, false);
            }));
        }

        item = SkullCreator.itemFromUuid(player.getUniqueId());
        meta = item.getItemMeta();
        meta.setDisplayName(Messages.get(player, "gui.main-menu.item-names.private-portals"));
        meta.setLore(Messages.getList(player, "gui.main-menu.item-lores.private-portals"));
        item.setItemMeta(meta);
        contents.set(1, 3 + offset, ClickableItem.of(item, e -> plugin.getMenuManager().openPrivate(player)));

        contents.set(1, 5 + offset,
                ClickableItem.of(MenuItems.pubPortals(player), e -> plugin.getMenuManager().openPublic(player)));
        ;

        if (offset == 0) {
            contents.set(1, 7, ClickableItem.of(MenuItems.portalSettings(player),
                    e -> plugin.getMenuManager().openEditPortal(player, portal)));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}