package lv.theironminerlv.sidesurvivalportals.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerialization
{
    public static String getStringFromLocation(Location loc) {
        if (loc == null) {
            return "";
        }
        return loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
    }

    public static Location getLocationFromString(String s) {
        if (s == null || s.trim() == "") {
            return null;
        }

        final String[] parts = s.split(";");
        
        if (parts.length == 4) {
            World w = Bukkit.getServer().getWorld(parts[0]);
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            return new Location(w, x, y, z);
        }
        return null;
    }
}