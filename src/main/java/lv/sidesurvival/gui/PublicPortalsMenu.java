package lv.sidesurvival.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lv.sidesurvival.SurvivalPortals;
import lv.sidesurvival.managers.ClaimManager;
import lv.sidesurvival.managers.PortalManager;
import lv.sidesurvival.objects.ClaimOwner;
import lv.sidesurvival.objects.Portal;
import lv.sidesurvival.utils.ConvertUtils;
import lv.sidesurvival.utils.Messages;
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
import lv.sidesurvival.data.PortalData;

public class PublicPortalsMenu implements InventoryProvider {

    private static final SurvivalPortals plugin = SurvivalPortals.getInstance();
    private final InventoryManager invManager = plugin.getInvManager();
    private final PortalManager portalManager = plugin.getPortalManager();
    private SmartInventory inventory;

    private void load(Player player) {
        this.inventory = SmartInventory.builder()
                .manager(invManager)
                .provider(new PublicPortalsMenu())
                .size(4, 9)
                .title(Messages.get(player, "gui.public-portals.gui-title"))
                .build();
    }

    public void open(Player player) {
        this.load(player);
        player.closeInventory();
        this.inventory.open(player);
        plugin.handleClose.add(player);
    }

    public void open(Player player, int page) {
        this.load(player);
        this.inventory.open(player, page);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        ItemStack item;
        ItemMeta itemMeta;
        Pagination pagination = contents.pagination();

        Map<String, Portal> portals = PortalData.getAllPublic();

        int portalAmount = portals.size();
        String posReadable;
        String desc;
        String temp;
        List<String> descLines = new ArrayList<>();
        int index;

        ClickableItem[] items = new ClickableItem[portalAmount];

        int i = 0;
        for (Portal portal : portals.values()) {
            ClaimOwner owner = ClaimManager.get().getOwnerById(portal.getOwner());
            if (owner == null)
                continue;

            item = portal.getIcon().clone();
            itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(
                    Messages.getParam(player, "gui.public-portals.item-names.portal", "{1}", owner.getName(null)));
            if (portal.getPos1() != null)
                posReadable = ConvertUtils.readableLoc(portal.getPos1());
            else
                posReadable = ConvertUtils.readableLocStr(portal.getLocStr());
            descLines.clear();

            desc = portal.getDescription();

            descLines.addAll(Messages.getListParam(player, "gui.public-portals.item-lores.portal-start", "{1}", posReadable));

            index = 0;
            while (index < desc.length()) {
                temp = Messages.getParam(player, "gui.public-portals.item-lores.portal-desc-lines", "{1}",
                        desc.substring(index, Math.min(index + 30, desc.length())));

                if (index + 31 < desc.length() && desc.charAt(index + 31) != ' ')
                    descLines.add(temp + "-");
                else
                    descLines.add(temp);

                index += 30;
            }

            descLines.addAll(Messages.getList(player, "gui.public-portals.item-lores.portal-end"));

            itemMeta.setLore(ConvertUtils.color(descLines));
            item.setItemMeta(itemMeta);

            items[i] = ClickableItem.of(item, e -> portalManager.teleportTo(player, portal));
            i++;
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(27);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        contents.set(3, 2, ClickableItem.of(MenuItems.prevPage(player), e -> open(player, pagination.previous().getPage())));
        contents.set(3, 6, ClickableItem.of(MenuItems.nextPage(player), e -> open(player, pagination.next().getPage())));

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}