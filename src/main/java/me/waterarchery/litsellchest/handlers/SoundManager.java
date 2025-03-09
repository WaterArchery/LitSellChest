package me.waterarchery.litsellchest.handlers;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.handlers.SoundHandler;
import me.waterarchery.litsellchest.LitSellChest;
import org.bukkit.entity.Player;

public class SoundManager {

    public static void sendSound(Player player, String path) {
        ConfigHandler configHandler = ConfigHandler.getInstance();
        LitLibs libs = LitSellChest.getLibs();
        SoundHandler soundHandler = libs.getSoundHandler();

        soundHandler.sendSound(player, "Sounds." + path);
    }
}
