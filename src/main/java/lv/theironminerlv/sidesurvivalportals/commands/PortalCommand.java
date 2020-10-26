package lv.theironminerlv.sidesurvivalportals.commands;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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

public class PortalCommand implements CommandExecutor, TabExecutor
{
    private static SideSurvivalPortals plugin = SideSurvivalPortals.getInstance();
    private static PortalManager portalManager = plugin.getPortalManager();
    private static PermissionManager permissionManager = plugin.getPermissionManager();
    private static DataManager dataManager = plugin.getDataManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length >= 1) {
                if (args.length == 1 && args[0].equalsIgnoreCase("apraksts")) {
                    player.sendMessage(ConvertUtils.color("&cNeesi norādījis aprakstu!"));
                    return true;
                }
                
                if (args[0].equalsIgnoreCase("apraksts")) {
                    Portal portal = portalManager.getPortalAt(player.getLocation());
    
                    if (portal == null) {
                        player.sendMessage(ConvertUtils.color("&cTev ir jāatrodas portālā, lai izmantotu šo komandu!"));
                        return true;
                    }
    
                    if (!permissionManager.canEditPortal(player, portal.getLand())) {
                        player.sendMessage(ConvertUtils.color("&cTev nav atļauts labot šo portālu!"));
                        return true;
                    }
    
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

            }

            String[] messages = {
                "",
                "  &5&lPortālu komandas",
                "",
                "&f/p apraksts [apraksts] &7&omaina aprakstu portālam, kurā pašlaik atrodies",
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

        if (args.length == 1)
            completions.add("apraksts");

        return completions;
    }
}
