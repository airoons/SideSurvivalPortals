package lv.theironminerlv.sidesurvivalportals.managers;

import org.bukkit.entity.Player;

import lv.theironminerlv.sidesurvivalportals.SideSurvivalPortals;
import lv.theironminerlv.sidesurvivalportals.gui.MainMenu;
import lv.theironminerlv.sidesurvivalportals.objects.Portal;

public class MenuManager
{
    private SideSurvivalPortals plugin;

    public MenuManager(SideSurvivalPortals plugin) {
        this.plugin = plugin;
    }

    public void openMain(Player player, Portal portal) {
        MainMenu gui = new MainMenu(portal);
        gui.open(player, portal);
    }
}