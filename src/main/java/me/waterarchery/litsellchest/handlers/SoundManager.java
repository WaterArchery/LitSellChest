package me.waterarchery.litsellchest.handlers;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litlibs.handlers.SoundHandler;
import me.waterarchery.litsellchest.LitSellChest;
import org.bukkit.entity.Player;

public class SoundManager {

    public static void sendSound(Player player, String path) {
        ConfigHandler configHandler = ConfigHandler.getInstance();
        LitLibs libs = LitSellChest.getInstance().getLibs();
        ConfigManager config = configHandler.getConfig();
        SoundHandler soundHandler = libs.getSoundHandler();

        if (config.getYml().getBoolean("SoundsEnabled")) {
            double volume = config.getYml().getDouble("SoundsVolume");
            soundHandler.sendSound(player, "Sounds." + path, config, volume);
        }
    }
}
