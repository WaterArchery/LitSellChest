package me.waterarchery.litsellchest.handlers;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.configuration.config.Config;
import me.waterarchery.litsellchest.configuration.config.DefaultLang;
import me.waterarchery.litsellchest.models.ConfigNameHolder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ConfigHandler {

    private ConfigManager config;
    private ConfigManager lang;
    private ConfigManager chests;
    private static ConfigHandler instance;

    public static ConfigHandler getInstance() {
        if (instance == null) instance = new ConfigHandler();
        return instance;
    }

    private ConfigHandler() {
        LitSellChest instance = LitSellChest.getInstance();
        LitLibs libs = LitSellChest.getInstance().getLibs();
        // Saving and loading twice because
        // yaml.setComment() not working without saving
        // and I am too lazy to write a extensize logic
        // for it.
        //
        // It is easier this way.
        config = new Config(libs, "", "config", true);
        config = new Config(libs, "", "config", true);
        lang = new DefaultLang(libs, "lang", "lang-en", true);
        lang = new DefaultLang(libs, "lang", "lang-en", true);
        chests = new ConfigManager(libs, "", "chests", true);
        chests = new ConfigManager(libs, "", "chests", true);

        saveDefaultYaml("default_menu", "gui");
        saveDefaultYaml("shop_menu", "gui");
        saveDefaultYaml("your_chests", "gui");
    }

    private void saveDefaultYaml(String fileName, String folder) {
        LitSellChest instance = LitSellChest.getInstance();
        File file = new File(LitSellChest.getInstance().getDataFolder(), "/"+ folder + "/" + fileName + ".yml");
        if (!file.exists()) {
            try {
                if (!file.exists()) {
                    instance.saveResource(folder + "/" + file.getName(), false);
                }
            } catch (NullPointerException | IllegalArgumentException e) {
                Bukkit.getConsoleSender().sendMessage("§7[§bLitSellChest§7] §finvalid " + fileName + " file is empty");
            }
            if (folder.equalsIgnoreCase("lang")) { return; }
        }
        YamlConfiguration yaml = new YamlConfiguration();
        try {
            yaml.load(file);
            ConfigNameHolder configNameHolder = new ConfigNameHolder(yaml, fileName);
             if (folder.equalsIgnoreCase("gui"))
                guiFileList.add(configNameHolder);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    ArrayList<ConfigNameHolder> guiFileList = new ArrayList<>();
    public FileConfiguration getGUIYaml(String guiName) {
        for (ConfigNameHolder guiFile : guiFileList) {
            if (guiFile.getName().equalsIgnoreCase(guiName)) {
                return guiFile.getConfiguration();
            }
        }
        return null;
    }

    public String getGuiString(String gui, String path){
        FileConfiguration yaml = getGUIYaml(gui);
        LitLibs libs = LitSellChest.getInstance().getLibs();
        return libs.getMessageHandler().updateColors(yaml.getString(path, path + "ERROR"));
    }

    public void sendMessageLang(CommandSender target, String path) {
        LitLibs libs = LitSellChest.getInstance().getLibs();
        libs.getMessageHandler().sendMessage(target, getMessageLang(path));
    }

    public String getMessageLang(String path) {
        ConfigHandler configHandler = ConfigHandler.getInstance();
        ConfigManager manager = configHandler.getLang();
        return manager.getString(path);
    }

    public ConfigManager getConfig() {
        return config;
    }

    public ConfigManager getLang() {
        return lang;
    }

    public ConfigManager getChests() {
        return chests;
    }

}
