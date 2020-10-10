package lv.theironminerlv.sidesurvivalportals.objects;

import org.bukkit.Location;
import org.bukkit.World;

public class Portal
{
    private String id;
    private Location pos1;
    private Location pos2;
    private World world;
    private boolean isPublic = false;
    private Location tpLoc;

    public Portal(Location pos1, Location pos2, World world, Location tpLoc) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.world = world;
        this.tpLoc = tpLoc;
    }

    public String getId() {
        return id;
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public Location getTpLoc() {
        return tpLoc;
    }

    public World getWorld() {
        return world;
    }

    public void setId(String id) {
        this.id = id;
    }
}