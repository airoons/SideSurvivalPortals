package lv.theironminerlv.sidesurvivalportals.gui;

import org.bukkit.entity.Player;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lv.theironminerlv.sidesurvivalportals.SurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.DataManager;
import lv.theironminerlv.sidesurvivalportals.managers.MenuManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.Messages;

public class EditPortalAccess implements InventoryProvider {

    private static SurvivalPortals plugin = SurvivalPortals.getInstance();
    private InventoryManager invManager = plugin.getInvManager();
    private MenuManager menuManager = plugin.getMenuManager();
    private DataManager dataManager = plugin.getDataManager();
    private SmartInventory inventory;
    private Portal portal;

    public EditPortalAccess(Portal portal) {
        this.portal = portal;
    }

    private void load(Portal portal) {
        this.inventory = SmartInventory.builder()
            .manager(invManager)
            .provider(new EditPortalAccess(portal))
            .size(3, 9)
            .title(Messages.get("gui.portal-settings.access-menu.gui-title"))
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
        contents.fillBorders(ClickableItem.empty(MenuItems.grayPane));
        contents.set(0, 1, ClickableItem.empty(MenuItems.blackPane));
        contents.set(0, 4, ClickableItem.empty(MenuItems.lightGrayPane));
        contents.set(0, 7, ClickableItem.empty(MenuItems.blackPane));
        contents.set(1, 0, ClickableItem.empty(MenuItems.blackPane));
        contents.set(1, 8, ClickableItem.empty(MenuItems.blackPane));
        contents.set(2, 1, ClickableItem.empty(MenuItems.blackPane));
        contents.set(2, 4, ClickableItem.empty(MenuItems.lightGrayPane));
        contents.set(2, 7, ClickableItem.empty(MenuItems.blackPane));

        if (portal.getIsPublic())
            contents.set(1, 2, ClickableItem.of(MenuItems.accessPublic, e -> togglePublic(player, portal, false, contents)));
        else
            contents.set(1, 2, ClickableItem.of(MenuItems.accessPrivate, e -> togglePublic(player, portal, true, contents)));

        contents.set(1, 4, ClickableItem.of(MenuItems.accessGroups, e -> menuManager.openPortalGroupAccess(player, portal)));
        contents.set(1, 6, ClickableItem.of(MenuItems.accessPlayers, e -> menuManager.openPortalPlayerAccess(player, portal)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void togglePublic(Player player, Portal portal, boolean isPublic, InventoryContents contents) {
        if (!menuManager.portalPermCheck(player, portal)) {
            plugin.handleClose.remove(player);
            player.closeInventory();
            return;
        }

        if (isPublic)
            contents.set(1, 2, ClickableItem.of(MenuItems.accessPublic, e -> togglePublic(player, portal, false, contents)));
        else
            contents.set(1, 2, ClickableItem.of(MenuItems.accessPrivate, e -> togglePublic(player, portal, true, contents)));
        
        portal.setIsPublic(isPublic);
        dataManager.save(portal);
    }
}