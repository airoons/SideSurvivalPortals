package lv.theironminerlv.sidesurvivalportals.commands;

import lv.side.lang.api.LangAPI;
import lv.sidesurvival.api.SurvivalCoreAPI;
import lv.sidesurvival.managers.ClaimManager;
import lv.sidesurvival.managers.GroupManager;
import lv.sidesurvival.objects.ClaimOwner;
import lv.sidesurvival.objects.Group;
import lv.sidesurvival.utils.Config;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import lv.theironminerlv.sidesurvivalportals.SurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.managers.DataManager;
import lv.theironminerlv.sidesurvivalportals.managers.PermissionManager;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.Messages;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public class PortalCommand implements CommandExecutor, TabExecutor {

    private static final SurvivalPortals plugin = SurvivalPortals.getInstance();
    private static final PortalManager portalManager = plugin.getPortalManager();
    private static final PermissionManager permissionManager = plugin.getPermissionManager();
    private static final DataManager dataManager = plugin.getDataManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            String argAdd = Messages.get(player, "command-arguments.add");
            String argRemove = Messages.get(player, "command-arguments.remove");
            String argGroup = Messages.get(player, "command-arguments.add-remove.group");
            String argPlayer = Messages.get(player, "command-arguments.add-remove.player");

            if (args.length >= 1) {
                if (player.hasPermission("portali.admin")) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        plugin.reloadConfig();
                        Config.reloadFromConfig(plugin.getConfig());
                        sender.sendMessage("[SurvivalPortals] Plugin successfully reloaded!");
                        return true;
                    }

                    if (args[0].equalsIgnoreCase("updatelang")) {
                        Map<String, String> keyMap = new HashMap<>();

                        if (plugin.getConfig().isConfigurationSection("default-messages")) {
                            ConfigurationSection messages = plugin.getConfig().getConfigurationSection("default-messages");

                            //noinspection ConstantConditions
                            Collection<String> keys = messages.getKeys(true);
                            for (String key : keys) {
                                if (messages.isString(key)) {
                                    String value = messages.getString(key);
                                    keyMap.put("s-portals." + key, value);
                                }
                            }
                        }

                        LangAPI.updateDefaults(keyMap);
                        sender.sendMessage("[SurvivalPortals] Default lang map reloaded!");
                        return true;
                    }
                }

                Portal portal = portalManager.getPortalAt(player.getLocation());
                String argDesc = Messages.get(player, "command-arguments.description");
                
                if (portal == null) {
                    player.sendMessage(Messages.get(player, "chat.not-in-portal"));
                    return true;
                }
                ClaimOwner owner = ClaimManager.get().getOwnerById(portal.getOwner());
                if (!permissionManager.canEditPortal(player, owner, player.getLocation())) {
                    player.sendMessage(Messages.get(player, "chat.no-edit-permission"));
                    return true;
                }

                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase(argDesc))
                        player.sendMessage(Messages.get(player, "chat.commands.description.no-description"));
                    else if (args[0].equalsIgnoreCase(argAdd))
                        player.sendMessage(Messages.get(player, "chat.commands.add-remove.no-add-type"));
                    else if (args[0].equalsIgnoreCase(argRemove))
                        player.sendMessage(Messages.get(player, "chat.commands.add-remove.no-remove-type"));

                    return true;
                }

                if (args[0].equalsIgnoreCase(argDesc)) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        builder.append(args[i]);

                        if (i < args.length - 1)
                            builder.append(" ");
                    }
                    String desc = builder.toString();
    
                    if (desc.length() > 90) {
                        player.sendMessage(Messages.get(player, "chat.commands.too-long"));
                        return true;
                    }
    
                    portal.setDescription(desc);
                    dataManager.save(portal);
                    player.sendMessage(Messages.getParam(player, "chat.commands.description.change-success", "{1}", desc));
    
                    return true;
                }

                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase(argGroup))
                        player.sendMessage(Messages.get(player, "chat.commands.add-remove.no-group"));
                    else if (args[1].equalsIgnoreCase(argPlayer))
                        player.sendMessage(Messages.get(player, "chat.commands.add-remove.no-player"));

                    return true;
                } else {
                    if (args[0].equalsIgnoreCase(argAdd)) {
                        if (!args[1].equalsIgnoreCase(argGroup) && !args[1].equalsIgnoreCase(argPlayer)) {
                            player.sendMessage(Messages.get(player, "chat.commands.add-remove.no-add-type"));
                            return true;
                        }

                        StringBuilder builder = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            builder.append(args[i]);
                        }
                        String input = builder.toString();

                        if (args[1].equalsIgnoreCase(argGroup)) {
                            Group group = GroupManager.get().getByName(input);

                            if (group == null) {
                                player.sendMessage(Messages.get(player, "chat.commands.add-remove.error-group-doesnt-exist"));
                                return true;
                            }

                            List<String> allowedGroups = portal.getAllowedGroups();
                            
                            if (allowedGroups.contains(group.getId())) {
                                player.sendMessage(Messages.get(player, "chat.commands.add-remove.error-already-added-group"));
                                return true;
                            }

                            if (portal.getOwner().equalsIgnoreCase(owner.getId())) {
                                player.sendMessage(Messages.get(player, "chat.commands.add-remove.error-same-group"));
                                return true;
                            }
                            
                            allowedGroups.add(group.getId());
                            portal.setAllowedGroups(allowedGroups);
                            dataManager.saveAccess(portal);

                            player.sendMessage(Messages.getParam(player, "chat.commands.add-remove.add-success-group", "{1}", group.getName(null)));

                            return true;
                        } else {
                            Player addPlayer = Bukkit.getPlayer(input);
                            String addUUID = addPlayer != null ? addPlayer.getUniqueId().toString() : null;

                            if (addPlayer == null || !addPlayer.isOnline() || !addPlayer.getName().equalsIgnoreCase(input)) {
                                if (SurvivalCoreAPI.getVisiblePlayers().contains(input))
                                    addUUID = SurvivalCoreAPI.getUUIDStringFromNick(input);
                                else {
                                    player.sendMessage(Messages.get(player, "chat.commands.add-remove.error-not-online"));
                                    return true;
                                }
                            }

                            List<String> allowedPlayers = portal.getAllowedPlayers();
                            
                            if (allowedPlayers.contains(addUUID)) {
                                player.sendMessage(Messages.get(player, "chat.commands.add-remove.error-already-added-player"));
                                return true;
                            }

                            if (player.equals(addPlayer)) {
                                player.sendMessage(Messages.get(player, "chat.commands.add-remove.error-same-player"));
                                return true;
                            }
                            
                            allowedPlayers.add(addUUID);
                            portal.setAllowedPlayers(allowedPlayers);
                            dataManager.saveAccess(portal);

                            player.sendMessage(Messages.getParam(player, "chat.commands.add-remove.add-success-player", "{1}", input));

                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase(argRemove)) {
                        if (!args[1].equalsIgnoreCase(argGroup) && !args[1].equalsIgnoreCase(argPlayer)) {
                            player.sendMessage(Messages.get(player, "chat.commands.add-remove.no-remove-type"));
                            return true;
                        }

                        StringBuilder builder = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            builder.append(args[i]);
                        }
                        String input = builder.toString();

                        if (args[1].equalsIgnoreCase(argGroup)) {
                            Group group = GroupManager.get().getByName(input);

                            if (group == null) {
                                player.sendMessage(Messages.get(player, "chat.commands.add-remove.error-group-doesnt-exist"));
                                return true;
                            }

                            if (!portal.getAllowedGroups().contains(group.getId())) {
                                player.sendMessage(Messages.get(player, "chat.commands.add-remove.error-not-added-group"));
                                return true;
                            }
                            
                            portalManager.removeGroupAccess(portal, group.getId());

                            player.sendMessage(Messages.getParam(player, "chat.commands.add-remove.remove-success-group", "{1}", group.getName(null)));

                            return true;
                        } else {
                            String remUUID = SurvivalCoreAPI.getUUIDStringFromNick(input);

                            if (remUUID == null) {
                                player.sendMessage(Messages.get(player, "chat.commands.add-remove.error-hasnt-joined"));
                                return true;
                            }
                            
                            if (!portal.getAllowedPlayers().contains(remUUID)) {
                                player.sendMessage(Messages.get(player, "chat.commands.add-remove.error-not-added-player"));
                                return true;
                            }
                            
                            portalManager.removePlayerAccess(portal, remUUID);

                            player.sendMessage(Messages.getParam(player, "chat.commands.add-remove.remove-success-player", "{1}", input));
    
                            return true;
                        }
                    }
                }

                return true;
            }

            player.sendMessage(Messages.get(player, "chat.commands.help"));
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (!(sender instanceof Player player))
            return completions;

        String argDesc = Messages.get(player, "command-arguments.description");
        String argAdd = Messages.get(player, "command-arguments.add");
        String argRemove = Messages.get(player, "command-arguments.remove");
        String argGroup = Messages.get(player, "command-arguments.add-remove.group");
        String argPlayer = Messages.get(player, "command-arguments.add-remove.player");

        if (args.length == 1) {
            List<String> commands = Arrays.asList(argDesc, argAdd, argRemove);
            return commands
            .stream()
            .filter(c -> c.startsWith(args[0]))
            .collect(Collectors.toList());
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase(argAdd) || args[0].equalsIgnoreCase(argRemove)) {
                completions.add(argGroup);
                completions.add(argPlayer);
            }
        } else if (args.length == 3) {
            if ((args[0].equalsIgnoreCase(argAdd) || args[0].equalsIgnoreCase(argRemove)) && 
            (args[1].equalsIgnoreCase(argGroup) || args[1].equalsIgnoreCase(argPlayer))) {
                return null;
            }
        }

        return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
    }
}
