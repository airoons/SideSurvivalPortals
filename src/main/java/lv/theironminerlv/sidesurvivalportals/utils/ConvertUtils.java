package lv.theironminerlv.sidesurvivalportals.utils;

import com.sk89q.worldedit.math.BlockVector3;

import org.bukkit.Location;

public class ConvertUtils
{
    public static BlockVector3 toBlockVector3(Location loc) {
        return BlockVector3.at(loc.getX(), loc.getY(), loc.getZ());
    }
}