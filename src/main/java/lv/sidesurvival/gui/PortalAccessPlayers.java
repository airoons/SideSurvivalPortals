package lv.sidesurvival.gui;

import java.util.List;
import java.util.UUID;

import lv.sidesurvival.SurvivalPortals;
import lv.sidesurvival.api.SurvivalCoreAPI;

import lv.sidesurvival.managers.PortalManager;
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
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;

public class PortalAccessPlayers implements InventoryProvider {

    private static final SurvivalPortals plugin = SurvivalPortals.getInstance();
    private final InventoryManager invManager = plugin.getInvManager();
    private final PortalManager portalManager = plugin.getPortalManager();
    private SmartInventory inventory;
    private final Portal portal;

    public PortalAccessPlayers(Portal portal) {
        this.portal = portal;
    }

    private void load(Player player) {
        this.inventory = SmartInventory.builder()
            .manager(invManager)
            .provider(new PortalAccessPlayers(portal))
            .size(4, 9)
            .title(Messages.get(player, "gui.portal-settings.access-menu.access-players.gui-title"))
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

        List<String> allowedPlayers = portal.getAllowedPlayers();

        ClickableItem[] items = new ClickableItem[allowedPlayers.size()];

        for(int i = 0; i < items.length; i++) {
            item = SkullCreator.itemFromUuid(UUID.fromString(allowedPlayers.get(i)));
            String uuid = allowedPlayers.get(i);
            itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(Messages.getParam(
                    player, "gui.portal-settings.access-menu.access-players.item-names.player",
                    "{1}", SurvivalCoreAPI.getFullNickFromUUIDString(uuid)
            ));
            itemMeta.setLore(Messages.getList(player, "gui.portal-settings.access-menu.access-players.item-lores.player"));
            item.setItemMeta(itemMeta);

            items[i] = ClickableItem.of(item, 
                e -> removePlayerAccess(player, portal, pagination.getPage(), uuid));
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

    public void removePlayerAccess(Player player, Portal portal, int page, String uuid) {
        plugin.handleClose.remove(player);
        portalManager.removePlayerAccess(portal, uuid);
        
        open(player, portal, page);
    }
}