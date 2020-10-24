package lv.theironminerlv.sidesurvivalportals.utils;

import java.util.ArrayList;

import org.bukkit.Location;

public class BlockUtils
{
    public static ArrayList<Location> getBlocksBetween(Location min, Location max) {
        ArrayList<Location> locs = new ArrayList<>();

        int topBlockX = (min.getBlockX() < max.getBlockX() ? max.getBlockX() : min.getBlockX());
        int bottomBlockX = (min.getBlockX() > max.getBlockX() ? max.getBlockX() : min.getBlockX());

        int topBlockY = (min.getBlockY() < max.getBlockY() ? max.getBlockY() : min.getBlockY());
        int bottomBlockY = (min.getBlockY() > max.getBlockY() ? max.getBlockY() : min.getBlockY());

        int topBlockZ = (min.getBlockZ() < max.getBlockZ() ? max.getBlockZ() : min.getBlockZ());
        int bottomBlockZ = (min.getBlockZ() > max.getBlockZ() ? max.getBlockZ() : min.getBlockZ());

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    locs.add(new Location(min.getWorld(), x, y, z));
                }
            }
        }

        return locs;
    }
}