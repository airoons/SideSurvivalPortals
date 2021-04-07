package lv.theironminerlv.sidesurvivalportals.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.dbassett.skullcreator.SkullCreator;
import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.utils.Messages;

public class MenuItems {
    public static SideSurvivalPortals plugin = SideSurvivalPortals.getInstance();

    public static ItemStack lightGrayPane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
    public static ItemStack grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    public static ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

    public static ItemStack goSpawn = SkullCreator.itemFromBase64(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdhN2I1ZGM0ZGMzNmYzY2YxMWFjMTg1NWJlNDNmMzdmYzU0YjhhYWRjM2U5NGFlYjY5OWM5NzE4YTNlM2Q0MSJ9fX0=");
    public static ItemStack goSpawnBedrock = SkullCreator.itemFromBase64(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzk1ZDM3OTkzZTU5NDA4MjY3ODQ3MmJmOWQ4NjgyMzQxM2MyNTBkNDMzMmEyYzdkOGM1MmRlNDk3NmIzNjIifX19");
    public static ItemStack goNetherBedrock = SkullCreator.itemFromBase64(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTliOTRlNWFkOTNkYzdhZGY1OTAwNTZkNGExZTAzNDA5MjUzZGZlY2ZjODhlODMxNTQxYzhkZjU0ZmYwNWNhNiJ9fX0=");

    public static ItemStack pubPortals = SkullCreator.itemFromBase64(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjFkZDRmZTRhNDI5YWJkNjY1ZGZkYjNlMjEzMjFkNmVmYTZhNmI1ZTdiOTU2ZGI5YzVkNTljOWVmYWIyNSJ9fX0");
    public static ItemStack portalSettings = new ItemStack(Material.WRITABLE_BOOK);

    public static ItemStack prevPage = new ItemStack(Material.ARROW);
    public static ItemStack nextPage = new ItemStack(Material.ARROW);

    public static ItemStack editPortalAccess = new ItemStack(Material.NAME_TAG);
    public static ItemStack editPortalDescr = new ItemStack(Material.OAK_SIGN);

    public static ItemStack defaultIcon = new ItemStack(Material.PAPER);
    public static List<ItemStack> availableIcons = new ArrayList<ItemStack>();

    public static ItemStack accessPublic = new ItemStack(Material.OBSIDIAN);
    public static ItemStack accessPrivate = new ItemStack(Material.CRYING_OBSIDIAN);

    public static ItemStack accessLands = goSpawn.clone();
    public static ItemStack accessPlayers = new ItemStack(Material.PLAYER_HEAD);

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

        itemMeta = goSpawn.getItemMeta();
        itemMeta.setDisplayName(Messages.get("gui.main-menu.item-names.to-spawn"));
        itemMeta.setLore(Messages.getList("gui.main-menu.item-lores.to-spawn"));
        goSpawn.setItemMeta(itemMeta);

        itemMeta = goSpawnBedrock.getItemMeta();
        itemMeta.setDisplayName(Messages.get("gui.main-menu.item-names.to-spawn-bedrock"));
        itemMeta.setLore(Messages.getList("gui.main-menu.item-lores.to-spawn-bedrock"));
        goSpawnBedrock.setItemMeta(itemMeta);

        itemMeta = goNetherBedrock.getItemMeta();
        itemMeta.setDisplayName(Messages.get("gui.main-menu.item-names.to-nether-bedrock"));
        itemMeta.setLore(Messages.getList("gui.main-menu.item-lores.to-nether-bedrock"));
        goNetherBedrock.setItemMeta(itemMeta);

        itemMeta = pubPortals.getItemMeta();
        itemMeta.setDisplayName(Messages.get("gui.main-menu.item-names.public-portals"));
        itemMeta.setLore(Messages.getList("gui.main-menu.item-lores.public-portals"));
        pubPortals.setItemMeta(itemMeta);

        itemMeta = portalSettings.getItemMeta();
        itemMeta.setDisplayName(Messages.get("gui.main-menu.item-names.portal-settings"));
        itemMeta.setLore(Messages.getList("gui.main-menu.item-lores.portal-settings"));
        portalSettings.setItemMeta(itemMeta);

        itemMeta = prevPage.getItemMeta();
        itemMeta.setDisplayName(Messages.get("gui.previous-page"));
        prevPage.setItemMeta(itemMeta);

        itemMeta = nextPage.getItemMeta();
        itemMeta.setDisplayName(Messages.get("gui.next-page"));
        nextPage.setItemMeta(itemMeta);

        itemMeta = editPortalAccess.getItemMeta();
        itemMeta.setDisplayName(Messages.get("gui.portal-settings.item-names.change-access"));
        itemMeta.setLore(Messages.getList("gui.portal-settings.item-lores.change-access"));
        editPortalAccess.setItemMeta(itemMeta);

        itemMeta = editPortalDescr.getItemMeta();
        itemMeta.setDisplayName(Messages.get("gui.portal-settings.item-names.change-description"));
        itemMeta.setLore(Messages.getList("gui.portal-settings.item-lores.change-description"));
        editPortalDescr.setItemMeta(itemMeta);

        List<String> configIcons = plugin.getConfiguration().getStringList("availableIcons");
        ItemStack item;
        for (String configIcon : configIcons) {
            item = new ItemStack(Material.valueOf(configIcon));

            itemMeta = item.getItemMeta();
            itemMeta.setLore(Messages.getList("gui.portal-settings.change-icon.item-lores.icon"));
            item.setItemMeta(itemMeta);

            availableIcons.add(item);
        }

        itemMeta = accessPublic.getItemMeta();
        itemMeta.setDisplayName(Messages.get("gui.portal-settings.access-menu.item-names.is-public"));
        itemMeta.setLore(Messages.getList("gui.portal-settings.access-menu.item-lores.is-public"));
        accessPublic.setItemMeta(itemMeta);

        itemMeta = accessPrivate.getItemMeta();
        itemMeta.setDisplayName(Messages.get("gui.portal-settings.access-menu.item-names.is-private"));
        itemMeta.setLore(Messages.getList("gui.portal-settings.access-menu.item-lores.is-private"));
        accessPrivate.setItemMeta(itemMeta);

        itemMeta = accessLands.getItemMeta();
        itemMeta.setDisplayName(Messages.get("gui.portal-settings.access-menu.item-names.land-access"));
        itemMeta.setLore(Messages.getList("gui.portal-settings.access-menu.item-lores.land-access"));
        accessLands.setItemMeta(itemMeta);

        itemMeta = accessPlayers.getItemMeta();
        itemMeta.setDisplayName(Messages.get("gui.portal-settings.access-menu.item-names.player-access"));
        itemMeta.setLore(Messages.getList("gui.portal-settings.access-menu.item-lores.player-access"));
        accessPlayers.setItemMeta(itemMeta);
    }
}