package lv.theironminerlv.sidesurvivalportals.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.dbassett.skullcreator.SkullCreator;
import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.utils.ConvertUtils;

public class MenuItems
{
    public static SideSurvivalPortals plugin;

    public static ItemStack lightGrayPane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
    public static ItemStack grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    public static ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

    public static ItemStack miniGrass = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzk1ZDM3OTkzZTU5NDA4MjY3ODQ3MmJmOWQ4NjgyMzQxM2MyNTBkNDMzMmEyYzdkOGM1MmRlNDk3NmIzNjIifX19");
    public static ItemStack miniNetherrack = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTliOTRlNWFkOTNkYzdhZGY1OTAwNTZkNGExZTAzNDA5MjUzZGZlY2ZjODhlODMxNTQxYzhkZjU0ZmYwNWNhNiJ9fX0=");
    public static ItemStack miniGlobe = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjFkZDRmZTRhNDI5YWJkNjY1ZGZkYjNlMjEzMjFkNmVmYTZhNmI1ZTdiOTU2ZGI5YzVkNTljOWVmYWIyNSJ9fX0");

    public static ItemStack prevPage = new ItemStack(Material.ARROW);
    public static ItemStack nextPage = new ItemStack(Material.ARROW);

    public static ItemStack editPortalAccess = new ItemStack(Material.NAME_TAG);
    public static ItemStack editPortalDescr = new ItemStack(Material.OAK_SIGN);

    public static ItemStack defaultIcon = new ItemStack(Material.PAPER);
    public static List<ItemStack> availableIcons = new ArrayList<ItemStack>();

    public MenuItems(SideSurvivalPortals plugin) {
        this.plugin = plugin;

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

        itemMeta = prevPage.getItemMeta();
        itemMeta.setDisplayName(ConvertUtils.color("&a&lIepriekšējā lapa"));
        prevPage.setItemMeta(itemMeta);

        itemMeta = nextPage.getItemMeta();
        itemMeta.setDisplayName(ConvertUtils.color("&a&lNākamā lapa"));
        nextPage.setItemMeta(itemMeta);

        itemMeta = editPortalAccess.getItemMeta();
        itemMeta.setDisplayName(ConvertUtils.color("&e&lPiekļuves atļaujas"));
        itemMeta.setLore(ConvertUtils.color(Arrays.asList(
            "",
            "&7Maini, kam ir atļauts izmantot portālu.",
            "",
            "&6Spied, lai atvērtu!")));
        editPortalAccess.setItemMeta(itemMeta);

        itemMeta = editPortalDescr.getItemMeta();
        itemMeta.setDisplayName(ConvertUtils.color("&3&lMainīt aprakstu"));
        editPortalDescr.setItemMeta(itemMeta);

        List<String> configIcons = plugin.getConfiguration().getStringList("availableIcons");
        ItemStack item;
        for (String configIcon : configIcons) {
            item = new ItemStack(Material.valueOf(configIcon));

            itemMeta = item.getItemMeta();
            itemMeta.setLore(ConvertUtils.color(Arrays.asList(
                "&7Spied, lai izvēlētos šo ikonu!")));
            item.setItemMeta(itemMeta);

            availableIcons.add(item);
        }
        
    }
}