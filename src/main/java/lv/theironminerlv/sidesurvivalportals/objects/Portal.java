package lv.theironminerlv.sidesurvivalportals.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private boolean isPublic = false;
    private boolean isNorthSouth;
    private Land land;
    private ItemStack icon = MenuItems.defaultIcon.clone();
    private String desc = "-";
    private List<Integer> allowedLands = new ArrayList<Integer>();
    private List<UUID> allowedPlayers = new ArrayList<UUID>();

    // When first creating a portal
    public Portal(Location pos1, Location pos2, World world, boolean isNorthSouth, Land land) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.tpLoc = pos1;
        this.world = world;
        this.isNorthSouth = isNorthSouth;
        this.land = land;
    }

    // From savefile
    public Portal(Location pos1, Location pos2, World world, boolean isNorthSouth, boolean isPublic, Land land, String id, String icon, String desc, List<Integer> allowedLands, List<String> allowedPlayers) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.tpLoc = pos1;
        this.world = world;
        this.isNorthSouth = isNorthSouth;
        this.isPublic = isPublic;
        this.land = land;
        this.id = id;

        this.icon = new ItemStack(Material.valueOf(icon));
        this.desc = desc;
        this.allowedLands = allowedLands;
        
        for (String loopPlayer : allowedPlayers) {
            this.allowedPlayers.add(UUID.fromString(loopPlayer));
        }

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

    public boolean getIsPublic() {
		return isPublic;
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

    public String getDescription() {
        return desc;
    }

    public List<Integer> getAllowedLands() {
        return allowedLands;
    }

    public List<UUID> getAllowedPlayers() {
        return allowedPlayers;
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

    public void setDescription(String desc) {
        this.desc = desc;
    }

    public void setAllowedlands(List<Integer> allowedLands) {
        this.allowedLands = allowedLands;
    }

    public void setAllowedPlayers(List<UUID> allowedPlayers) {
        this.allowedPlayers = allowedPlayers;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}