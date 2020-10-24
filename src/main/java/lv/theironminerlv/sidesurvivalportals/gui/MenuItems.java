package lv.theironminerlv.sidesurvivalportals.gui;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.dbassett.skullcreator.SkullCreator;
import lv.theironminerlv.sidesurvivalportals.utils.ConvertUtils;

public class MenuItems
{
    public static ItemStack lightGrayPane = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
    public static ItemStack grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    public static ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

    public static ItemStack miniGrass = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzk1ZDM3OTkzZTU5NDA4MjY3ODQ3MmJmOWQ4NjgyMzQxM2MyNTBkNDMzMmEyYzdkOGM1MmRlNDk3NmIzNjIifX19");
    public static ItemStack miniNetherrack = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTliOTRlNWFkOTNkYzdhZGY1OTAwNTZkNGExZTAzNDA5MjUzZGZlY2ZjODhlODMxNTQxYzhkZjU0ZmYwNWNhNiJ9fX0=");
    public static ItemStack miniGlobe = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjFkZDRmZTRhNDI5YWJkNjY1ZGZkYjNlMjEzMjFkNmVmYTZhNmI1ZTdiOTU2ZGI5YzVkNTljOWVmYWIyNSJ9fX0");

    public static ItemStack prevPage = new ItemStack(Material.ARROW);
    public static ItemStack nextPage = new ItemStack(Material.ARROW);

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

        itemMeta = prevPage.getItemMeta();
        itemMeta.setDisplayName("&a&lIepriekšējā lapa");
        prevPage.setItemMeta(itemMeta);

        itemMeta = nextPage.getItemMeta();
        itemMeta.setDisplayName("&a&lNākamā lapa");
        nextPage.setItemMeta(itemMeta);
    }
}