package me.waterarchery.litsellchest.configuration.gui;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litlibs.handlers.InventoryHandler;
import me.waterarchery.litlibs.handlers.MessageHandler;
import me.waterarchery.litlibs.inventory.ActionType;
import me.waterarchery.litlibs.inventory.InventoryImpl;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.handlers.ChestHandler;
import me.waterarchery.litsellchest.handlers.ConfigHandler;
import me.waterarchery.litsellchest.handlers.GUIHandler;
import me.waterarchery.litsellchest.models.SellChestType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShopMenu extends InventoryImpl {

    public ShopMenu(ConfigManager file, String path, LitLibs litLibs) {
        super(file, path, litLibs);
    }


    public Inventory generateInventory(Player player) {
        GUIHandler guiHandler = GUIHandler.getInstance();
        LitLibs libs = LitSellChest.getLibs();
        MessageHandler mHandler = libs.getMessageHandler();
        ChestHandler chestHandler = ChestHandler.getInstance();
        ConfigManager configManager = new ConfigManager(libs, "gui", "shop_menu", false);

        String menuName = "shop_menu";
        FileConfiguration yaml = ConfigHandler.getInstance().getGUIYaml(menuName);

        int size = yaml.getInt(menuName + ".size");
        String name = yaml.getString(menuName + ".name");
        name = mHandler.updateColors(name);

        int playerChestLimit = chestHandler.getMaxPlaceableChests(player);
        int playerChestCount = chestHandler.getChestCount(player);

        Inventory shopGUI = Bukkit.createInventory(null, size, name);
        for (String path : yaml.getConfigurationSection(menuName + ".items").getKeys(false)) {
            ItemStack itemStack = guiHandler.createItem(menuName, path);
            List<String> oldLore = itemStack.getItemMeta().getLore();
            SellChestType sellChestType = chestHandler.getType(path);

            String chestName = sellChestType.getName();
            double tax = sellChestType.getTax();
            double sellMultiplier = sellChestType.getSellMultiplier();
            double sellInterval = sellChestType.getSellInterval();
            double price = sellChestType.getPrice();

            ArrayList<String> newLore = new ArrayList<>();
            for (String part : oldLore) {
                part = mHandler.updateColors(part);
                part = part.replace("%name%", chestName);
                part = part.replace("%tax%", tax + "");
                part = part.replace("%sellMultiplier%", sellMultiplier + "");
                part = part.replace("%sellInterval%", sellInterval + "");
                part = part.replace("%price%", price + "");
                part = part.replace("%playerLimit%", playerChestLimit + "");
                part = part.replace("%playerCount%", playerChestCount + "");
                newLore.add(part);
            }
            ItemMeta meta = itemStack.getItemMeta();
            meta.setLore(newLore);
            String itemName = meta.getDisplayName();
            itemName = itemName.replace("%name%", chestName);
            meta.setDisplayName(itemName);
            itemStack.setItemMeta(meta);
            libs.getNBTAPIHook().setGUIAction(path, itemStack, configManager.getName(), ActionType.PLUGIN);
            int slot = yaml.getInt(menuName + ".items." + path + ".slot");
            shopGUI.setItem(slot, itemStack);
        }

        if (yaml.getBoolean(menuName + ".fillMenu.enabled", true)) {
            InventoryHandler inventoryHandler = libs.getInventoryHandler();
            inventoryHandler.fillGUI(shopGUI, menuName, configManager);
        }

        return shopGUI;
    }

}
