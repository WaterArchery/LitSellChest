package me.waterarchery.litsellchest.models;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigNameHolder {

    FileConfiguration configuration;
    String name;

    public ConfigNameHolder(FileConfiguration configuration, String name) {
        this.name = name;
        this.configuration = configuration;
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public String getName() {
        return name;
    }

}
