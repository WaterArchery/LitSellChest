package me.waterarchery.litsellchest.configuration.gui;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litlibs.inventory.InventoryImpl;

public class DefaultMenu extends InventoryImpl {

    public DefaultMenu(ConfigManager file, String path, LitLibs litLibs) {
        super(file, path, litLibs);
    }

}
