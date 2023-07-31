package lv.theironminerlv.sidesurvivalportals.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lv.theironminerlv.sidesurvivalportals.SurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.MenuManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.Messages;

public class EditPortalMenu implements InventoryProvider {

    private static final SurvivalPortals plugin = SurvivalPortals.getInstance();
    private final InventoryManager invManager = plugin.getInvManager();
    private final MenuManager menuManager = plugin.getMenuManager();
    private SmartInventory inventory;
    private Portal portal;

    public EditPortalMenu(Portal portal) {
        this.portal = portal;
    }

    private void load(Player player, Portal portal) {
        this.inventory = SmartInventory.builder()
            .manager(invManager)
            .provider(new EditPortalMenu(portal))
            .size(3, 9)
            .title(Messages.get(player, "gui.portal-settings.gui-title"))
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
        ItemMeta meta;

        contents.fillBorders(ClickableItem.empty(MenuItems.grayPane));
        contents.set(0, 1, ClickableItem.empty(MenuItems.blackPane));
        contents.set(0, 4, ClickableItem.empty(MenuItems.lightGrayPane));
        contents.set(0, 7, ClickableItem.empty(MenuItems.blackPane));
        contents.set(1, 0, ClickableItem.empty(MenuItems.blackPane));
        contents.set(1, 8, ClickableItem.empty(MenuItems.blackPane));
        contents.set(2, 1, ClickableItem.empty(MenuItems.blackPane));
        contents.set(2, 4, ClickableItem.empty(MenuItems.lightGrayPane));
        contents.set(2, 7, ClickableItem.empty(MenuItems.blackPane));

        contents.set(1, 2, ClickableItem.of(MenuItems.editPortalAccess(player), e -> menuManager.openEditPortalAccess(player, portal)));
        contents.set(1, 4, ClickableItem.of(MenuItems.editPortalDescr(player), e -> editPortalDescr(player, portal)));

        item = portal.getIcon().clone();
        meta = item.getItemMeta();
        meta.setDisplayName(Messages.get(player, "gui.portal-settings.item-names.change-icon"));
        meta.setLore(Messages.getList(player, "gui.portal-settings.item-lores.change-icon"));
        item.setItemMeta(meta);
        contents.set(1, 6, ClickableItem.of(item, e -> menuManager.openEditPortalIcon(player, portal)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void editPortalDescr(Player player, Portal portal) {
        plugin.handleClose.remove(player);
        player.closeInventory();
        player.sendMessage(Messages.get(player, "chat.commands.description.from-gui"));
    }
}