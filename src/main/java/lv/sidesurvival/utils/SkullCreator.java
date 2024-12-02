package lv.sidesurvival.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.UUID;

public class SkullCreator {

    public static ItemStack itemFromUuid(UUID id) {
        return itemWithUuid(id);
    }

    public static ItemStack itemFromUrl(String url) {
        return itemWithUrl(url);
    }

    public static ItemStack itemFromBase64(String base64) {
        return itemWithBase64(base64);
    }

    public static ItemStack itemWithUuid(UUID id) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta)item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(id));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack itemWithUrl(String url) {
        return itemWithBase64(urlToBase64(url));
    }

    public static ItemStack itemWithBase64(String base64) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        if (!(item.getItemMeta() instanceof SkullMeta)) {
            return null;
        } else {
            SkullMeta meta = (SkullMeta)item.getItemMeta();
            PlayerProfile profile = Bukkit.createProfile(new UUID(0, 0), "");
            profile.setProperty(new ProfileProperty("textures", base64));
            meta.setPlayerProfile(profile);
            item.setItemMeta(meta);
            return item;
        }
    }

    private static String urlToBase64(String url) {
        URI actualUrl;
        try {
            actualUrl = new URI(url);
        } catch (URISyntaxException var3) {
            throw new RuntimeException(var3);
        }

        String toEncode = "{\"textures\":{\"SKIN\":{\"url\":\"" + actualUrl.toString() + "\"}}}";
        return Base64.getEncoder().encodeToString(toEncode.getBytes());
    }
}
