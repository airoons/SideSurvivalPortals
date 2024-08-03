package lv.sidesurvival.gui;

import java.util.ArrayList;
import java.util.List;

import lv.sidesurvival.SurvivalPortals;
import lv.sidesurvival.managers.GroupManager;
import lv.sidesurvival.managers.PortalManager;
import lv.sidesurvival.objects.Group;
import lv.sidesurvival.objects.Portal;
import lv.sidesurvival.utils.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;

public class PortalAccessGroups implements InventoryProvider {

    private static final SurvivalPortals plugin = SurvivalPortals.getInstance();
    private final InventoryManager invManager = plugin.getInvManager();
    private final PortalManager portalManager = plugin.getPortalManager();
    private SmartInventory inventory;
    private final Portal portal;

    public PortalAccessGroups(Portal portal) {
        this.portal = portal;
    }

    private void load(Player player) {
        this.inventory = SmartInventory.builder()
            .manager(invManager)
            .provider(new PortalAccessGroups(portal))
            .size(4, 9)
            .title(Messages.get(player, "gui.portal-settings.access-menu.access-groups.gui-title"))
            .build();
    }

    public void open(Player player, Portal portal) {
        this.load(player);
        player.closeInventory();
        this.inventory.open(player);
        plugin.handleClose.add(player);
    }

    public void open(Player player, Portal portal, int page) {
        this.load(player);
        this.inventory.open(player, page);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        ItemStack item;
        ItemMeta itemMeta;
        Pagination pagination = contents.pagination();

        List<String> allowedGroups = portal.getAllowedGroups();
        List<Group> groups = new ArrayList<>();

        for (String groupId : allowedGroups) {
            Group group = GroupManager.get().getById(groupId, true);
            if (group == null) {
                portalManager.removeGroupAccess(portal, groupId);
                continue;
            }

            groups.add(group);
        }

        ClickableItem[] items = new ClickableItem[groups.size()];

        for (int i = 0; i < items.length; i++) {
            Group group = groups.get(i);
            
            item = new ItemStack(Material.BOOK);
            itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(Messages.getParam(player, "gui.portal-settings.access-menu.access-groups.item-names.group", "{1}", group.getName(null)));
            itemMeta.setLore(Messages.getList(player, "gui.portal-settings.access-menu.access-groups.item-lores.group"));
            item.setItemMeta(itemMeta);

            items[i] = ClickableItem.of(item, 
                e -> removeGroupAccess(player, portal, pagination.getPage(), group.getId()));
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(27);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        contents.set(3, 2, ClickableItem.of(MenuItems.prevPage(player),
            e -> open(player, portal, pagination.previous().getPage())));
        contents.set(3, 6, ClickableItem.of(MenuItems.nextPage(player),
            e -> open(player, portal, pagination.next().getPage())));

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void removeGroupAccess(Player player, Portal portal, int page, String groupId) {
        plugin.handleClose.remove(player);
        portalManager.removeGroupAccess(portal, groupId);
        open(player, portal, page);
    }
}