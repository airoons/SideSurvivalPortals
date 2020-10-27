package lv.theironminerlv.sidesurvivalportals.gui;

import java.util.ArrayList;
import java.util.List;

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
import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.ConvertUtils;
import lv.theironminerlv.sidesurvivalportals.utils.Messages;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Land;

public class PortalAccessLands implements InventoryProvider {
    private static SideSurvivalPortals plugin = SideSurvivalPortals.getInstance();
    private InventoryManager invManager = plugin.getInvManager();
    private PortalManager portalManager = plugin.getPortalManager();
    private LandsIntegration landsAPI = plugin.getLandsAPI();
    private SmartInventory inventory;
    private Portal portal;

    public PortalAccessLands(Portal portal) {
        this.portal = portal;
    }

    private void load() {
        this.inventory = SmartInventory.builder()
            .manager(invManager)
            .provider(new PortalAccessLands(portal))
            .size(4, 9)
            .title(Messages.get("gui.portal-settings.access-menu.access-lands.gui-title"))
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

        List<Integer> allowedLands = portal.getAllowedLands();
        List<Land> lands = new ArrayList<Land>();

        for (int landId : allowedLands) {
            if (landsAPI.getLand(landId) == null) {
                portalManager.removeLandAccess(portal, landId);
                continue;
            }

            lands.add(landsAPI.getLand(landId));
        }

        int landAmount = lands.size();

        ClickableItem[] items = new ClickableItem[landAmount];

        for(int i = 0; i < items.length; i++) {
            Land land = lands.get(i);
            
            item = new ItemStack(Material.BOOK);
            itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(Messages.getParam("gui.portal-settings.access-menu.access-lands.item-names.land", "{1}", land.getName()));
            itemMeta.setLore(Messages.getList("gui.portal-settings.access-menu.access-lands.item-lores.land"));
            item.setItemMeta(itemMeta);

            items[i] = ClickableItem.of(item, 
                e -> removeLandAccess(player, portal, pagination.getPage(), land));
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

    public void removeLandAccess(Player player, Portal portal, int page, Land land) {
        plugin.handleClose.remove(player);

        portalManager.removeLandAccess(portal, land);
        
        open(player, portal, page);
    }
}