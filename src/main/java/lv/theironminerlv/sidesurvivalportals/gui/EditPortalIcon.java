package lv.theironminerlv.sidesurvivalportals.gui;

import java.util.Arrays;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
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
import lv.theironminerlv.sidesurvivalportals.managers.DataManager;
import lv.theironminerlv.sidesurvivalportals.managers.MenuManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.ConvertUtils;
import lv.theironminerlv.sidesurvivalportals.utils.Messages;

public class EditPortalIcon implements InventoryProvider
{
    private static SideSurvivalPortals plugin = SideSurvivalPortals.getInstance();
    private InventoryManager invManager = plugin.getInvManager();
    private MenuManager menuManager = plugin.getMenuManager();
    private DataManager dataManager = plugin.getDataManager();
    private SmartInventory inventory;
    private Portal portal;

    public EditPortalIcon(Portal portal) {
        this.portal = portal;
    }

    private void load(Portal portal) {
        this.inventory = SmartInventory.builder()
            .manager(invManager)
            .provider(new EditPortalIcon(portal))
            .size(6, 9)
            .title(Messages.get("gui.portal-settings.change-icon.gui-title"))
            .build();
    }

    public void open(Player player, Portal portal) {
        this.portal = portal;
        this.load(portal);
        player.closeInventory();
        this.inventory.open(player);
        plugin.handleClose.add(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        ItemStack item;
        ItemMeta itemMeta;
        Pagination pagination = contents.pagination();

        int iconAmount = MenuItems.availableIcons.size();
        
        ClickableItem[] items = new ClickableItem[iconAmount];

        for(int i = 0; i < items.length; i++) {
            item = MenuItems.availableIcons.get(i).clone();
            ItemStack loopIcon = item.clone();
            
            if (portal.getIcon().getType() == item.getType()) {
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                itemMeta = item.getItemMeta();
                itemMeta.setLore(Messages.getList("gui.portal-settings.change-icon.item-lores.current-icon"));
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(itemMeta);
                items[i] = ClickableItem.empty(item);
            } else {
                items[i] = ClickableItem.of(item, 
                e -> changePortalIcon(player, portal, loopIcon));
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