package lv.theironminerlv.sidesurvivalportals.gui;

import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;
import org.bukkit.Material;
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
import lv.theironminerlv.sidesurvivalportals.SurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.DataManager;
import lv.theironminerlv.sidesurvivalportals.managers.MenuManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.Messages;

import java.util.List;

public class EditPortalColor implements InventoryProvider {

    private static final SurvivalPortals plugin = SurvivalPortals.getInstance();
    private final InventoryManager invManager = plugin.getInvManager();
    private final MenuManager menuManager = plugin.getMenuManager();
    private final DataManager dataManager = plugin.getDataManager();
    private SmartInventory inventory;
    private Portal portal;

    public EditPortalColor(Portal portal) {
        this.portal = portal;
    }

    private void load(Player player, Portal portal) {
        this.inventory = SmartInventory.builder()
                .manager(invManager)
                .provider(new EditPortalColor(portal))
                .size(2, 9)
                .title(Messages.get(player, "gui.portal-settings.change-color.gui-title"))
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
        Pagination pagination = contents.pagination();

        List<ItemStack> available = MenuItems.availableColors(player);
        int iconAmount = available.size();

        ClickableItem[] items = new ClickableItem[iconAmount];

        for(int i = 0; i < items.length; i++) {
            ItemStack item = available.get(i).clone();
            ItemStack loopIcon = item.clone();

            if (portal.getIcon().getType() == item.getType()) {
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setLore(Messages.getList(player, "gui.portal-settings.change-color.current-color"));
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(itemMeta);
                items[i] = ClickableItem.empty(item);
            } else {
                items[i] = ClickableItem.of(item, e -> changePortalColor(player, portal, loopIcon));
            }
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(54);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void changePortalColor(Player player, Portal portal, ItemStack color) {
        if (!menuManager.portalPermCheck(player, portal)) {
            player.closeInventory();
            return;
        }

        plugin.getPortalManager().setPortalGlass(
                portal.getPos1(),
                portal.getPos2(),
                portal.getNorthSouth(),
                color.getType()
        );

        plugin.handleClose.remove(player);
        menuManager.openEditPortal(player, portal);
    }
}