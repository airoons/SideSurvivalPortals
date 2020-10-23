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

public class DataManager
{
    private SideSurvivalPortals plugin;

    public DataManager(SideSurvivalPortals plugin) {
        this.plugin = plugin;
    }

    public void save(Portal portal) {
        SaveFile save = new SaveFile(plugin, plugin.getPortalFolder(), portal.getId(), true, false);

        save.getConfig().set("id", portal.getId());
        save.getConfig().set("world", portal.getWorld().getName());
        save.getConfig().set("pos1", LocationSerialization.getStringFromLocation(portal.getPos1()));
        save.getConfig().set("pos2", LocationSerialization.getStringFromLocation(portal.getPos2()));
        save.getConfig().set("tploc", LocationSerialization.getStringFromLocation(portal.getTpLoc()));
        
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
        Location tpLoc;
        World world;

        if (portalFiles.length > 0) {
            for (File file : portalFiles) {
                portal =  null;
                save = YamlConfiguration.loadConfiguration(file);

                pos1 = LocationSerialization.getLocationFromString(save.getString("pos1"));
                pos2 = LocationSerialization.getLocationFromString(save.getString("pos2"));
                tpLoc = LocationSerialization.getLocationFromString(save.getString("tploc"));
                world = Bukkit.getWorld(save.getString("world"));

                portal = new Portal(pos1, pos2, world, tpLoc, save.getString("id"));

                if (portal != null)
                    PortalData.addPortal(portal, false);
            }
        }
    }
}