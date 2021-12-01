package lv.theironminerlv.sidesurvivalportals;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import lv.sidesurvival.SurvivalCoreBukkit;
import lv.sidesurvival.listeners.ProtonListener;
import lv.theironminerlv.sidesurvivalportals.listeners.*;
import lv.theironminerlv.sidesurvivalportals.managers.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.minuskube.inv.InventoryManager;
import lv.theironminerlv.sidesurvivalportals.commands.PortalCommand;
import lv.theironminerlv.sidesurvivalportals.data.PortalData;
import lv.theironminerlv.sidesurvivalportals.gui.MenuItems;
import lv.theironminerlv.sidesurvivalportals.utils.Messages;

public class SurvivalPortals extends JavaPlugin {

    private static SurvivalPortals instance;
    private InventoryManager invManager;
    private DataManager dataManager;
    private PortalManager portalManager;
    private PermissionManager permissionManager;
    private MenuManager menuManager;
    private PortalData portalData;

    private FileConfiguration config;
    private File portalFolder = new File(this.getDataFolder() + "/portals");

    public Set<Player> handleClose = new HashSet<Player>();

    public static SurvivalPortals getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        config = getConfig();

        this.saveDefaultConfig();

        Messages messages = new Messages();
        messages.load(config.getConfigurationSection("messages"));

        dataManager = new DataManager(this);
        portalData = new PortalData(this);
        portalManager = new PortalManager(this);
        permissionManager = new PermissionManager(this);
        menuManager = new MenuManager(this);
        new MenuItems();

        getLogger().info("Starting!");

        if (!MongoManager.get().connect()) {
            return;
        }
        invManager = new InventoryManager(this);
        invManager.init();

        dataManager.loadPortals();

        getServer().getPluginManager().registerEvents(new PortalCreateListener(this), this);
        getServer().getPluginManager().registerEvents(new PortalBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new PortalEnterListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(this), this);

        CrossServerHandler csHandler = new CrossServerHandler(this);
        getServer().getPluginManager().registerEvents(csHandler, this);
        SurvivalCoreBukkit.getInstance().getProtonManager().registerMessageHandlers(csHandler);

        this.getCommand("p").setExecutor(new PortalCommand());

        getLogger().info("Startup successful!");
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

    @Override
    public void onDisable() {
        getLogger().info("Stopped!");
    }
}