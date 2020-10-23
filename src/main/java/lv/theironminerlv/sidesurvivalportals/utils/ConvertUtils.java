package lv.theironminerlv.sidesurvivalportals.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sk89q.worldedit.math.BlockVector3;

import org.bukkit.Location;

import net.md_5.bungee.api.ChatColor;

public class ConvertUtils
{
    private static Pattern HEX_PATTERN = Pattern.compile("&(#[a-f0-9]{6})", 2);

    public static BlockVector3 toBlockVector3(Location loc) {
        return BlockVector3.at(loc.getX(), loc.getY(), loc.getZ());
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
}