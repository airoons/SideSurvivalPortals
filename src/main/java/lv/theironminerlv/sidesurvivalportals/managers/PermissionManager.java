package lv.theironminerlv.sidesurvivalportals.managers;

import org.bukkit.entity.Player;

import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Land;
import me.angeschossen.lands.api.player.LandPlayer;
import me.angeschossen.lands.api.role.enums.ManagementSetting;

public class PermissionManager
{
    private SideSurvivalPortals plugin;
    private static LandsIntegration landsAPI;

    public PermissionManager(SideSurvivalPortals plugin) {
        this.plugin = plugin;
        landsAPI = this.plugin.getLandsAPI();
    }

    public boolean canCreatePortal(Player player, Land land) {
        if (player.hasPermission("sidesurvivalportals.admin"))
            return true;

        if (!land.isTrusted(player.getUniqueId()))
            return false;
        
        if (!land.canManagement(player.getUniqueId(), ManagementSetting.AREA_ASSIGN))
            return false;

        return true;
    }

    public boolean canEditPortal(Player player, Land land) {
        if (player.hasPermission("sidesurvivalportals.admin"))
            return true;

        if (!land.isTrusted(player.getUniqueId()))
            return false;

        if (!land.canManagement(player.getUniqueId(), ManagementSetting.WAR_MANAGE))
            return false;

        return true;
    }
}