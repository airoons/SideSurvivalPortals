package lv.theironminerlv.sidesurvivalportals.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.PermissionManager;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.ConvertUtils;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Land;

public class PortalCreateListener implements Listener
{
    private SideSurvivalPortals plugin;
    private static PortalManager portalManager;
    private static PermissionManager permissionManager;
    private static LandsIntegration landsAPI;

    public PortalCreateListener(SideSurvivalPortals plugin) {
        this.plugin = plugin;
        portalManager = this.plugin.getPortalManager();
        permissionManager = this.plugin.getPermissionManager();
        landsAPI = this.plugin.getLandsAPI();
    }

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event)
    {
        event.setCancelled(true);

        if (event.getEntity() == null)
            return;

        if (portalManager.getPortalAt(event.getBlocks().get(2).getLocation()) != null)
            return;

        boolean isNorthSouth = false;
        Block block;
        BlockVector3 min = null;
        BlockVector3 max = null;
        Land land = null;
        Player player = (Player)(event.getEntity());

        // Gets portal facing...
        for (BlockState portalBlock : event.getBlocks()) {
            block = portalBlock.getBlock();

            if (!landsAPI.isClaimed(block.getLocation())) {
                player.sendMessage(ConvertUtils.color("&cPortālu var uztaisīt tikai kādā teritorijā! (kāds no portāla blokiem atrodas ārpus teritorijas)"));
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

        // Gets min and max coords of the portal + land
        for (BlockState portalBlock : event.getBlocks()) {
            block = portalBlock.getBlock();

            if (land == null)
                land = landsAPI.getLand(block.getLocation());
            
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

        if (!permissionManager.canCreatePortal(player, land)) {
            player.sendMessage(ConvertUtils.color("&cTev nav atļauts izveidot šeit portālu!"));
            return;
        }

        Portal portal = new Portal(BukkitAdapter.adapt(event.getWorld(), min), BukkitAdapter.adapt(event.getWorld(), max), event.getWorld(), isNorthSouth, land);

        if (!portalManager.create(portal, isNorthSouth))
            return;
    }
}
