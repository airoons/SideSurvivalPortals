package lv.theironminerlv.sidesurvivalportals.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.data.PortalData;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.objects.SaveFile;
import lv.theironminerlv.sidesurvivalportals.utils.LocationSerialization;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Land;

public class DataManager
{
    private SideSurvivalPortals plugin;
    private static LandsIntegration landsAPI;

    public DataManager(SideSurvivalPortals plugin) {
        this.plugin = plugin;
        landsAPI = this.plugin.getLandsAPI();
    }

    public void save(Portal portal) {
        SaveFile save = new SaveFile(plugin, plugin.getPortalFolder(), portal.getId(), true, false);

        save.getConfig().set("id", portal.getId());
        save.getConfig().set("world", portal.getWorld().getName());
        save.getConfig().set("pos1", LocationSerialization.getStringFromLocation(portal.getPos1(), false));
        save.getConfig().set("pos2", LocationSerialization.getStringFromLocation(portal.getPos2(), false));
        save.getConfig().set("tploc", LocationSerialization.getStringFromLocation(portal.getTpLoc(), true));
        save.getConfig().set("isNorthSouth", portal.getNorthSouth());
        save.getConfig().set("isPublic", portal.getIsPublic());
        save.getConfig().set("landId", portal.getLand().getId());
        save.getConfig().set("settings.icon", portal.getIcon().getType().toString());
        save.getConfig().set("settings.desc", portal.getDescription());
        save.getConfig().set("settings.allowedLands", portal.getAllowedLands());

        List<UUID> allowedPlayers = portal.getAllowedPlayers();
        List<String> allowedPlayersStr = new ArrayList<String>();
        
        for (UUID uuid : allowedPlayers) {
            allowedPlayersStr.add(uuid.toString());
        }
        save.getConfig().set("settings.allowedPlayers", allowedPlayersStr);
        
        save.save();
    }

    public void delete(Portal portal) {
        SaveFile save = new SaveFile(plugin, plugin.getPortalFolder(), portal.getId(), true, false);
        save.delete();
    }

    public void loadPortals() {
        File[] portalFiles = plugin.getPortalFolder().listFiles();
        FileConfiguration save;
        Portal portal;
        Location pos1;
        Location pos2;
        boolean isNorthSouth;
        boolean isPublic;
        World world;
        Land land;
        String icon;
        String desc;
        List<Integer> allowedLands;
        List<String> allowedPlayers;

        if (portalFiles.length > 0) {
            for (File file : portalFiles) {
                portal =  null;

                save = YamlConfiguration.loadConfiguration(file);
                if (!save.contains("landId") || !save.contains("pos1") || !save.contains("pos2") || !save.contains("id") || !save.contains("isNorthSouth") || !save.contains("isPublic") || !save.contains("settings.icon")|| !save.contains("settings.desc") || !save.contains("settings.allowedLands") || !save.contains("settings.allowedPlayers"))
                    continue;

                land = landsAPI.getLand((int)save.getInt("landId"));

                pos1 = LocationSerialization.getLocationFromString(save.getString("pos1"));
                pos2 = LocationSerialization.getLocationFromString(save.getString("pos2"));
                world = Bukkit.getWorld(save.getString("world"));
                isNorthSouth = save.getBoolean("isNorthSouth");
                isPublic = save.getBoolean("isPublic");
                icon = save.getString("settings.icon");
                desc = save.getString("settings.desc");

                allowedLands = save.getIntegerList("settings.allowedLands");
                allowedPlayers = save.getStringList("settings.allowedPlayers");

                portal = new Portal(pos1, pos2, world, isNorthSouth, isPublic, land, save.getString("id"), icon, desc, allowedLands, allowedPlayers);

                if (portal != null && land != null)
                    PortalData.addPortal(portal, false);
            }
        }
    }
}