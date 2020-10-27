package lv.theironminerlv.sidesurvivalportals.commands;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.DataManager;
import lv.theironminerlv.sidesurvivalportals.managers.PermissionManager;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.ConvertUtils;
import lv.theironminerlv.sidesurvivalportals.utils.Messages;
import me.angeschossen.lands.api.integration.LandsIntegration;
import me.angeschossen.lands.api.land.Land;

public class PortalCommand implements CommandExecutor, TabExecutor
{
    private static SideSurvivalPortals plugin = SideSurvivalPortals.getInstance();
    private static PortalManager portalManager = plugin.getPortalManager();
    private static PermissionManager permissionManager = plugin.getPermissionManager();
    private static DataManager dataManager = plugin.getDataManager();
    private static LandsIntegration landsAPI = plugin.getLandsAPI();

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            final String argAdd = Messages.get("command-arguments.add");
            final String argRemove = Messages.get("command-arguments.remove");
            final String argLand = Messages.get("command-arguments.add-remove.land");
            final String argPlayer = Messages.get("command-arguments.add-remove.player");

            final Player player = (Player) sender;

            if (args.length >= 1) {
                final Portal portal = portalManager.getPortalAt(player.getLocation());
                final String argDesc = Messages.get("command-arguments.description");
                
                if (portal == null) {
                    player.sendMessage(Messages.get("chat.not-in-portal"));
                    return true;
                }
                if (!permissionManager.canEditPortal(player, portal.getLand())) {
                    player.sendMessage(Messages.get("chat.no-edit-permission"));
                    return true;
                }

                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase(argDesc))
                        player.sendMessage(Messages.get("chat.commands.description.no-description"));
                    else if (args[0].equalsIgnoreCase(argAdd))
                        player.sendMessage(Messages.get("chat.commands.add-remove.no-add-type"));
                    else if (args[0].equalsIgnoreCase(argRemove))
                        player.sendMessage(Messages.get("chat.commands.add-remove.no-remove-type"));

                    return true;
                }

                if (args[0].equalsIgnoreCase(argDesc)) {
                    final StringBuilder builder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        builder.append(args[i]);

                        if (i < args.length - 1)
                            builder.append(" ");
                    }
                    final String desc = builder.toString();
    
                    if (desc.length() > 90) {
                        player.sendMessage(Messages.get("chat.commands.too-long"));
                        return true;
                    }
    
                    portal.setDescription(desc);
                    dataManager.save(portal);
                    player.sendMessage(Messages.getParam("chat.commands.description.change-success", "{1}", desc));
    
                    return true;
                }

                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase(argLand))
                        player.sendMessage(Messages.get("chat.commands.add-remove.no-land"));
                    else if (args[1].equalsIgnoreCase(argPlayer))
                        player.sendMessage(Messages.get("chat.commands.add-remove.no-player"));

                    return true;
                } else {
                    if (args[0].equalsIgnoreCase(argAdd)) {
                        if (!args[1].equalsIgnoreCase(argLand) && !args[1].equalsIgnoreCase(argPlayer)) {
                            player.sendMessage(Messages.get("chat.commands.add-remove.no-add-type"));
                            return true;
                        }

                        final StringBuilder builder = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            builder.append(args[i]);
                        }
                        final String input = builder.toString();

                        if (args[1].equalsIgnoreCase(argLand)) {
                            final Land land = landsAPI.getLand(input);

                            if (land == null) {
                                player.sendMessage(Messages.get("chat.commands.add-remove.error-land-doesnt-exist"));
                                return true;
                            }

                            final List<Integer> allowedLands = portal.getAllowedLands();
                            
                            if (allowedLands.contains(land.getId())) {
                                player.sendMessage(Messages.get("chat.commands.add-remove.error-already-added-land"));
                                return true;
                            }

                            if (portal.getLand().getId() == land.getId()) {
                                player.sendMessage(Messages.get("chat.commands.add-remove.error-same-land"));
                                return true;
                            }
                            
                            allowedLands.add(land.getId());
                            portal.setAllowedlands(allowedLands);
                            dataManager.save(portal);

                            player.sendMessage(Messages.getParam("chat.commands.add-remove.add-success-land", "{1}", land.getName()));

                            return true;
                        } else {
                            final Player addPlayer = Bukkit.getPlayer(input);

                            if (addPlayer == null) {
                                player.sendMessage(Messages.get("chat.commands.add-remove.error-not-online"));
                                return true;
                            }

                            final List<UUID> allowedPlayers = portal.getAllowedPlayers();
                            
                            if (allowedPlayers.contains(addPlayer.getUniqueId())) {
                                player.sendMessage(Messages.get("chat.commands.add-remove.error-already-added-player"));
                                return true;
                            }

                            if (player.equals(addPlayer)) {
                                player.sendMessage(Messages.get("chat.commands.add-remove.error-same-player"));
                                return true;
                            }

                            if (portal.getLand().isTrusted(addPlayer.getUniqueId())) {
                                player.sendMessage(Messages.get("chat.commands.add-remove.error-already-added-playerland"));
                                return true;
                            }
                            
                            allowedPlayers.add(addPlayer.getUniqueId());
                            portal.setAllowedPlayers(allowedPlayers);
                            dataManager.save(portal);

                            player.sendMessage(Messages.getParam("chat.commands.add-remove.add-success-player", "{1}", addPlayer.getName()));

                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase(argRemove)) {
                        if (!args[1].equalsIgnoreCase(argLand) && !args[1].equalsIgnoreCase(argPlayer)) {
                            player.sendMessage(Messages.get("chat.commands.add-remove.no-remove-type"));
                            return true;
                        }

                        final StringBuilder builder = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            builder.append(args[i]);
                        }
                        final String input = builder.toString();

                        if (args[1].equalsIgnoreCase(argLand)) {
                            final Land land = landsAPI.getLand(input);

                            if (land == null) {
                                player.sendMessage(Messages.get("chat.commands.add-remove.error-land-doesnt-exist"));
                                return true;
                            }

                            
                            if (!portal.getAllowedLands().contains(land.getId())) {
                                player.sendMessage(Messages.get("chat.commands.add-remove.error-not-added-land"));
                                return true;
                            }
                            
                            portalManager.removeLandAccess(portal, land);

                            player.sendMessage(Messages.getParam("chat.commands.add-remove.remove-success-land", "{1}", land.getName()));

                            return true;
                        } else {
                            final OfflinePlayer remPlayer = Bukkit.getOfflinePlayer(input);

                            if (!remPlayer.hasPlayedBefore()) {
                                player.sendMessage(Messages.get("chat.commands.add-remove.error-hasnt-joined"));
                                return true;
                            }
                            
                            if (!portal.getAllowedPlayers().contains(remPlayer.getUniqueId())) {
                                player.sendMessage(Messages.get("chat.commands.add-remove.error-not-added-player"));
                                return true;
                            }
                            
                            portalManager.removePlayerAccess(portal, remPlayer.getUniqueId());

                            player.sendMessage(Messages.getParam("chat.commands.add-remove.remove-success-player", "{1}", remPlayer.getName()));
    
                            return true;
                        }
                    }
                }

                return true;
            }

            player.sendMessage(Messages.get("chat.commands.help"));
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        final List<String> completions = new ArrayList<String>();

        final String argDesc = Messages.get("command-arguments.description");
        final String argAdd = Messages.get("command-arguments.add");
        final String argRemove = Messages.get("command-arguments.remove");
        final String argLand = Messages.get("command-arguments.add-remove.land");
        final String argPlayer = Messages.get("command-arguments.add-remove.player");

        if (args.length == 1) {
            final List<String> commands = Arrays.asList(argDesc, argAdd, argRemove);
            return commands
            .stream()
            .filter(c -> c.startsWith(args[0]))
            .collect(Collectors.toList());
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase(argAdd) || args[0].equalsIgnoreCase(argRemove)) {
                completions.add(argLand);
                completions.add(argPlayer);
            }
        } else if (args.length == 3) {
            if ((args[0].equalsIgnoreCase(argAdd) || args[0].equalsIgnoreCase(argRemove)) && 
            (args[1].equalsIgnoreCase(argLand) || args[1].equalsIgnoreCase(argPlayer))) {
                return null;
            }
        }

        return completions;
    }
}
