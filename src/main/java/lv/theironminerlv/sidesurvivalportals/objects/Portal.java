package lv.theironminerlv.sidesurvivalportals.objects;

import org.bukkit.Location;
import org.bukkit.World;

import me.angeschossen.lands.api.land.Land;

public class Portal
{
    private String id;
    private Location pos1;
    private Location pos2;
    private Location tpLoc;
    private World world;
    // private boolean isPublic = false;
    private boolean isNorthSouth;
    private Land land;

    public Portal(Location pos1, Location pos2, World world, boolean isNorthSouth, Land land) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.tpLoc = pos1;
        this.world = world;
        this.isNorthSouth = isNorthSouth;
        this.land = land;
    }

    public Portal(Location pos1, Location pos2, World world, boolean isNorthSouth, Land land, String id) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.tpLoc = pos1;
        this.world = world;
        this.isNorthSouth = isNorthSouth;
        this.land = land;
        this.id = id;
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

    public boolean getNorthSouth() {
        return isNorthSouth;
    }

    public World getWorld() {
        return world;
    }

    public Land getLand() {
        return land;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTpLoc(Location loc) {
        this.tpLoc = loc;
    }
}