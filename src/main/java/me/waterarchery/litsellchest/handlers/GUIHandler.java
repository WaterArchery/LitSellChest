package me.waterarchery.litsellchest.handlers;

import lombok.Getter;
import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.configuration.gui.ShopMenu;
import org.bukkit.entity.Player;

@Getter
public class GUIHandler {

    private static GUIHandler instance;

    public static synchronized GUIHandler getInstance() {
        if (instance == null) instance = new GUIHandler();
        return instance;
    }

    private GUIHandler() { initialize(); }

    public void initialize() {
        LitLibs libs = LitSellChest.getLibs();

        new ConfigManager(libs, "gui", "default_menu", false);
        new ConfigManager(libs, "gui", "shop_menu", false);
        new ConfigManager(libs, "gui", "your_chests", false);
    }

    public void openShop(Player player) {
        ShopMenu shopMenu = new ShopMenu();
        shopMenu.openAsync(player);
        SoundManager.sendSound(player, "ShopMenuOpened");
    }

}
