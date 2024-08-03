package lv.sidesurvival.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import lv.sidesurvival.SurvivalPortals;
import lv.sidesurvival.managers.DataManager;
import lv.sidesurvival.managers.MenuManager;
import lv.sidesurvival.objects.Portal;
import lv.sidesurvival.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class EditPortalIcon implements InventoryProvider {

    private static final SurvivalPortals plugin = SurvivalPortals.getInstance();
    private final InventoryManager invManager = plugin.getInvManager();
    private final MenuManager menuManager = plugin.getMenuManager();
    private final DataManager dataManager = plugin.getDataManager();
    private SmartInventory inventory;
    private Portal portal;

    public EditPortalIcon(Portal portal) {
        this.portal = portal;
    }

    private void load(Player player, Portal portal) {
        this.inventory = SmartInventory.builder()
            .manager(invManager)
            .provider(new EditPortalIcon(portal))
            .size(6, 9)
            .title(Messages.get(player, "gui.portal-settings.change-icon.gui-title"))
            .build();
    }

    public void open(Player player, Portal portal) {
        this.portal = portal;
        this.load(player, portal);
        player.closeInventory();
        this.inventory.open(player);
        plugin.handleClose.add(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        ItemStack item;
        ItemMeta itemMeta;
        Pagination pagination = contents.pagination();

        List<ItemStack> available = MenuItems.availableIcons(player);
        int iconAmount = available.size();

        ClickableItem[] items = new ClickableItem[iconAmount];

        for(int i = 0; i < items.length; i++) {
            item = available.get(i).clone();
            ItemStack loopIcon = item.clone();
            
            if (portal.getIcon().getType() == item.getType()) {
                itemMeta = item.getItemMeta();
                itemMeta.setEnchantmentGlintOverride(true);
                itemMeta.setLore(Messages.getList(player, "gui.portal-settings.change-icon.item-lores.current-icon"));
                item.setItemMeta(itemMeta);
                items[i] = ClickableItem.empty(item);
            } else {
                items[i] = ClickableItem.of(item, e -> changePortalIcon(player, portal, loopIcon));
            }
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(54);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void changePortalIcon(Player player, Portal portal, ItemStack icon) {
        if (!menuManager.portalPermCheck(player, portal)) {
            player.closeInventory();
            return;
        }

        portal.setIcon(icon);
        dataManager.save(portal);

        plugin.handleClose.remove(player);
        
        menuManager.openEditPortal(player, portal);
    }
}