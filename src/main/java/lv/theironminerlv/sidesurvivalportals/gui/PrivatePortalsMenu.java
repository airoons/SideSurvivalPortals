package lv.theironminerlv.sidesurvivalportals.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
import lv.theironminerlv.sidesurvivalportals.data.PortalData;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.ConvertUtils;
import lv.theironminerlv.sidesurvivalportals.utils.Messages;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Land;

public class PrivatePortalsMenu implements InventoryProvider {
    private static SideSurvivalPortals plugin = SideSurvivalPortals.getInstance();
    private InventoryManager invManager = plugin.getInvManager();
    private PortalManager portalManager = plugin.getPortalManager();
    private LandsIntegration landsAPI = plugin.getLandsAPI();
    private SmartInventory inventory;

    private void load() {
        this.inventory = SmartInventory.builder()
            .manager(invManager)
            .provider(new PrivatePortalsMenu())
            .size(4, 9)
            .title(Messages.get("gui.private-portals.gui-title"))
            .build();
    }

    public void open(Player player) {
        this.load();
        player.closeInventory();
        this.inventory.open(player);
        plugin.handleClose.add(player);
    }

    public void open(Player player, int page) {
        this.load();
        this.inventory.open(player, page);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        ItemStack item;
        ItemMeta itemMeta;
        Pagination pagination = contents.pagination();

        Set<? extends Land> playerLands = landsAPI.getLandPlayer(player.getUniqueId()).getLands();
        Map<String, Portal> portals = new ConcurrentHashMap<>();

        for (Land land : playerLands) {
            portals.putAll(PortalData.getByLand(land));
            portals.putAll(PortalData.getAccessablePortalsByLand(land));
        }

        portals.putAll(PortalData.getAccessablePortalsByPlayer(player.getUniqueId()));

        int portalAmount = portals.size();
        String desc;
        String temp;
        List<String> descLines = new ArrayList<>();
        int index;

        ClickableItem[] items = new ClickableItem[portalAmount];

        int i = 0;
        for (Portal portal : portals.values()) {
            item = portal.getIcon().clone();
            itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(Messages.getParam("gui.private-portals.item-names.portal", "{1}", portal.getLand().getName()));
            descLines.clear();

            desc = portal.getDescription();

            descLines.addAll(Messages.getList("gui.private-portals.item-lores.portal-start"));

            index = 0;
            while (index < desc.length()) {
                temp = Messages.getParam("gui.private-portals.item-lores.portal-desc-lines", "{1}", desc.substring(index, Math.min(index + 30,desc.length())));

                if (index + 31 < desc.length() && desc.charAt(index + 31) != ' ')
                    descLines.add(temp + "-");
                else
                    descLines.add(temp);

                index += 30;
            }

            descLines.addAll(Messages.getList("gui.private-portals.item-lores.portal-end"));
            
            itemMeta.setLore(ConvertUtils.color(descLines));
            item.setItemMeta(itemMeta);

            items[i] = ClickableItem.of(item, 
                e -> portalManager.teleportTo(player, portal, true));
            i++;
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(27);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        contents.set(3, 2, ClickableItem.of(MenuItems.prevPage,
            e -> open(player, pagination.previous().getPage())));
        contents.set(3, 6, ClickableItem.of(MenuItems.nextPage,
            e -> open(player, pagination.next().getPage())));

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}