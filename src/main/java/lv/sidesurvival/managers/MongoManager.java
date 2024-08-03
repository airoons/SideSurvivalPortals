package lv.sidesurvival.managers;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lv.sidesurvival.SurvivalPortals;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoManager {

    private final SurvivalPortals plugin;
    private static MongoManager instance;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private MongoCollection<Document> portalCollection;

    public MongoManager(SurvivalPortals zemes){
        this.plugin = zemes;
        instance = this;
    }

    public static MongoManager get() {
        if (instance == null)
            new MongoManager(SurvivalPortals.getInstance());

        return instance;
    }

    public boolean connect() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        try {
            if (this.plugin.getConfig().getBoolean("MONGODB.AUTHENTICATION.ENABLED")) {
                MongoCredential credential = MongoCredential.createCredential(
                        this.plugin.getConfig().getString("MONGODB.AUTHENTICATION.USERNAME"),
                        this.plugin.getConfig().getString("MONGODB.AUTHENTICATION.DATABASE"),
                        this.plugin.getConfig().getString("MONGODB.AUTHENTICATION.PASSWORD").toCharArray()
                );

                MongoClientSettings clientSettings = MongoClientSettings.builder()
                        .credential(credential)
                        .applyToClusterSettings(builder ->
                                builder.hosts(Collections.singletonList(new ServerAddress(this.plugin.getConfig().getString("MONGODB.ADDRESS"),
                                        this.plugin.getConfig().getInt("MONGODB.PORT")))))
                        .build();
                mongoClient = MongoClients.create(clientSettings);
            } else {
                MongoClientSettings clientSettings = MongoClientSettings.builder().applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(new ServerAddress(this.plugin.getConfig().getString("MONGODB.ADDRESS"),
                                this.plugin.getConfig().getInt("MONGODB.PORT")))))
                        .build();
                mongoClient = MongoClients.create(clientSettings);
            }

            mongoDatabase = mongoClient.getDatabase(this.plugin.getConfig().getString("MONGODB.DATABASE"));

            portalCollection = mongoDatabase.getCollection("Portals");
//            claimCollection.createIndex(Indexes.ascending("world"));
//            claimCollection.createIndex(Indexes.ascending("id"));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(this.plugin);
            return false;
        }
    }

    public void disconnect() {
        mongoClient.close();
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public MongoCollection<Document> getPortalCollection() {
        return portalCollection;
    }
}
