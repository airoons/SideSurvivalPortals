package lv.theironminerlv.sidesurvivalportals.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;

public class PortalCreateListener implements Listener
{
    private SideSurvivalPortals plugin;
    private static PortalManager portalManager;

    public PortalCreateListener(SideSurvivalPortals plugin) {
        this.plugin = plugin;
        portalManager = this.plugin.getPortalManager();
    }

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event)
    {
        event.setCancelled(true);

        boolean isNorthSouth = false;
        Block block;
        BlockVector3 min = null;
        BlockVector3 max = null;
        Location tpLoc = null;

        // Gets portal facing...
        for (BlockState portalBlock : event.getBlocks()) {
            block = portalBlock.getBlock();

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

        // Gets min and max coords of the portal
        for (BlockState portalBlock : event.getBlocks()) {
            block = portalBlock.getBlock();
            
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

        Portal portal = new Portal(BukkitAdapter.adapt(event.getWorld(), min), BukkitAdapter.adapt(event.getWorld(), max), event.getWorld(), tpLoc);

        if (!portalManager.create(portal, isNorthSouth))
            return;
    }
}
