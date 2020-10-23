package lv.theironminerlv.sidesurvivalportals;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.minuskube.inv.InventoryManager;
import lv.theironminerlv.sidesurvivalportals.commands.test;
import lv.theironminerlv.sidesurvivalportals.data.PortalData;
import lv.theironminerlv.sidesurvivalportals.gui.MenuItems;
import lv.theironminerlv.sidesurvivalportals.listeners.PortalBreakListener;
import lv.theironminerlv.sidesurvivalportals.listeners.PortalCreateListener;
import lv.theironminerlv.sidesurvivalportals.listeners.PortalEnterListener;
import lv.theironminerlv.sidesurvivalportals.managers.DataManager;
import lv.theironminerlv.sidesurvivalportals.managers.MenuManager;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;

public class SideSurvivalPortals extends JavaPlugin
{
    private static SideSurvivalPortals instance;
    private static InventoryManager invManager; // shouldn't be static in the end... (rework needed)
    private PortalManager portalManager;
    private DataManager dataManager;
    private MenuManager menuManager;
    private PortalData portalData;
    private MenuItems menuItems;

    private FileConfiguration config;
    private File portalFolder = new File(this.getDataFolder() + "/portals");

    public static SideSurvivalPortals getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        config = getConfig();
        dataManager = new DataManager(this);
        portalData = new PortalData(this);
        portalManager = new PortalManager(this);
        menuManager = new MenuManager(this);
        menuItems = new MenuItems();

        getLogger().info("SideSurvivalPortals starting!");

        this.saveDefaultConfig();

        this.getCommand("test").setExecutor(new test());

        getServer().getPluginManager().registerEvents(new PortalCreateListener(this), this);
        getServer().getPluginManager().registerEvents(new PortalBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new PortalEnterListener(this), this);

        invManager = new InventoryManager(this);
        invManager.init();

        dataManager.loadPortals();
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

    public DataManager getDataManager() {
        return dataManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public FileConfiguration getConfiguration() {
        return config;
    }

    public File getPortalFolder() {
        return portalFolder;
    }

    @Override
    public void onDisable() {
        getLogger().info("SideSurvivalPortals stopped!");
    }
}