package lv.theironminerlv.sidesurvivalportals.managers;

import java.io.File;

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
        save.getConfig().set("isnorthsouth", portal.getNorthSouth());
        save.getConfig().set("landid", portal.getLand().getId());
        save.getConfig().set("settings.icon", portal.getIcon().getType().toString());
        
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
        World world;
        Land land;
        String icon;

        if (portalFiles.length > 0) {
            for (File file : portalFiles) {
                portal =  null;

                save = YamlConfiguration.loadConfiguration(file);
                if (!save.contains("landid") || !save.contains("pos1") || !save.contains("pos2") || !save.contains("id") || !save.contains("isnorthsouth") || !save.contains("settings.icon"))
                    continue;

                land = landsAPI.getLand((int)save.getInt("landid"));

                pos1 = LocationSerialization.getLocationFromString(save.getString("pos1"));
                pos2 = LocationSerialization.getLocationFromString(save.getString("pos2"));
                world = Bukkit.getWorld(save.getString("world"));
                isNorthSouth = save.getBoolean("isnorthsouth");
                icon = save.getString("settings.icon");

                portal = new Portal(pos1, pos2, world, isNorthSouth, land, save.getString("id"), icon);

                if (portal != null && land != null)
                    PortalData.addPortal(portal, false);
            }
        }
    }
}