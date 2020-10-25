package lv.theironminerlv.sidesurvivalportals.objects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import lv.theironminerlv.sidesurvivalportals.gui.MenuItems;
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
    private ItemStack icon;

    // When first creating a portal
    public Portal(Location pos1, Location pos2, World world, boolean isNorthSouth, Land land) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.tpLoc = pos1;
        this.world = world;
        this.isNorthSouth = isNorthSouth;
        this.land = land;
        this.icon = MenuItems.defaultIcon.clone();
    }

    // From savefile
    public Portal(Location pos1, Location pos2, World world, boolean isNorthSouth, Land land, String id, String icon) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.tpLoc = pos1;
        this.world = world;
        this.isNorthSouth = isNorthSouth;
        this.land = land;
        this.id = id;

        this.icon = new ItemStack(Material.valueOf(icon));
        
        if (this.icon == null)
            this.icon = MenuItems.defaultIcon.clone();;
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

    public ItemStack getIcon() {
        return icon;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTpLoc(Location loc) {
        this.tpLoc = loc;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }
}