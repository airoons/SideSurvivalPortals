package lv.theironminerlv.sidesurvivalportals.commands;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.data.PortalData;
import lv.theironminerlv.sidesurvivalportals.managers.DataManager;
import lv.theironminerlv.sidesurvivalportals.managers.PermissionManager;
import lv.theironminerlv.sidesurvivalportals.managers.PortalManager;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;
import lv.theironminerlv.sidesurvivalportals.utils.ConvertUtils;
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length >= 1) {
                Portal portal = portalManager.getPortalAt(player.getLocation());
                
                if (portal == null) {
                    player.sendMessage(ConvertUtils.color("&cTev ir jāatrodas portālā, lai izmantotu šo komandu!"));
                    return true;
                }
                if (!permissionManager.canEditPortal(player, portal.getLand())) {
                    player.sendMessage(ConvertUtils.color("&cTev nav atļauts labot šo portālu!"));
                    return true;
                }

                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("apraksts"))
                        player.sendMessage(ConvertUtils.color("&cNeesi norādījis aprakstu!"));
                    else if (args[0].equalsIgnoreCase("pievienot"))
                        player.sendMessage(ConvertUtils.color("&cNoradi, vai pievienosi teritoriju vai komandu!"));
                    else if (args[0].equalsIgnoreCase("nonemt"))
                        player.sendMessage(ConvertUtils.color("&cNoradi, vai nonemsi teritoriju vai komandu!"));

                    return true;
                }

                if (args[0].equalsIgnoreCase("apraksts")) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        builder.append(args[i]);

                        if (i < args.length - 1)
                            builder.append(" ");
                    }
                    String desc = builder.toString();
    
                    if (desc.length() > 90) {
                        player.sendMessage(ConvertUtils.color("&cApraksts nedrīkst būt garāks par 90 rakstzīmēm!"));
                        return true;
                    }
    
                    portal.setDescription(desc);
                    dataManager.save(portal);
                    player.sendMessage(ConvertUtils.color("&7Portāla apraksts veiksmīgi mainīts uz &f" + desc + "&7!"));
    
                    return true;
                }

                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("t"))
                        player.sendMessage(ConvertUtils.color("&cNoradi, kuru teritoriju pievienosi!"));
                    else if (args[1].equalsIgnoreCase("s"))
                        player.sendMessage(ConvertUtils.color("&cNoradi, kuru speletaju pievienosi!"));

                    return true;
                } else {
                    if (args[0].equalsIgnoreCase("pievienot")) {
                        if (!args[1].equalsIgnoreCase("t") && !args[1].equalsIgnoreCase("s")) {
                            player.sendMessage(ConvertUtils.color("&cNoradi, vai pievienosi teritoriju (t) vai speletaju (s)! " + args[1]));
                            return true;
                        }

                        StringBuilder builder = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            builder.append(args[i]);
                        }
                        String input = builder.toString();

                        if (args[1].equalsIgnoreCase("t")) {
                            Land land = landsAPI.getLand(input);

                            if (land == null) {
                                player.sendMessage(ConvertUtils.color("&cKļūda: tāda teritorija neeksistē!"));
                                return true;
                            }

                            List<Integer> allowedLands = portal.getAllowedLands();
                            
                            if (allowedLands.contains(land.getId())) {
                                player.sendMessage(ConvertUtils.color("&cKļūda: tādai teritorijai jau ir atļauts izmantot šo portālu!"));
                                return true;
                            }

                            if (portal.getLand().getId() == land.getId()) {
                                player.sendMessage(ConvertUtils.color("&cKļūda: nevar portālam teritoriju, kurā tas atrodas!"));
                                return true;
                            }
                            
                            allowedLands.add(land.getId());
                            portal.setAllowedlands(allowedLands);
                            dataManager.save(portal);

                            player.sendMessage(ConvertUtils.color("&7Veiksmīgi atļāvi teritorijai &f" + land.getName() + " &7izmantot šo portālu!"));

                            return true;
                        } else {
                            Player addPlayer = Bukkit.getPlayer(input);

                            if (addPlayer == null) {
                                player.sendMessage(ConvertUtils.color("&cKļūda: tāds spēlētājs nav tiešsaistē!"));
                                return true;
                            }

                            List<UUID> allowedPlayers = portal.getAllowedPlayers();
                            
                            if (allowedPlayers.contains(addPlayer.getUniqueId())) {
                                player.sendMessage(ConvertUtils.color("&cKļūda: tam spēlētājam jau ir atļauts izmantot šo portālu!"));
                                return true;
                            }

                            if (player.equals(addPlayer)) {
                                player.sendMessage(ConvertUtils.color("&cKļūda: nevari pats sevi pievienot šim portālam!"));
                                return true;
                            }

                            if (portal.getLand().isTrusted(addPlayer.getUniqueId())) {
                                player.sendMessage(ConvertUtils.color("&cKļūda: tas spēlētājs jau pieder teritorijai, kurā atrodas šis portāls!"));
                                return true;
                            }
                            
                            allowedPlayers.add(addPlayer.getUniqueId());
                            portal.setAllowedPlayers(allowedPlayers);
                            dataManager.save(portal);

                            player.sendMessage(ConvertUtils.color("&7Veiksmīgi atļāvi spēlētājam &f" + addPlayer.getName() + " &7izmantot šo portālu!"));

                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("nonemt")) {
                        if (!args[1].equalsIgnoreCase("t") && !args[1].equalsIgnoreCase("s")) {
                            player.sendMessage(ConvertUtils.color("&cNoradi, vai nonemsi teritoriju (t) vai speletaju (s)! " + args[1]));
                            return true;
                        }

                        StringBuilder builder = new StringBuilder();
                        for (int i = 2; i < args.length; i++) {
                            builder.append(args[i]);
                        }
                        String input = builder.toString();

                        if (args[1].equalsIgnoreCase("t")) {
                            Land land = landsAPI.getLand(input);

                            if (land == null) {
                                player.sendMessage(ConvertUtils.color("&cKļūda: tāda teritorija neeksistē!"));
                                return true;
                            }

                            List<Integer> allowedLands = portal.getAllowedLands();
                            
                            if (!allowedLands.contains(land.getId())) {
                                player.sendMessage(ConvertUtils.color("&cKļūda: tādai teritorija nav pievienota šī portāla atļautajā sarakstā!"));
                                return true;
                            }
                            
                            allowedLands.remove((Object)land.getId());
                            portal.setAllowedlands(allowedLands);
                            dataManager.save(portal);

                            player.sendMessage(ConvertUtils.color("&7Veiksmīgi noņēmi teritoriju &f" + land.getName() + " &7atļauju izmantot šo portālu!"));

                            return true;
                        } else {
                            OfflinePlayer remPlayer = Bukkit.getOfflinePlayer(input);

                            if (!remPlayer.hasPlayedBefore()) {
                                player.sendMessage(ConvertUtils.color("&cKļūda: tāds spēlētājs nav iepriekš pievienojies serverim!"));
                                return true;
                            }

                            List<UUID> allowedPlayers = portal.getAllowedPlayers();
                            
                            if (!allowedPlayers.contains(remPlayer.getUniqueId())) {
                                player.sendMessage(ConvertUtils.color("&cKļūda: tas spēlētājs nav pievienots šī portāla atļautajā sarakstā!"));
                                return true;
                            }
                            
                            allowedPlayers.remove(remPlayer.getUniqueId());
                            portal.setAllowedPlayers(allowedPlayers);
                            dataManager.save(portal);

                            player.sendMessage(ConvertUtils.color("&7Veiksmīgi noņēmi spēlētājam &f" + remPlayer.getName() + " &7atļauju izmantot šo portālu!"));

                            return true;
                        }
                    }
                }

                return true;
            }

            String[] messages = {
                "",
                "  &5&lPortālu komandas",
                "",
                "&f/p apraksts <apraksts> &7&omaina aprakstu portālam, kurā pašlaik atrodies",
                "&f/p pievienot t <teritorija> &7&oatļauj norādītajai teritorijai izmantot portālu",
                "&f/p pievienot s <speletajs> &7&oatļauj norādītajam spēlētājam izmantot portālu",
                "",
                "&f/p nonemt t <teritorija> &7&onoņem teritorijai atļauju izmantot portālu",
                "&f/p nonemt s <speletajs> &7&onoņem spēlētājam atļauju izmantot portālu",
                ""
            };

            player.sendMessage(ConvertUtils.color(messages));
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<String>();

        if (args.length == 1) {
            completions.add("apraksts");
            completions.add("pievienot");
            completions.add("nonemt");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("pievienot") || args[0].equalsIgnoreCase("nonemt")) {
                completions.add("s");
                completions.add("t");
            }
        } else if (args.length == 3) {
            if ((args[0].equalsIgnoreCase("pievienot") || args[0].equalsIgnoreCase("nonemt")) && 
            (args[1].equalsIgnoreCase("t") || args[1].equalsIgnoreCase("s"))) {
                return null;
            }
        }

        return completions;
    }
}
