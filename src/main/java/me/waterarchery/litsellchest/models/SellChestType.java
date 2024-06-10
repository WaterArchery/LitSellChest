package me.waterarchery.litsellchest.models;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litlibs.libs.xseries.XMaterial;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.handlers.ConfigHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SellChestType {

    private final String id;
    private final String name;
    private final double tax;
    private final double sellMultiplier;
    private final double sellInterval;
    private final double price;

    public SellChestType(String id) {
        this.id = id;

        ConfigManager manager = ConfigHandler.getInstance().getChests();
        FileConfiguration yml = manager.getYml();

        name = manager.getString(getId() + ".name");
        tax = yml.getDouble(getId() + ".tax");
        sellMultiplier = yml.getDouble(getId() + ".sellMultiplier");
        sellInterval = yml.getInt(getId() + ".sellInterval");
        price = yml.getInt(getId() + ".price");
    }

    public ItemStack toItemStack() {
        ConfigManager manager = ConfigHandler.getInstance().getChests();
        FileConfiguration yml = manager.getYml();
        LitLibs lib = LitSellChest.getInstance().getLibs();
        String rawMaterial = yml.getString(getId() + ".item.material", "STONE");
        XMaterial material = XMaterial.valueOf(rawMaterial);
        String name = manager.getString(getId() + ".item.name");
        name = name.replace("%name%", getName()); // Parsing with Chest Name
        int customModelData = yml.getInt(getId() + ".item.customModelData", -9999);
        List<String> rawLore = yml.getStringList(getId() + ".item.lore");
        List<String> lore = new ArrayList<>();
        for (String part : rawLore) {
            part = part.replace("%tax%", getTax() + "");
            part = part.replace("%sellMultiplier%", getSellMultiplier() + "");
            part = part.replace("%sellInterval%", getSellInterval() + "");
            part = LitSellChest.getInstance().getLibs().getMessageHandler().updateColors(part);
            lore.add(part);
        }

        ItemStack itemStack = material.parseItem();
        ItemMeta meta = itemStack.getItemMeta();
        if (customModelData != -99999)
            meta.setCustomModelData(customModelData);
        meta.setDisplayName(name);
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        itemStack = lib.getNBTAPIHook().setNBT(itemStack, "SellChestType", getId());
        return itemStack;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getTax() {
        return tax;
    }

    public double getSellMultiplier() {
        return sellMultiplier;
    }

    public double getSellInterval() {
        return sellInterval;
    }

    public double getPrice() {
        return price;
    }
}
