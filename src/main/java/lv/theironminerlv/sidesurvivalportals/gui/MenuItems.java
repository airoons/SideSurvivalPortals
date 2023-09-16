package lv.theironminerlv.sidesurvivalportals.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.dbassett.skullcreator.SkullCreator;
import lv.theironminerlv.sidesurvivalportals.SurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.utils.Messages;

public class MenuItems {

    public static SurvivalPortals plugin = SurvivalPortals.getInstance();

    public static ItemStack lightGrayPane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
    public static ItemStack grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    public static ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

    private static final ItemStack goSpawn = SkullCreator.itemFromBase64(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdhN2I1ZGM0ZGMzNmYzY2YxMWFjMTg1NWJlNDNmMzdmYzU0YjhhYWRjM2U5NGFlYjY5OWM5NzE4YTNlM2Q0MSJ9fX0=");
    private static final ItemStack goSpawnBedrock = SkullCreator.itemFromBase64(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzk1ZDM3OTkzZTU5NDA4MjY3ODQ3MmJmOWQ4NjgyMzQxM2MyNTBkNDMzMmEyYzdkOGM1MmRlNDk3NmIzNjIifX19");
    private static final ItemStack goNetherBedrock = SkullCreator.itemFromBase64(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTliOTRlNWFkOTNkYzdhZGY1OTAwNTZkNGExZTAzNDA5MjUzZGZlY2ZjODhlODMxNTQxYzhkZjU0ZmYwNWNhNiJ9fX0=");

    private static final ItemStack pubPortals = SkullCreator.itemFromBase64(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjFkZDRmZTRhNDI5YWJkNjY1ZGZkYjNlMjEzMjFkNmVmYTZhNmI1ZTdiOTU2ZGI5YzVkNTljOWVmYWIyNSJ9fX0");
    private static final ItemStack portalSettings = new ItemStack(Material.WRITABLE_BOOK);

    private static final ItemStack prevPage = new ItemStack(Material.ARROW);
    private static final ItemStack nextPage = new ItemStack(Material.ARROW);

    private static final ItemStack editPortalAccess = new ItemStack(Material.NAME_TAG);
    private static final ItemStack editPortalDescr = new ItemStack(Material.OAK_SIGN);

    public static final ItemStack defaultIcon = new ItemStack(Material.PAPER);

    private static final ItemStack accessPublic = new ItemStack(Material.OBSIDIAN);
    private static final ItemStack accessPrivate = new ItemStack(Material.CRYING_OBSIDIAN);

    private static final ItemStack accessGroups = goSpawn.clone();
    private static final ItemStack accessPlayers = new ItemStack(Material.PLAYER_HEAD);

    public MenuItems() {
        ItemMeta itemMeta;
        itemMeta = lightGrayPane.getItemMeta();
        itemMeta.setDisplayName(" ");
        lightGrayPane.setItemMeta(itemMeta);

        itemMeta = grayPane.getItemMeta();
        itemMeta.setDisplayName(" ");
        grayPane.setItemMeta(itemMeta);

        itemMeta = blackPane.getItemMeta();
        itemMeta.setDisplayName(" ");
        blackPane.setItemMeta(itemMeta);
    }

    public static ItemStack goSpawn(Player player) {
        ItemStack item = goSpawn.clone();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Messages.get(player, "gui.main-menu.item-names.to-spawn"));
        itemMeta.setLore(Messages.getList(player, "gui.main-menu.item-lores.to-spawn"));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack goSpawnBedrock(Player player) {
        ItemStack item = goSpawnBedrock.clone();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Messages.get(player, "gui.main-menu.item-names.to-spawn-bedrock"));
        itemMeta.setLore(Messages.getList(player, "gui.main-menu.item-lores.to-spawn-bedrock"));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack goNetherBedrock(Player player) {
        ItemStack item = goNetherBedrock.clone();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Messages.get(player, "gui.main-menu.item-names.to-nether-bedrock"));
        itemMeta.setLore(Messages.getList(player, "gui.main-menu.item-lores.to-nether-bedrock"));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack pubPortals(Player player) {
        ItemStack item = pubPortals.clone();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Messages.get(player, "gui.main-menu.item-names.public-portals"));
        itemMeta.setLore(Messages.getList(player, "gui.main-menu.item-lores.public-portals"));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack portalSettings(Player player) {
        ItemStack item = portalSettings.clone();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Messages.get(player, "gui.main-menu.item-names.portal-settings"));
        itemMeta.setLore(Messages.getList(player, "gui.main-menu.item-lores.portal-settings"));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack prevPage(Player player) {
        ItemStack item = prevPage.clone();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Messages.get(player, "gui.previous-page"));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack nextPage(Player player) {
        ItemStack item = nextPage.clone();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Messages.get(player, "gui.next-page"));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack editPortalAccess(Player player) {
        ItemStack item = editPortalAccess.clone();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Messages.get(player, "gui.portal-settings.item-names.change-access"));
        itemMeta.setLore(Messages.getList(player, "gui.portal-settings.item-lores.change-access"));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack editPortalDescr(Player player) {
        ItemStack item = editPortalDescr.clone();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Messages.get(player, "gui.portal-settings.item-names.change-description"));
        itemMeta.setLore(Messages.getList(player, "gui.portal-settings.item-lores.change-description"));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static List<ItemStack> availableIcons(Player player) {
        List<String> configIcons = plugin.getConfiguration().getStringList("availableIcons");
        ItemStack item;

        List<ItemStack> result = new ArrayList<>();
        ItemMeta itemMeta;

        for (String configIcon : configIcons) {
            item = new ItemStack(Material.valueOf(configIcon));

            itemMeta = item.getItemMeta();
            itemMeta.setLore(Messages.getList(player, "gui.portal-settings.change-icon.item-lores.icon"));
            itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
            item.setItemMeta(itemMeta);

            result.add(item);
        }

        return result;
    }

    public static ItemStack accessPublic(Player player) {
        ItemStack item = accessPublic.clone();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Messages.get(player, "gui.portal-settings.access-menu.item-names.is-public"));
        itemMeta.setLore(Messages.getList(player, "gui.portal-settings.access-menu.item-lores.is-public"));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack accessPrivate(Player player) {
        ItemStack item = accessPrivate.clone();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Messages.get(player, "gui.portal-settings.access-menu.item-names.is-private"));
        itemMeta.setLore(Messages.getList(player, "gui.portal-settings.access-menu.item-lores.is-private"));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack accessGroups(Player player) {
        ItemStack item = accessGroups.clone();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Messages.get(player, "gui.portal-settings.access-menu.item-names.group-access"));
        itemMeta.setLore(Messages.getList(player, "gui.portal-settings.access-menu.item-lores.group-access"));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack accessPlayers(Player player) {
        ItemStack item = accessPlayers.clone();
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Messages.get(player, "gui.portal-settings.access-menu.item-names.player-access"));
        itemMeta.setLore(Messages.getList(player, "gui.portal-settings.access-menu.item-lores.player-access"));
        item.setItemMeta(itemMeta);
        return item;
    }
}