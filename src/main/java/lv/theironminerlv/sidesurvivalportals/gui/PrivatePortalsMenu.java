package lv.theironminerlv.sidesurvivalportals.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
import lv.theironminerlv.sidesurvivalportals.data.PortalData;
import lv.theironminerlv.sidesurvivalportals.managers.DataManager;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.ConvertUtils;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Land;

public class PrivatePortalsMenu implements InventoryProvider {
    private static SideSurvivalPortals plugin = SideSurvivalPortals.getInstance();
    private InventoryManager invManager = plugin.getInvManager();
    private PortalManager portalManager = plugin.getPortalManager();
    private DataManager dataManager = plugin.getDataManager();
    private LandsIntegration landsAPI = plugin.getLandsAPI();
    private SmartInventory inventory;

    public PrivatePortalsMenu() {

    }

    private void load() {
        this.inventory = SmartInventory.builder().manager(invManager).id("portal").provider(new PrivatePortalsMenu())
                .size(6, 9).title(ChatColor.DARK_PURPLE + "Privātie portāli").build();
    }

    public void open(Player player) {
        this.load();
        this.inventory.open(player);
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
        List<Portal> portals = new ArrayList<Portal>();

        for (Land land : playerLands) {
            portals.addAll(PortalData.getByLand(land).values());
        }

        int portalAmount = portals.size();
        
        ClickableItem[] items = new ClickableItem[portalAmount];

        for(int i = 0; i < items.length; i++) {
            Portal portal = portals.get(i);
            
            item = new ItemStack(Material.PAPER);
            itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ConvertUtils.color("&5&lPortāls"));
            String posReadable = ConvertUtils.readableLoc(portal.getPos1());
            itemMeta.setLore(ConvertUtils.color(Arrays.asList("", "&7Teritorija: &f" + portal.getLand().getName(), "&7Lokācija: &f" + posReadable, "", "&dSpied, lai teleportētos!")));
            item.setItemMeta(itemMeta);

            items[i] = ClickableItem.of(item, 
                e -> teleportTo(player, portal));
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(45);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        contents.set(5, 2, ClickableItem.of(MenuItems.prevPage,
            e -> open(player, pagination.previous().getPage())));
        contents.set(5, 6, ClickableItem.of(MenuItems.nextPage,
            e -> open(player, pagination.next().getPage())));

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    public void teleportTo(Player player, Portal portal) {
        Location loc = portal.getTpLoc().clone();

        if (!loc.getBlock().isEmpty() || !loc.getBlock().getRelative(BlockFace.UP).isEmpty()) {
            loc = null;
        }

        Block checkBlock = portal.getTpLoc().getBlock().getRelative(BlockFace.DOWN);
        if (checkBlock.isEmpty() || checkBlock.isLiquid() || checkBlock.isPassable()) {

            if (checkBlock.isEmpty()) {
                checkBlock = checkBlock.getRelative(BlockFace.DOWN);
                if (checkBlock.isEmpty() || checkBlock.isLiquid() || checkBlock.isPassable())
                    loc = null;
            } else
                loc = null;
        }

        if (loc == null) {
            loc = portalManager.getSafeTeleportLoc(portal);
            
            if (loc == null) {
                player.sendMessage(ConvertUtils.color("&cThe teleport location is unsafe!"));
                return;
            }
            
            portal.setTpLoc(loc);
            dataManager.save(portal);
        }
  
        loc.setPitch(player.getLocation().getPitch());
        loc.setYaw(player.getLocation().getYaw());

        player.teleport(loc);
    }
}