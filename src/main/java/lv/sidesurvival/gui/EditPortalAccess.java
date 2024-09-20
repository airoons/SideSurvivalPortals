package lv.sidesurvival.gui;

import lv.sidesurvival.SurvivalPortals;
import lv.sidesurvival.managers.DataManager;
import lv.sidesurvival.managers.MenuManager;
import lv.sidesurvival.objects.Portal;
import lv.sidesurvival.utils.Messages;
import org.bukkit.entity.Player;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;

public class EditPortalAccess implements InventoryProvider {

    private static final SurvivalPortals plugin = SurvivalPortals.getInstance();
    private final InventoryManager invManager = plugin.getInvManager();
    private final MenuManager menuManager = plugin.getMenuManager();
    private final DataManager dataManager = plugin.getDataManager();
    private SmartInventory inventory;
    private Portal portal;

    public EditPortalAccess(Portal portal) {
        this.portal = portal;
    }

    private void load(Player player, Portal portal) {
        this.inventory = SmartInventory.builder()
            .manager(invManager)
            .provider(new EditPortalAccess(portal))
            .size(1, 9)
            .title(Messages.get(player, "gui.portal-settings.access-menu.gui-title"))
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
        if (portal.getIsPublic())
            contents.set(0, 2, ClickableItem.of(MenuItems.accessPublic(player), e -> togglePublic(player, portal, false, contents)));
        else
            contents.set(0, 2, ClickableItem.of(MenuItems.accessPrivate(player), e -> togglePublic(player, portal, true, contents)));

        contents.set(0, 4, ClickableItem.of(MenuItems.accessGroups(player), e -> menuManager.openPortalGroupAccess(player, portal)));
        contents.set(0, 6, ClickableItem.of(MenuItems.accessPlayers(player), e -> menuManager.openPortalPlayerAccess(player, portal)));
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
            contents.set(0, 2, ClickableItem.of(MenuItems.accessPublic(player), e -> togglePublic(player, portal, false, contents)));
        else
            contents.set(0, 2, ClickableItem.of(MenuItems.accessPrivate(player), e -> togglePublic(player, portal, true, contents)));
        
        portal.setIsPublic(isPublic);
        dataManager.save(portal);
    }
}