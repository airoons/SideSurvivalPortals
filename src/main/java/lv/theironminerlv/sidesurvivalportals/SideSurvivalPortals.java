package lv.theironminerlv.sidesurvivalportals;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.minuskube.inv.InventoryManager;
import lv.theironminerlv.sidesurvivalportals.commands.test;
import lv.theironminerlv.sidesurvivalportals.data.PortalData;
import lv.theironminerlv.sidesurvivalportals.listeners.PortalBreakListener;
import lv.theironminerlv.sidesurvivalportals.listeners.PortalCreateListener;
import lv.theironminerlv.sidesurvivalportals.listeners.PortalEnterListener;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;

public class SideSurvivalPortals extends JavaPlugin
{
    private static SideSurvivalPortals instance;
    private static InventoryManager invManager; // shouldn't be static in the end... (rework needed)
    private PortalManager portalManager;
    private PortalData portalData;
    private FileConfiguration config = getConfig();

    public static SideSurvivalPortals getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        portalData = new PortalData(this);
        portalManager = new PortalManager(this);
        getLogger().info("SideSurvivalPortals starting!");

        this.saveDefaultConfig();

        this.getCommand("test").setExecutor(new test());

        getServer().getPluginManager().registerEvents(new PortalCreateListener(this), this);
        getServer().getPluginManager().registerEvents(new PortalBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new PortalEnterListener(this), this);

        invManager = new InventoryManager(this);
        invManager.init();
    }

    public static InventoryManager getInvManager() {
        return invManager;
    }

    public PortalData getPortalData() {
        return this.portalData;
    }

    public PortalManager getPortalManager() {
        return portalManager;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    @Override
    public void onDisable() {
        getLogger().info("SideSurvivalPortals stopped!");
    }
}