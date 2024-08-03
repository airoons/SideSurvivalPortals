package lv.sidesurvival.utils;

import java.util.ArrayList;

import org.bukkit.Location;

public class BlockUtils {

    public static ArrayList<Location> getBlocksBetween(Location min, Location max) {
        ArrayList<Location> locs = new ArrayList<>();

        int topBlockX = (Math.max(min.getBlockX(), max.getBlockX()));
        int bottomBlockX = (Math.min(min.getBlockX(), max.getBlockX()));

        int topBlockY = (Math.max(min.getBlockY(), max.getBlockY()));
        int bottomBlockY = (Math.min(min.getBlockY(), max.getBlockY()));

        int topBlockZ = (Math.max(min.getBlockZ(), max.getBlockZ()));
        int bottomBlockZ = (Math.min(min.getBlockZ(), max.getBlockZ()));

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