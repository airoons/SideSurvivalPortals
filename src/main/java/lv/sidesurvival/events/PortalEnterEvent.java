package lv.sidesurvival.events;

import lv.sidesurvival.objects.Portal;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PortalEnterEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Portal portal;

    public PortalEnterEvent(Player player, Portal portal) {
        this.player = player;
        this.portal = portal;
    }

    public Player getPlayer() {
        return player;
    }

    public Portal getPortal() {
        return portal;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
