package lv.theironminerlv.sidesurvivalportals.gui;

import java.util.List;
import java.util.UUID;

import lv.sidesurvival.api.SurvivalCoreAPI;

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
import lv.theironminerlv.sidesurvivalportals.SurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.Messages;

public class PortalAccessPlayers implements InventoryProvider {

    private static SurvivalPortals plugin = SurvivalPortals.getInstance();
    private InventoryManager invManager = plugin.getInvManager();
    private PortalManager portalManager = plugin.getPortalManager();
    private SmartInventory inventory;
    private Portal portal;

    public PortalAccessPlayers(Portal portal) {
        this.portal = portal;
    }

    private void load() {
        this.inventory = SmartInventory.builder()
            .manager(invManager)
            .provider(new PortalAccessPlayers(portal))
            .size(4, 9)
            .title(Messages.get("gui.portal-settings.access-menu.access-players.gui-title"))
            .build();
    }

    public void open(Player player, Portal portal) {
        this.load();
        player.closeInventory();
        this.inventory.open(player);
        plugin.handleClose.add(player);
    }

    public void open(Player player, Portal portal, int page) {
        this.load();
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
            itemMeta.setDisplayName(Messages.getParam("gui.portal-settings.access-menu.access-players.item-names.player", "{1}", SurvivalCoreAPI.getNickFromUUIDString(uuid)));
            itemMeta.setLore(Messages.getList("gui.portal-settings.access-menu.access-players.item-lores.player"));
            item.setItemMeta(itemMeta);

            items[i] = ClickableItem.of(item, 
                e -> removePlayerAccess(player, portal, pagination.getPage(), uuid));
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(27);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        contents.set(3, 2, ClickableItem.of(MenuItems.prevPage,
            e -> open(player, portal, pagination.previous().getPage())));
        contents.set(3, 6, ClickableItem.of(MenuItems.nextPage,
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