package lv.theironminerlv.sidesurvivalportals.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerialization
{
    public static String getStringFromLocation(Location loc, boolean strict) {
        if (loc == null) {
            return "";
        }
        if (strict)
            return loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ();
        else
            return loc.getWorld().getName() + ";" + loc.getBlockX() + ";" + loc.getBlockY() + ";" + loc.getBlockZ();
    }

    public static Location getLocationFromString(String s) {
        if (s == null || s.trim() == "") {
            return null;
        }

        final String[] parts = s.split(";");
        
        if (parts.length >= 4) {
            World w = Bukkit.getServer().getWorld(parts[0]);
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);

            if (parts.length == 4)
                return new Location(w, x, y, z);
            if (parts.length != 6)
                return null;
            
            float yaw = Float.parseFloat(parts[4]);
            float pitch = Float.parseFloat(parts[5]);
            return new Location(w, x, y, z, yaw, pitch);
        }

        return null;
    }
}