package me.waterarchery.litsellchest.configuration.gui;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.handlers.MessageHandler;
import me.waterarchery.litlibs.hooks.other.NBTAPIHook;
import me.waterarchery.litlibs.inventory.ActionType;
import me.waterarchery.litlibs.utils.ChatUtils;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.handlers.ChestHandler;
import me.waterarchery.litsellchest.handlers.ConfigHandler;
import me.waterarchery.litsellchest.models.SellChest;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class YourChestsMenu {

    public static Inventory generateInventory(Player player) {
        LitLibs libs = LitSellChest.getLibs();
        MessageHandler mHandler = libs.getMessageHandler();
        ChestHandler chestHandler = ChestHandler.getInstance();
        NBTAPIHook nbtapiHook = libs.getNBTAPIHook();

        String menuName = "your_chests";
        FileConfiguration yaml = ConfigHandler.getInstance().getGUIYaml(menuName);

        int size = yaml.getInt(menuName + ".size");
        String name = yaml.getString(menuName + ".name");
        name = ChatUtils.colorizeLegacy(name);

        Inventory inventory = Bukkit.createInventory(null, size, name);
        List<SellChest> chests = chestHandler.getPlayerChests(player);
        for (SellChest chest : chests) {
            String rawMaterial = yaml.getString("your_chests.chestItem.material");
            String itemName = yaml.getString("your_chests.chestItem.name");
            List<String> rawLore = yaml.getStringList("your_chests.chestItem.lore");

            ItemStack itemStack = new ItemStack(Material.valueOf(rawMaterial));
            ItemMeta meta = itemStack.getItemMeta();
            itemName = ChatUtils.colorizeLegacy(itemName);
            meta.setDisplayName(itemName);

            List<String> lore = new ArrayList<>();
            for (String line : rawLore) {
                line = ChatUtils.colorizeLegacy(line);
                line = line.replace("%world%", chest.getLocation().getWorld().getName());
                line = line.replace("%x%", chest.getLocation().getBlockX() + "");
                line = line.replace("%y%", chest.getLocation().getBlockY() + "");
                line = line.replace("%z%", chest.getLocation().getBlockZ() + "");
                line = line.replace("%loaded%", chest.isLoaded() + "");

                lore.add(line);
            }

            meta.setLore(lore);
            itemStack.setItemMeta(meta);

            itemStack = nbtapiHook.setGUIAction("your_chests", itemStack, "your_chests", ActionType.PLUGIN);
            itemStack = nbtapiHook.setNBT(itemStack, "chestID", chest.getUUID() + "");
            inventory.addItem(itemStack);
        }

        return inventory;
    }

}
