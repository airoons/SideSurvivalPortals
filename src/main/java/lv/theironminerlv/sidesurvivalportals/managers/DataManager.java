package lv.theironminerlv.sidesurvivalportals.managers;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lv.sidesurvival.SurvivalCoreBukkit;
import lv.sidesurvival.managers.ClaimManager;
import lv.sidesurvival.objects.ClaimOwner;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import lv.theironminerlv.sidesurvivalportals.SurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.data.PortalData;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.LocationSerialization;
import org.bukkit.scheduler.BukkitRunnable;

public class DataManager {

    private final SurvivalPortals plugin;

    public DataManager(SurvivalPortals plugin) {
        this.plugin = plugin;
    }

    public void save(Portal portal) {
        new BukkitRunnable() {
            @Override
            public void run() {
                MongoCollection<Document> col = MongoManager.get().getPortalCollection();
                Document doc = new Document("_id", portal.getId());
                Document found = col.find(doc).first();

                doc.put("world", portal.getWorld().getName());
                doc.put("pos1", LocationSerialization.getStringFromLocation(portal.getPos1(), false));
                doc.put("pos2", LocationSerialization.getStringFromLocation(portal.getPos2(), false));
                doc.put("tploc", LocationSerialization.getStringFromLocation(portal.getTpLoc(), true));
                doc.put("isNorthSouth", portal.getNorthSouth());
                doc.put("isPublic", portal.getIsPublic());
                doc.put("ownerId", portal.getOwner());
                doc.put("settings-icon", portal.getIcon().getType().toString());
                doc.put("settings-desc", portal.getDescription());
                doc.put("settings-allowedGroups", portal.getAllowedGroups());
                doc.put("settings-allowedPlayers", portal.getAllowedPlayers());

                if (found != null) {
                    doc.remove("_id");
                    Document update = new Document("$set", doc);
                    col.updateOne(found, update, new UpdateOptions().upsert(true));
                } else {
                    col.insertOne(doc);
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        SurvivalCoreBukkit.getInstance().getProtonManager().broadcast("survivalportals", "portalUpdated", portal.getId());
                    }
                }.runTask(plugin);
            }
        }.runTaskAsynchronously(plugin);
    }

    public void saveAccess(Portal portal) {
        new BukkitRunnable() {
            @Override
            public void run() {
                MongoCollection<Document> col = MongoManager.get().getPortalCollection();
                Document doc = new Document("_id", portal.getId());
                Document found = col.find(doc).first();
                if (found == null) {
                    return;
                }

                doc.put("settings-allowedGroups", portal.getAllowedGroups());
                doc.put("settings-allowedPlayers", portal.getAllowedPlayers());

                Document update = new Document("$set", doc);
                col.updateOne(found, update, new UpdateOptions().upsert(true));

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        SurvivalCoreBukkit.getInstance().getProtonManager().broadcast("survivalportals", "portalUpdated", portal.getId());
                    }
                }.runTask(plugin);
            }
        }.runTaskAsynchronously(plugin);
    }

    public void delete(Portal portal) {
        new BukkitRunnable() {
            @Override
            public void run() {
                MongoManager.get().getPortalCollection().deleteOne(Filters.eq("_id", portal.getId()));
            }
        }.runTaskAsynchronously(plugin);
    }

    public void loadPortals() {
        Portal portal;
        Location pos1;
        Location pos2;
        boolean isNorthSouth;
        boolean isPublic;
        String locStr;
        String worldStr;
        World world;
        String owner;
        String icon;
        String desc;
        List<String> allowedGroups;
        List<String> allowedPlayers;

        try (MongoCursor<Document> cursor = MongoManager.get().getPortalCollection().find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();

                owner = doc.getString("ownerId");

                locStr = doc.getString("pos1");
                pos1 = LocationSerialization.getLocationFromString(locStr);
                pos2 = LocationSerialization.getLocationFromString(doc.getString("pos2"));
                worldStr = doc.getString("world");
                if (worldStr == null) {
                    plugin.getLogger().warning("Couldn't load portal " + doc.getString("_id") + " (" + owner + ")");
                    continue;
                }

                world = Bukkit.getWorld(worldStr);
                isNorthSouth = doc.getBoolean("isNorthSouth");
                isPublic = doc.getBoolean("isPublic");
                icon = doc.getString("settings-icon");
                desc = doc.getString("settings-desc");

                allowedGroups = doc.getList("settings-allowedGroups", String.class, new ArrayList<>());
                allowedPlayers = doc.getList("settings-allowedPlayers", String.class, new ArrayList<>());

                portal = new Portal(pos1, pos2, world, locStr, worldStr, isNorthSouth, isPublic, owner, doc.getString("_id"), icon, desc, allowedGroups, allowedPlayers);
                ClaimOwner claimOwner = ClaimManager.get().getOwnerById(owner);

                if (claimOwner != null)
                    PortalData.addPortal(portal, false);
            }
        }
    }

    public void updateOne(String portalId) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Document doc = MongoManager.get().getPortalCollection().find(Filters.eq("_id", portalId)).first();
                if (doc == null)
                    return;

                String owner = doc.getString("ownerId");

                String locStr = doc.getString("pos1");
                Location pos1 = LocationSerialization.getLocationFromString(locStr);
                Location pos2 = LocationSerialization.getLocationFromString(doc.getString("pos2"));
                String worldStr = doc.getString("world");
                World world = Bukkit.getWorld(worldStr);
                boolean isNorthSouth = doc.getBoolean("isNorthSouth");
                boolean isPublic = doc.getBoolean("isPublic");
                String icon = doc.getString("settings-icon");
                String desc = doc.getString("settings-desc");

                List<String> allowedGroups = doc.getList("settings-allowedGroups", String.class, new ArrayList<>());
                List<String> allowedPlayers = doc.getList("settings-allowedPlayers", String.class, new ArrayList<>());

                Portal portal = new Portal(pos1, pos2, world, locStr, worldStr, isNorthSouth, isPublic, owner, doc.getString("_id"), icon, desc, allowedGroups, allowedPlayers);
                ClaimOwner claimOwner = ClaimManager.get().getOwnerById(owner);
                if (claimOwner == null)
                    return;

                PortalData.updatePortal(portal);
            }
        }.runTaskAsynchronously(plugin);
    }
}