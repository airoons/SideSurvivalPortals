package lv.sidesurvival.gui;

import lv.sidesurvival.SurvivalPortals;
import lv.sidesurvival.managers.ClaimManager;
import lv.sidesurvival.managers.PermissionManager;
import lv.sidesurvival.managers.PortalManager;
import lv.sidesurvival.objects.ClaimOwner;
import lv.sidesurvival.objects.Portal;
import lv.sidesurvival.utils.Messages;
import lv.sidesurvival.utils.SkullCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        this.inventory = SmartInventory.builder()
                .manager(invManager)
                .provider(new MainMenu(portal))
                .size(1, 9)
                .title(Messages.get(player, "gui.main-menu.gui-title"))
                .build();
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

        ClaimOwner owner = ClaimManager.get().getOwnerById(portal.getOwner());
        if (owner == null || !permissionManager.canEditPortal(player, owner, portal.getPos1())) {
            offset = 1;
        }
        contents.set(0, 1 + offset, ClickableItem.of(MenuItems.goSpawn(player), e -> {
            portalManager.teleportToSpawn(player, e.isRightClick());
        }));

        item = SkullCreator.itemFromUuid(player.getUniqueId());
        meta = item.getItemMeta();
        meta.setDisplayName(Messages.get(player, "gui.main-menu.item-names.private-portals"));
        meta.setLore(Messages.getList(player, "gui.main-menu.item-lores.private-portals"));
        item.setItemMeta(meta);
        contents.set(0, 3 + offset, ClickableItem.of(item, e -> plugin.getMenuManager().openPrivate(player)));

        contents.set(0, 5 + offset,
                ClickableItem.of(MenuItems.pubPortals(player), e -> plugin.getMenuManager().openPublic(player)));

        if (offset == 0) {
            contents.set(0, 7, ClickableItem.of(MenuItems.portalSettings(player),
                    e -> plugin.getMenuManager().openEditPortal(player, portal)));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}