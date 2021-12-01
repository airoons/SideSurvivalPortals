package lv.theironminerlv.sidesurvivalportals.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;

import lv.sidesurvival.managers.ClaimManager;
import lv.sidesurvival.objects.Claim;
import lv.sidesurvival.objects.ClaimOwner;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

import lv.theironminerlv.sidesurvivalportals.SurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.PermissionManager;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.Messages;

public class PortalCreateListener implements Listener {

    private SurvivalPortals plugin;
    private static PortalManager portalManager;
    private static PermissionManager permissionManager;

    public PortalCreateListener(SurvivalPortals plugin) {
        this.plugin = plugin;
        portalManager = this.plugin.getPortalManager();
        permissionManager = this.plugin.getPermissionManager();
    }

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {
        event.setCancelled(true);

        if (event.getEntity() == null)
            return;

        if (portalManager.getPortalAt(event.getBlocks().get(2).getLocation()) != null)
            return;

        boolean isNorthSouth = false;
        Block block = null;
        BlockVector3 min = null;
        BlockVector3 max = null;
        ClaimOwner owner = null;
        Player player = (Player)(event.getEntity());

        // Gets portal facing...
        for (BlockState portalBlock : event.getBlocks()) {
            block = portalBlock.getBlock();

            if (ClaimManager.get().getOwnerAt(new Claim(block)) == null) {
                player.sendMessage(Messages.get("chat.error-creating-outside-group"));
                return;
            }

            if (block.getType() != Material.OBSIDIAN) {
                for (BlockState portalBlock2 : event.getBlocks()) {
                    if (block.getRelative(BlockFace.NORTH).getLocation().equals(portalBlock2.getLocation())) {
                        isNorthSouth = true;
                        break;
                    }
                    else if (block.getRelative(BlockFace.SOUTH).getLocation().equals(portalBlock2.getLocation())) {
                        isNorthSouth = true;
                        break;
                    }
                }
            }
        }

        // Gets min and max coords of the portal + owner
        for (BlockState portalBlock : event.getBlocks()) {
            block = portalBlock.getBlock();

            if (owner == null)
                owner = ClaimManager.get().getOwnerAt(new Claim(block));
            
            if (block.getType() != Material.OBSIDIAN) {
                if (isNorthSouth) {
                    if ((block.getRelative(BlockFace.DOWN).getType() == Material.OBSIDIAN) && (block.getRelative(BlockFace.NORTH).getType() == Material.OBSIDIAN)) {
                        min = BlockVector3.at(block.getX(), block.getY(), block.getZ());
                    } else if ((block.getRelative(BlockFace.UP).getType() == Material.OBSIDIAN) && (block.getRelative(BlockFace.SOUTH).getType() == Material.OBSIDIAN)) {
                        max = BlockVector3.at(block.getX(), block.getY(), block.getZ());
                    }
                } else {
                    if ((block.getRelative(BlockFace.DOWN).getType() == Material.OBSIDIAN) && (block.getRelative(BlockFace.EAST).getType() == Material.OBSIDIAN)) {
                        min = BlockVector3.at(block.getX(), block.getY(), block.getZ());
                    } else if ((block.getRelative(BlockFace.UP).getType() == Material.OBSIDIAN) && (block.getRelative(BlockFace.WEST).getType() == Material.OBSIDIAN)) {
                        max = BlockVector3.at(block.getX(), block.getY(), block.getZ());
                    }
                }
            }
        }

        if (block != null && !permissionManager.canCreatePortal(player, owner, block.getLocation())) {
            player.sendMessage(Messages.get("chat.no-create-permission"));
            return;
        }

        if (owner != null) {
            Portal portal = new Portal(BukkitAdapter.adapt(event.getWorld(), min), BukkitAdapter.adapt(event.getWorld(), max), event.getWorld(), isNorthSouth, owner.getId());
            portalManager.create(portal, isNorthSouth);
        }
    }
}
