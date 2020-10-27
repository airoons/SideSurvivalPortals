package lv.theironminerlv.sidesurvivalportals.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

import org.bukkit.OfflinePlayer;
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
import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.ConvertUtils;
import lv.theironminerlv.sidesurvivalportals.utils.Messages;
public class PortalAccessPlayers implements InventoryProvider {
    private static SideSurvivalPortals plugin = SideSurvivalPortals.getInstance();
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

        List<UUID> allowedPlayers = portal.getAllowedPlayers();
        List<OfflinePlayer> players = new ArrayList<OfflinePlayer>();

        for (UUID uuid : allowedPlayers) {
            players.add(Bukkit.getOfflinePlayer(uuid));
        }

        int landAmount = players.size();

        ClickableItem[] items = new ClickableItem[landAmount];

        for(int i = 0; i < items.length; i++) {
            OfflinePlayer loopPlayer = players.get(i);
            
            item = SkullCreator.itemFromUuid(loopPlayer.getUniqueId());
            itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(Messages.getParam("gui.portal-settings.access-menu.access-players.item-names.player", "{1}", loopPlayer.getName()));
            itemMeta.setLore(Messages.getList("gui.portal-settings.access-menu.access-players.item-lores.player"));
            item.setItemMeta(itemMeta);

            items[i] = ClickableItem.of(item, 
                e -> removePlayerAccess(player, portal, pagination.getPage(), loopPlayer.getUniqueId()));
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

    public void removePlayerAccess(Player player, Portal portal, int page, UUID uuid) {
        plugin.handleClose.remove(player);

        portalManager.removePlayerAccess(portal, uuid);
        
        open(player, portal, page);
    }
}