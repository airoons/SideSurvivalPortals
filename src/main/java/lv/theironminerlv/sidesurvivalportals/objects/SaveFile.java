package lv.theironminerlv.sidesurvivalportals.objects;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;

public class SaveFile
{
    protected final boolean createIfNotExist, resource;
    protected final SideSurvivalPortals plugin;

    protected FileConfiguration config;
    protected File file, path;
    protected String name;

    public SaveFile(SideSurvivalPortals plugin, File path, String name, boolean createIfNotExist, boolean resource) {
        this.plugin = plugin;
        this.path = path;
        this.name = name + ".yml";
        this.createIfNotExist = createIfNotExist;
        this.resource = resource;
        create();
    }

    public SaveFile(SideSurvivalPortals plugin, String path, String name, boolean createIfNotExist, boolean resource) {
        this(plugin, new File(path), name, createIfNotExist, resource);
    }
    
    public FileConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public File reloadFile() {
        file = new File(path, name);
        return file;
    }

    public FileConfiguration reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
        return config;
    }

    public void reload() {
        reloadFile();
        reloadConfig();
    }

    public void create() {
        if (file == null) {
            reloadFile();
        }
        if (!createIfNotExist || file.exists()) {
            if (file.exists())
                reloadConfig();
            return;
        }
        file.getParentFile().mkdirs();
        if (resource) {
            plugin.saveResource(name, false);
        } else {
            try {
                file.createNewFile();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        if (config == null) {
            reloadConfig();
        }
    }

    public void delete() {
        if (file != null)
            file.delete();
    }
}