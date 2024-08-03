package lv.sidesurvival.managers;

import lv.sidesurvival.managers.PlayerManager;
import lv.sidesurvival.objects.CPlayer;
import lv.sidesurvival.objects.Claim;
import lv.sidesurvival.objects.ClaimOwner;
import lv.sidesurvival.objects.perms.PermissableAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PermissionManager {

    public boolean canCreatePortal(Player player, ClaimOwner owner, Location loc) {
        if (player.hasPermission("sidesurvivalportals.admin"))
            return true;

        CPlayer cPlayer = PlayerManager.get().getByPlayer(player);
        Claim claim = new Claim(loc);
        return owner.hasPlayerPerms(cPlayer, claim, PermissableAction.CREATE_PORTALS);
    }

    public boolean canEditPortal(Player player, ClaimOwner owner, Location loc) {
        if (player.hasPermission("sidesurvivalportals.admin"))
            return true;

        CPlayer cPlayer = PlayerManager.get().getByPlayer(player);
        Claim claim = new Claim(loc);
        return owner.hasPlayerPerms(cPlayer, claim, PermissableAction.CREATE_PORTALS);
    }
}