package lv.theironminerlv.sidesurvivalportals.utils;

import java.util.ArrayList;

import org.bukkit.Location;

public class BlockUtils
{
    public static ArrayList<Location> getBlocksBetween(Location min, Location max) {
        ArrayList<Location> locs = new ArrayList<>();

        for (int i = max.getBlockX(); i <= min.getBlockX(); i++) {
            for (int j = min.getBlockY(); j <= max.getBlockY(); j++) {
                for (int k = min.getBlockZ(); k <= max.getBlockZ(); k++) {
                    locs.add(new Location(min.getWorld(), i, j, k));
                }
            }
        }

        return locs;
    }
}