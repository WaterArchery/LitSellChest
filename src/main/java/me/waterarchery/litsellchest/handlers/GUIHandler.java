package me.waterarchery.litsellchest.handlers;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litlibs.handlers.MessageHandler;
import me.waterarchery.litlibs.hooks.other.NBTAPIHook;
import me.waterarchery.litlibs.inventory.ActionType;
import me.waterarchery.litlibs.libs.skullcreator.SkullCreator;
import me.waterarchery.litlibs.libs.xseries.XMaterial;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.configuration.gui.DefaultMenu;
import me.waterarchery.litsellchest.configuration.gui.ShopMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class GUIHandler {

    private static GUIHandler instance;
    private DefaultMenu defaultMenu;
    private ShopMenu shopMenu;

    public static synchronized GUIHandler getInstance() {
        if (instance == null) instance = new GUIHandler();
        return instance;
    }

    private GUIHandler() { initialize(); }

    public void initialize() {
        LitLibs libs = LitSellChest.getInstance().getLibs();

        ConfigManager defaultMenuFile = new ConfigManager(libs, "gui", "default_menu", false);
        defaultMenu = new DefaultMenu(defaultMenuFile, "default_menu",  libs);

        ConfigManager shopMenuFile = new ConfigManager(libs, "gui", "shop_menu", false);
        shopMenu = new ShopMenu(shopMenuFile, "shop_menu",  libs);

        new ConfigManager(libs, "gui", "your_chests", false);
    }

    public void openShop(Player player) {
        ShopMenu shopMenu = getShopMenu();
        player.openInventory(shopMenu.generateInventory(player));
        SoundManager.sendSound(player, "ShopMenuOpened");
    }

    public ItemStack createItem(String menuName, String path){
        ConfigHandler configHandler = ConfigHandler.getInstance();
        LitLibs libs = LitSellChest.getInstance().getLibs();;
        MessageHandler messageHandler = libs.getMessageHandler();
        NBTAPIHook nbtapiHook = libs.getNBTAPIHook();

        String mat = configHandler.getGuiString(menuName, menuName + ".items." + path + ".material");
        String name = configHandler.getGuiString(menuName, menuName + ".items." + path + ".name");
        List<String> tempLore = configHandler.getGUIYaml(menuName).getStringList(menuName + ".items." + path + ".lore");
        ArrayList<String> lore = new ArrayList<>();
        ItemStack itemStack = parseRawMaterial(mat, menuName, path);

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (configHandler.getGUIYaml(menuName).get(menuName + ".items." + path + ".customModelData") != null) {
            itemMeta.setCustomModelData(configHandler.getGUIYaml(menuName).getInt(menuName + ".items." + path + ".customModelData"));
        }
        for (String part : tempLore) {
            part = messageHandler.updateColors(part);
            lore.add(part.replace("&", "§"));
        }
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        String customCMD = configHandler.getGUIYaml(menuName).getString(menuName + ".items." + path + ".command", "");
        boolean isCustomCommand = !customCMD.equalsIgnoreCase("");
        if (isCustomCommand) {
            path = "CUSTOM_CMD " + customCMD;
            return nbtapiHook.setGUIAction(path, itemStack, menuName, ActionType.COMMAND);
        }
        else {
            return nbtapiHook.setGUIAction(path, itemStack, menuName, ActionType.PLUGIN);
        }
    }

    public ItemStack parseRawMaterial(String mat, String menuName, String path) {
        ItemStack itemStack;
        if (!mat.contains("HEAD-")) {
            Optional<XMaterial> material = XMaterial.matchXMaterial(mat);
            if (material.isPresent())
                itemStack = material.get().parseItem();
            else {
                itemStack = new ItemStack(Material.STONE);
                Bukkit.getConsoleSender().sendMessage("§cLitMinions error on menu: " + menuName + " and item path: " + path);
            }
        }
        else {
            itemStack = SkullCreator.itemFromBase64(mat.replace("HEAD-", ""));
        }
        if (itemStack == null) {
            itemStack = new ItemStack(Material.STONE);
        }

        return itemStack;
    }

    public DefaultMenu getDefaultMenu() { return defaultMenu; }
    public ShopMenu getShopMenu() { return shopMenu; }
}
