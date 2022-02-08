package lv.theironminerlv.sidesurvivalportals.listeners;

import lv.sidesurvival.SurvivalCoreBukkit;
import lv.sidesurvival.api.SurvivalCoreAPI;
import lv.theironminerlv.sidesurvivalportals.SurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.data.PortalData;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.objects.PortalRequest;
import me.drepic.proton.common.message.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;

public class CrossServerHandler implements Listener {

    private SurvivalPortals plugin;
    public static Map<String, Portal> requests = new HashMap<>();
    public static Map<String, PortalRequest> tpRequests = new HashMap<>();

    public CrossServerHandler(SurvivalPortals plugin) {
        this.plugin = plugin;
    }

    @MessageHandler(namespace = "survivalportals", subject = "portalUpdated")
    public void onPortalUpdated(String portalId) {
        plugin.getDataManager().updateOne(portalId);
    }

    @MessageHandler(namespace = "survivalportals", subject = "portalDeleted")
    public void onPortalDeleted(String portalId) {
        PortalData.removePortalById(portalId, false);
    }




    @MessageHandler(namespace = "survivalportals", subject = "locationSafeRequest")
    public void onLocationSafeRequest(PortalRequest request) {
        Portal portal = PortalData.CACHED_PORTALS.get(request.getPortalId());
        if (portal != null && portal.getPos1() != null) {
            Location loc = plugin.getPortalManager().getFinalSafeLoc(null, portal);
            String playerServer = SurvivalCoreAPI.getPlayerServer(request.getPlayer());
            if (playerServer == null)
                return;

            PortalRequest reply = new PortalRequest(request.getPlayer(), request.getUuid(), request.getPortalId(), (loc != null));
            SurvivalCoreBukkit.getInstance().getProtonManager().send("survivalportals", "locationSafeReply", reply, playerServer);
        }
    }

    @MessageHandler(namespace = "survivalportals", subject = "locationSafeReply")
    public void onLocationSafeReply(PortalRequest request) {
        if (!requests.containsKey(request.getUuid()) || !requests.get(request.getUuid()).getId().equals(request.getPortalId()))
            return;

        requests.remove(request.getUuid());
        Player player = Bukkit.getPlayer(request.getPlayer());
        Portal portal = PortalData.CACHED_PORTALS.get(request.getPortalId());
        if (player != null && player.isOnline() && player.getName().equalsIgnoreCase(request.getPlayer()) && portal != null)
            plugin.getPortalManager().crossServerPortalSafe(player, portal, request.isSafe());
    }

    @MessageHandler(namespace = "survivalportals", subject = "teleportRequest")
    public void onTeleportRequest(PortalRequest request) {
        if (!request.getPortalId().equalsIgnoreCase("0") && !request.getPortalId().equalsIgnoreCase("-1")) {
            Portal portal = PortalData.CACHED_PORTALS.get(request.getPortalId());
            if (portal == null || portal.getPos1() == null)
                return;
        }

        tpRequests.put(request.getPlayer(), request);
        SurvivalCoreAPI.movePlayerTo(request.getPlayer(), SurvivalCoreBukkit.getInstance().getProtonManager().getClientName());
        plugin.getLogger().info(request.getPlayer() + " is being moved to here, portalid " + request.getPortalId());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (tpRequests.containsKey(event.getPlayer().getName())) {
            PortalRequest request = tpRequests.remove(event.getPlayer().getName());
            if (!request.getPortalId().equalsIgnoreCase("0") && !request.getPortalId().equalsIgnoreCase("-1")) {
                Portal portal = PortalData.CACHED_PORTALS.get(request.getPortalId());
                if (portal == null || portal.getPos1() == null)
                    return;
                plugin.getPortalManager().forceTeleportToPortal(event.getPlayer(), portal);
            } else {
                if (request.getPortalId().equalsIgnoreCase("0"))
                    plugin.getPortalManager().forceTeleportToSpawn(event.getPlayer(), false);
                else if (request.getPortalId().equalsIgnoreCase("-1"))
                    plugin.getPortalManager().forceTeleportToSpawn(event.getPlayer(), true);
            }
        }
    }
}
