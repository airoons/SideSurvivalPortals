package lv.sidesurvival.managers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import lv.sidesurvival.SurvivalCoreBukkit;
import lv.sidesurvival.SurvivalPortals;
import lv.sidesurvival.listeners.CrossServerHandler;
import lv.sidesurvival.managers.ClaimManager;
import lv.sidesurvival.objects.Claim;
import lv.sidesurvival.objects.ClaimOwner;
import lv.sidesurvival.utils.Messages;
import lv.sidesurvival.data.PortalData;
import lv.sidesurvival.objects.SafeZone;
import lv.sidesurvival.objects.Portal;
import lv.sidesurvival.objects.PortalRequest;
import lv.sidesurvival.utils.BlockUtils;
import lv.sidesurvival.utils.ConvertUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.type.GlassPane;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class PortalManager {

    private final SurvivalPortals plugin;
    private static DataManager dataManager;

    public Map<UUID, BukkitTask> tasks = new HashMap<>();
    public List<Material> safeBlocks = new ArrayList<>();
    private static final Map<UUID, Portal> requests = new HashMap<>();
    public final List<Material> allowedBlocks = List.of(
            Material.GLASS_PANE,
            Material.WHITE_STAINED_GLASS_PANE,
            Material.LIGHT_GRAY_STAINED_GLASS_PANE,
            Material.GRAY_STAINED_GLASS_PANE,
            Material.BLACK_STAINED_GLASS_PANE,
            Material.BROWN_STAINED_GLASS_PANE,
            Material.RED_STAINED_GLASS_PANE,
            Material.ORANGE_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE,
            Material.LIME_STAINED_GLASS_PANE,
            Material.GREEN_STAINED_GLASS_PANE,
            Material.CYAN_STAINED_GLASS_PANE,
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            Material.BLUE_STAINED_GLASS_PANE,
            Material.PURPLE_STAINED_GLASS_PANE,
            Material.MAGENTA_STAINED_GLASS_PANE,
            Material.PINK_STAINED_GLASS_PANE
    );

    public PortalManager(SurvivalPortals plugin) {
        this.plugin = plugin;
        dataManager = this.plugin.getDataManager();

        if (plugin.getConfig().isList("safeBlocks")) {
            for (String material : plugin.getConfig().getStringList("safeBlocks")) {
                if (Material.valueOf(material) != null)
                    safeBlocks.add(Material.valueOf(material));
            }
        }
    }

    // Fully creates portal (region + blocks), but saving has to be done after
    // Will return true if everything is fine with creation
    public boolean create(Portal portal, boolean isNorthSouth) {
        if (portal == null)
            return false;

        Location pos1 = portal.getPos1();
        Location pos2 = portal.getPos2();
        World world = portal.getWorld();

        if ((pos1 == null) || (pos2 == null) || (world == null))
            return false;

        String id = generatePortalName(pos1, world);
        portal.setId(id);

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(world));
        if (regionManager == null)
            return false;

        ProtectedRegion region = new ProtectedCuboidRegion(id, ConvertUtils.toBlockVector3(pos1),
                ConvertUtils.toBlockVector3(pos2));
        region.setFlag(Flags.BUILD, StateFlag.State.DENY);
        region.setFlag(Flags.DENY_MESSAGE, "");
        regionManager.addRegion(region);

        setPortalGlass(pos1, pos2, isNorthSouth);

        Location safeLoc = getSafeTeleportLoc(portal);
        if (safeLoc != null)
            portal.setTpLoc(safeLoc);
        else
            portal.setTpLoc(pos1);

        PortalData.addPortal(portal, true);

        return true;
    }

    // Fully removes portal from world and database
    public void remove(Portal portal) {
        if (portal == null)
            return;

        ArrayList<Location> portalBlocks = BlockUtils.getBlocksBetween(portal.getPos1(), portal.getPos2());

        new BukkitRunnable() {
            public void run() {
                for (Location blockLoc : portalBlocks) {
                    blockLoc.getBlock().breakNaturally();
                }
            }
        }.runTask(plugin);

        PortalData.removePortal(portal, true);
        removeRegion(portal.getId(), portal.getWorld());
    }

    // Used to place purple stained glass pane blocks between positions,
    // IsNorthSouth controls how panes are going to connect to adjacent blocks
    public void setPortalGlass(Location pos1, Location pos2, boolean isNorthSouth, Material color) {
        ArrayList<Location> portalBlocks = BlockUtils.getBlocksBetween(pos1, pos2);
        Block loopBlock;

        for (Location blockLoc : portalBlocks) {
            loopBlock = blockLoc.getBlock();
            if (loopBlock.getType() != Material.OBSIDIAN) {
                loopBlock.setType(color);
                BlockData data = loopBlock.getBlockData();
                if (isNorthSouth) {
                    ((GlassPane) data).setFace(BlockFace.NORTH, true);
                    ((GlassPane) data).setFace(BlockFace.SOUTH, true);
                } else {
                    ((GlassPane) data).setFace(BlockFace.EAST, true);
                    ((GlassPane) data).setFace(BlockFace.WEST, true);
                }

                loopBlock.setBlockData(data);
                loopBlock.getState().update(true);
            }
        }
    }

    private void setPortalGlass(Location pos1, Location pos2, boolean isNorthSouth) {
        setPortalGlass(pos1, pos2, isNorthSouth, Material.PURPLE_STAINED_GLASS_PANE);
    }

    private String generatePortalName(Location loc, World world) {
        return "portal_" + world.getName() + "_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
    }

    public boolean isPortalAt(Location loc) {
        ProtectedRegion region = getPortalRegionAt(loc);

        if (region == null)
            return false;

        if (!region.getId().contains("portal_"))
            return false;

        if (!PortalData.portalExists(region.getId()))
            return false;

        return true;
    }

    public Portal getPortalAt(Location loc) {
        ProtectedRegion region = getPortalRegionAt(loc);
        if ((region != null) && (region.getId().contains("portal_"))) {

            return PortalData.CACHED_PORTALS.get(region.getId());
        }

        return null;
    }

    public ProtectedRegion getPortalRegionAt(Location loc) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(loc.getWorld()));
        if (regionManager == null)
            return null;

        ApplicableRegionSet applicable = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(loc));
        if (applicable.size() < 1)
            return null;

        for (ProtectedRegion region : applicable.getRegions()) {
            if (region.getId().startsWith("portal"))
                return region;
        }

        return null;
    }

    public void removeRegion(String id, World world) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(world));
        if (regionManager == null)
            return;

        regionManager.removeRegion(id);
    }

    // Removes any portals that are outside of ClaimOwners's claims
    public void recheckPortals(ClaimOwner owner) {
        Map<String, Portal> portals = PortalData.getByOwner(owner.getId());

        for (Portal portal : portals.values()) {
            if (portal.getPos1() == null)
                continue;

            if (!Objects.equals(ClaimManager.get().getOwnerAt(new Claim(portal.getPos1())), owner))
                remove(portal);
        }
    }

    public Location getSafeTeleportLoc(Portal portal) {
        Location pos1 = portal.getPos1().clone();
        Location pos2 = portal.getPos2().clone();
        Block checkBlock;
        ArrayList<Location> portalBlocks;

        pos2.setY(pos1.getY());
        if (portal.getNorthSouth()) {
            portalBlocks = BlockUtils.getBlocksBetween(pos1.add(1.0, 0.0, 0.0), pos2.add(-1.0, 0.0, 0.0));
        } else {
            portalBlocks = BlockUtils.getBlocksBetween(pos1.add(0.0, 0.0, 1.0), pos2.add(0.0, 0.0, -1.0));
        }

        for (Location loc : portalBlocks) {
            if (loc.getBlock().isEmpty() && loc.getBlock().getRelative(BlockFace.UP).isEmpty()) {
                checkBlock = loc.getBlock().getRelative(BlockFace.DOWN);
                if (isSolidBlock(checkBlock)) {
                    loc.add(0.5, 0, 0.5);
                    return loc;
                } else if (checkBlock.isEmpty()) {
                    checkBlock = checkBlock.getRelative(BlockFace.DOWN);
                    if (isSolidBlock(checkBlock)) {
                        loc.add(0.5, 0, 0.5);
                        return loc;
                    }
                }
            }
        }

        return null;
    }

    public boolean isSolidBlock(Block block) {
        if (!block.isEmpty() && !block.isLiquid() && !block.isPassable())
            return true;

        return (safeBlocks.contains(block.getType()));
    }

    public void removeGroupAccess(Portal portal, String groupId) {
        if (!PortalData.portalExists(portal))
            return;

        List<String> allowedGroups = portal.getAllowedGroups();

        allowedGroups.remove(groupId);
        portal.setAllowedGroups(allowedGroups);
        dataManager.saveAccess(portal);
    }

    public void removePlayerAccess(Portal portal, String uuid) {
        if (!PortalData.portalExists(portal))
            return;

        List<String> allowedPlayers = portal.getAllowedPlayers();

        allowedPlayers.remove(uuid);
        portal.setAllowedPlayers(allowedPlayers);
        dataManager.saveAccess(portal);
    }

    public void fakePortalBlocks(Player player, Portal portal, boolean enable) {
        if (!PortalData.portalExists(portal))
            return;

        if (ClaimManager.get().getOwnerAt(new Claim(player.getLocation())) instanceof SafeZone)
            return;

        ArrayList<Location> portalBlocks = BlockUtils.getBlocksBetween(portal.getPos1(), portal.getPos2());
        Orientable fakePortal = (Orientable) Material.NETHER_PORTAL.createBlockData();

        if (portal.getNorthSouth())
            fakePortal.setAxis(Axis.Z);

        if (enable) {
            for (Location loc : portalBlocks) {
                player.sendBlockChange(loc, fakePortal);
            }
        } else {
            for (Location loc : portalBlocks) {
                player.sendBlockChange(loc, loc.getBlock().getBlockData());
            }
        }
    }

    public void teleportTo(Player player, Portal portal) {
        plugin.handleClose.remove(player);
        player.closeInventory();

        if (!PortalData.portalExists(portal))
            return;

        if (portal.getPos1() != null) { // Portal is in this server
            if (getFinalSafeLoc(player, portal) == null)
                return;

            teleportAccept(player, portal, true);

        } else { // If in a different server
            PortalRequest reply = new PortalRequest(player.getName(), player.getUniqueId().toString(), portal.getId());
            CrossServerHandler.requests.put(player.getUniqueId().toString(), portal);
            SurvivalCoreBukkit.getInstance().getProtonManager().broadcast("survivalportals", "locationSafeRequest", reply);
        }
    }

    public void teleportAccept(Player player, Portal portal, boolean cooldown) {
        if (cooldown) {
            if (player.hasPermission("sidesurvivalportals.tp.bypass")) {
                if (portal.getPos1() != null) {
                    forceTeleportToPortal(player, portal);
                } else {
                    PortalRequest reply = new PortalRequest(player.getName(), player.getUniqueId().toString(), portal.getId());
                    CrossServerHandler.requests.put(player.getUniqueId().toString(), portal);
                    requests.put(player.getUniqueId(), portal);
                    SurvivalCoreBukkit.getInstance().getProtonManager().broadcast("survivalportals", "locationSafeRequest", reply);
                }
                return;
            }

            if (!tasks.containsKey(player.getUniqueId())) {
                player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, SoundCategory.BLOCKS, 0.5f, 1.0f);
                if (isPortalAt(player.getLocation()))
                    fakePortalBlocks(player, getPortalAt(player.getLocation()), true);

                final String tpTitle = Messages.get(player, "chat.teleport-title");

                tasks.put(player.getUniqueId(), new BukkitRunnable() {
                    int n = 0;
                    String dots;

                    @Override
                    public void run() {
                        if (n >= 5) {
                            if (isPortalAt(player.getLocation()))
                                fakePortalBlocks(player, getPortalAt(player.getLocation()), false);
                            tasks.remove(player.getUniqueId());
                            this.cancel();
                            if (portal.getPos1() != null) {
                                if (getFinalSafeLoc(player, portal) == null)
                                    return;
                                teleportAccept(player, portal, false);
                            } else {
                                PortalRequest reply = new PortalRequest(player.getName(), player.getUniqueId().toString(), portal.getId());
                                CrossServerHandler.requests.put(player.getUniqueId().toString(), portal);
                                requests.put(player.getUniqueId(), portal);
                                SurvivalCoreBukkit.getInstance().getProtonManager().broadcast("survivalportals", "locationSafeRequest", reply);
                            }
                            return;
                        }

                        dots = "";
                        for (int i = 0; i < n; i++) {
                            if (i > 0)
                                dots += " ●";
                            else
                                dots += "&d&l●";
                        }
                        dots += "&5&l";

                        for (int i = n; i < 5; i++) {
                            if (i > 0)
                                dots += " ●";
                            else
                                dots += "●";
                        }
                        player.sendTitle(tpTitle, ConvertUtils.color(dots), 0, 20, 5);

                        n++;
                    }
                }.runTaskTimer(plugin, 0, 15));
            }
        } else {
            forceTeleportToPortal(player, portal);
        }
    }

    public void crossServerPortalSafe(Player player, Portal portal, boolean safe) {
        if (!safe) {
            player.sendMessage(Messages.get(player, "chat.teleport-not-safe"));
            return;
        }

        if (!requests.containsKey(player.getUniqueId())) {
            teleportAccept(player, portal, true);
        } else {
            PortalRequest request = new PortalRequest(player.getName(), player.getUniqueId().toString(), portal.getId());
            SurvivalCoreBukkit.getInstance().getProtonManager().broadcast("survivalportals", "teleportRequest", request);
        }
    }

    public void forceTeleportToPortal(Player player, Portal portal) {
        if (portal.getPos1() != null) {
            Location loc = portal.getTpLoc().clone();
            loc.setPitch(player.getLocation().getPitch());
            loc.setYaw(player.getLocation().getYaw());
            player.teleport(loc);
        }
    }

    public Location getFinalSafeLoc(Player player, Portal portal) {
        if (portal.getPos1() == null)
            return null;

        Location loc = portal.getTpLoc().clone();

        if (!loc.getBlock().isEmpty() || !loc.getBlock().getRelative(BlockFace.UP).isEmpty()) {
            loc = null;
        }

        Block checkBlock = portal.getTpLoc().getBlock().getRelative(BlockFace.DOWN);
        if (checkBlock.isEmpty() || checkBlock.isLiquid() || checkBlock.isPassable()) {
            if (checkBlock.isEmpty()) {
                checkBlock = checkBlock.getRelative(BlockFace.DOWN);
                if (checkBlock.isEmpty() || checkBlock.isLiquid() || checkBlock.isPassable())
                    loc = null;
            } else
                loc = null;
        }

        if (loc == null) {
            loc = getSafeTeleportLoc(portal);

            if (loc == null) {
                if (player != null && player.isOnline())
                    player.sendMessage(Messages.get(player, "chat.teleport-not-safe"));
                return null;
            }

            portal.setTpLoc(loc);
            dataManager.save(portal);
        }

        return loc;
    }

    public void teleportToSpawn(Player player, boolean isNether) {
        player.closeInventory();

        if (player.hasPermission("sidesurvivalportals.tp.bypass")) {
            forceTeleportToSpawn(player, isNether);
            return;
        }

        if (!tasks.containsKey(player.getUniqueId())) {
            player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, SoundCategory.BLOCKS, 0.5f, 1.0f);
            if (isPortalAt(player.getLocation()))
                fakePortalBlocks(player, getPortalAt(player.getLocation()), true);

            final String tpTitle = Messages.get(player, "chat.teleport-title");

            tasks.put(player.getUniqueId(), new BukkitRunnable() {
                int n = 0;
                String dots;

                @Override
                public void run() {
                    if (n >= 5) {
                        if (isPortalAt(player.getLocation()))
                            fakePortalBlocks(player, getPortalAt(player.getLocation()), false);

                        forceTeleportToSpawn(player, isNether);
                        tasks.remove(player.getUniqueId());
                        this.cancel();
                        return;
                    }

                    dots = "";
                    for (int i = 0; i < n; i++) {
                        if (i > 0)
                            dots += " ●";
                        else
                            dots += "&d&l●";
                    }
                    dots += "&5&l";

                    for (int i = n; i < 5; i++) {
                        if (i > 0)
                            dots += " ●";
                        else
                            dots += "●";
                    }
                    player.sendTitle(tpTitle, ConvertUtils.color(dots), 0, 20, 5);

                    n++;
                }
            }.runTaskTimer(plugin, 0, 15));
        }
    }

    public void forceTeleportToSpawn(Player player, boolean isNether) {
        if (!isNether) {
            if (PortalData.getSpawnLocation() != null) {
                player.teleport(PortalData.getSpawnLocation());
            } else {
                PortalRequest request = new PortalRequest(player.getName(), player.getUniqueId().toString(), "0");
                SurvivalCoreBukkit.getInstance().getProtonManager().send("survivalportals", "teleportRequest", request, PortalData.getWorldSpawnServer());
            }
        } else {
            if (PortalData.getNetherSpawnLocation() != null) {
                player.teleport(PortalData.getNetherSpawnLocation());
            } else {
                PortalRequest request = new PortalRequest(player.getName(), player.getUniqueId().toString(), "-1");
                SurvivalCoreBukkit.getInstance().getProtonManager().send("survivalportals", "teleportRequest", request, PortalData.getNetherSpawnServer());
            }
        }
    }
}
