package lv.theironminerlv.sidesurvivalportals;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.minuskube.inv.InventoryManager;
import lv.theironminerlv.sidesurvivalportals.commands.PortalCommand;
import lv.theironminerlv.sidesurvivalportals.data.PortalData;
import lv.theironminerlv.sidesurvivalportals.gui.MenuItems;
import lv.theironminerlv.sidesurvivalportals.listeners.InventoryCloseListener;
import lv.theironminerlv.sidesurvivalportals.listeners.PortalBreakListener;
import lv.theironminerlv.sidesurvivalportals.listeners.PortalCreateListener;
import lv.theironminerlv.sidesurvivalportals.listeners.PortalEnterListener;
import lv.theironminerlv.sidesurvivalportals.managers.DataManager;
import lv.theironminerlv.sidesurvivalportals.managers.MenuManager;
import lv.theironminerlv.sidesurvivalportals.managers.PermissionManager;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;
import me.angeschossen.lands.api.integration.LandsIntegration;

public class SideSurvivalPortals extends JavaPlugin
{
    private static SideSurvivalPortals instance;
    private InventoryManager invManager; // shouldn't be static in the end... (rework needed)
    private PortalManager portalManager;
    private DataManager dataManager;
    private PermissionManager permissionManager;
    private MenuManager menuManager;
    private PortalData portalData;
    private MenuItems menuItems;

    private FileConfiguration config;
    private File portalFolder = new File(this.getDataFolder() + "/portals");

    private LandsIntegration landsAPI;

    public Set<Player> handleClose = new HashSet<Player>();

    public static SideSurvivalPortals getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        config = getConfig();

        this.saveDefaultConfig();

        landsAPI = new LandsIntegration(this);

        dataManager = new DataManager(this);
        portalData = new PortalData(this);
        portalManager = new PortalManager(this);
        permissionManager = new PermissionManager(this);
        menuManager = new MenuManager(this);
        menuItems = new MenuItems(this);

        getLogger().info("SideSurvivalPortals starting!");

        invManager = new InventoryManager(this);
        invManager.init();

        dataManager.loadPortals();

        getServer().getPluginManager().registerEvents(new PortalCreateListener(this), this);
        getServer().getPluginManager().registerEvents(new PortalBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new PortalEnterListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(this), this);

        this.getCommand("p").setExecutor(new PortalCommand());
    }

    public InventoryManager getInvManager() {
        return invManager;
    }

    public PortalData getPortalData() {
        return portalData;
    }

    public PortalManager getPortalManager() {
        return portalManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
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

    public LandsIntegration getLandsAPI() {
        return landsAPI;
    }

    @Override
    public void onDisable() {
        getLogger().info("SideSurvivalPortals stopped!");
    }
}