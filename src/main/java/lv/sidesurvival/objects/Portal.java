package lv.sidesurvival.objects;

import java.util.ArrayList;
import java.util.List;

import lv.sidesurvival.gui.MenuItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.meta.ItemMeta;

public class Portal {

    private String id;
    private final Location pos1;
    private final Location pos2;
    private Location tpLoc;
    private String locStr;
    private final String worldStr;
    private final World world;
    private boolean isPublic = false;
    private final boolean isNorthSouth;
    private final String owner;
    private ItemStack icon = MenuItems.defaultIcon.clone();
    private String desc = "-";
    private List<String> allowedGroups = new ArrayList<>();
    private List<String> allowedPlayers = new ArrayList<>();

    // When first creating a portal
    public Portal(Location pos1, Location pos2, World world, boolean isNorthSouth, String owner) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.tpLoc = pos1;
        this.worldStr = world.getName();
        this.world = world;
        this.isNorthSouth = isNorthSouth;
        this.owner = owner;
    }

    // From savefile
    public Portal(Location pos1, Location pos2, World world, String locStr, String worldStr, boolean isNorthSouth, boolean isPublic, String owner, String id, String icon, String desc, List<String> allowedGroups, List<String> allowedPlayers) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.tpLoc = pos1;
        this.locStr = locStr;
        this.worldStr = worldStr;
        this.world = world;
        this.isNorthSouth = isNorthSouth;
        this.isPublic = isPublic;
        this.owner = owner;
        this.id = id;

        this.icon = new ItemStack(Material.valueOf(icon));
        ItemMeta meta = this.getIcon().getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        this.icon.setItemMeta(meta);
        this.desc = desc;
        this.allowedGroups = allowedGroups;
        this.allowedPlayers = allowedPlayers;
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

    public String getLocStr() {
        return locStr;
    }

    public String getWorldStr() {
        return worldStr;
    }

    public String getOwner() {
        return owner;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public String getDescription() {
        return desc;
    }

    public List<String> getAllowedGroups() {
        return allowedGroups;
    }

    public List<String> getAllowedPlayers() {
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

    public void setAllowedGroups(List<String> allowedGroups) {
        this.allowedGroups = allowedGroups;
    }

    public void setAllowedPlayers(List<String> allowedPlayers) {
        this.allowedPlayers = allowedPlayers;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}