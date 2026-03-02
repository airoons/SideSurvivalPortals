package lv.sidesurvival.events;

import lv.sidesurvival.objects.Portal;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PortalTeleportEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Portal destinationPortal;
    private final TeleportType teleportType;

    public PortalTeleportEvent(Player player, Portal destinationPortal, TeleportType teleportType) {
        this.player = player;
        this.destinationPortal = destinationPortal;
        this.teleportType = teleportType;
    }

    public Player getPlayer() {
        return player;
    }

    public Portal getDestinationPortal() {
        return destinationPortal;
    }

    public TeleportType getTeleportType() {
        return teleportType;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum TeleportType {
        NORMAL,
        OVERWORLD_SPAWN,
        NETHER_SPAWN
    }
}
