package lv.theironminerlv.sidesurvivalportals.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;

public class DatabaseManager {
    private SideSurvivalPortals plugin;
    private FileConfiguration config;

    private String host, port, database, user, password;

    private Connection connection;

    public DatabaseManager(SideSurvivalPortals plugin) {
        this.plugin = plugin;
        config = this.plugin.getConfig();

        host = config.getString("mysql.host");
        port = config.getString("mysql.port");
        database = config.getString("mysql.database");
        user = config.getString("mysql.user");
        password = config.getString("mysql.password");

        (new BukkitRunnable() {
            @Override
            public void run() {
                 try {
                      if (connection != null && !connection.isClosed()) {
                          connection.createStatement().execute("SELECT 1");
                      }
                 } catch (SQLException e) {
                     connection = getNewConnection();
                 }
            }
        }).runTaskTimerAsynchronously(plugin, 60 * 20, 60 * 20);
    }

    private Connection getNewConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
      
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database;
            Connection connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            return null;
        }
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public boolean checkConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = getNewConnection();
    
            if (connection == null || connection.isClosed()) {
                return false;
            }
            execute("CREATE TABLE IF NOT EXISTS portals");
        }
        return true;
    }

    public boolean init() {
        try {
            return checkConnection();
        } catch (SQLException e) {
            // Handle Possible exception caused by syntax or other reasons.
            return false;
        }
     }

    public boolean execute(String sql) throws SQLException {
        boolean success = connection.createStatement().execute(sql);
        return success;
    }
}