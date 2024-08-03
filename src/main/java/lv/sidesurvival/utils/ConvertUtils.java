package lv.sidesurvival.utils;

import com.sk89q.worldedit.math.BlockVector3;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConvertUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&(#[a-f0-9]{6})", 2);

    public static BlockVector3 toBlockVector3(Location loc) {
        return BlockVector3.at(loc.getX(), loc.getY(), loc.getZ());
    }
  
    public static List<String> color(List<String> input) {
        for (int i = 0; i < input.size(); i++) {
            input.set(i, color(input.get(i)));
        }

        return input;
    }

    public static String[] color(String[] input) {
        for (int i = 0; i < input.length; i++) {
            input[i] = color(input[i]);
        }

        return input;
    }

    public static String color(String input) {
        Matcher m = HEX_PATTERN.matcher(input);
        try {
            ChatColor.class.getDeclaredMethod("of", new Class[] { String.class });
            while (m.find())
                input = input.replace(m.group(), ChatColor.of(m.group(1)).toString()); 
        } catch (Exception e) {
            while (m.find())
                input = input.replace(m.group(), ""); 
        } 
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String readableLoc(Location loc) {
        return loc.getWorld().getName() + ", X: " + loc.getBlockX() + ", Y: " + loc.getBlockY() + ", Z: " + loc.getBlockZ();
    }

    public static String readableLocStr(String locStr) {
        final String[] parts = locStr.split(";");
        String w = parts[0];
        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);
        double z = Double.parseDouble(parts[3]);

        return w + ", X: " + x + ", Y: " + y + ", Z: " + z;
    }
}