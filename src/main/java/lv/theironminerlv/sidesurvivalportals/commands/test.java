package lv.theironminerlv.sidesurvivalportals.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lv.theironminerlv.sidesurvivalportals.managers.SimpleInventory;

public class test implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            player.sendMessage("whatisthis");

            SimpleInventory inv = new SimpleInventory();
            inv.open(player);
        }
        
        return true;
    }
}
