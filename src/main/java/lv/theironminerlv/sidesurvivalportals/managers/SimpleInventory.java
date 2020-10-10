package lv.theironminerlv.sidesurvivalportals.managers;

/*
 * Example inventory of minuskube SmartInventory framework
 */

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;

public class SimpleInventory implements InventoryProvider
{
    private static InventoryManager invManager = SideSurvivalPortals.getInvManager();
    private SmartInventory inventory;
    
    private void load() {
        this.inventory = SmartInventory.builder()
        .manager(invManager)
        .id("myInventory")
        .provider(new SimpleInventory())
        .size(3, 9)
        .title(ChatColor.BLUE + "My Awesome Inventory!")
        .build();
    } 

    public void open(Player player) {
        this.load();
        this.inventory.open(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));

        contents.set(1, 1, ClickableItem.of(new ItemStack(Material.CARROT),
                e -> player.sendMessage(ChatColor.GOLD + "You clicked on a carrot.")));

        contents.set(1, 7, ClickableItem.of(new ItemStack(Material.BARRIER),
                e -> player.closeInventory()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        
    }

    // @Override
    // public void update(Player player, InventoryContents contents) {
    //     int state = contents.property("state", 0);
    //     contents.setProperty("state", state + 1);

    //     ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

    //     if (state % 5 != 0) {
    //         glass = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
    //     }

    //     contents.fillBorders(ClickableItem.empty(glass));
    // }
}