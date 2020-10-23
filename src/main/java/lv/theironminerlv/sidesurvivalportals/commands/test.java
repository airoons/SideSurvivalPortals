package lv.theironminerlv.sidesurvivalportals.commands;

import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lv.theironminerlv.sidesurvivalportals.data.PortalData;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;

public class test implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            player.sendMessage("whatisthis");

            Map<String, Portal> portals;
            portals = PortalData.getByWorld(player.getWorld());
            player.sendMessage("array size " + portals.size());
        }
        
        return true;
    }
}
