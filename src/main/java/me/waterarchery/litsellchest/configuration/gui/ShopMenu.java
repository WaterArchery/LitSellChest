package me.waterarchery.litsellchest.configuration.gui;

import com.chickennw.utils.libs.themoep.inventorygui.GuiElement;
import com.chickennw.utils.libs.themoep.inventorygui.InventoryGui;
import com.chickennw.utils.libs.themoep.inventorygui.StaticGuiElement;
import com.chickennw.utils.models.menus.LitMenu;
import com.chickennw.utils.utils.ConfigUtils;
import me.waterarchery.litsellchest.configuration.config.ChestsFile;
import me.waterarchery.litsellchest.managers.ChestManager;
import me.waterarchery.litsellchest.models.SellChestType;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopMenu extends LitMenu {

    public ShopMenu(Player player) {
        super("shop_menu", player);
    }

    @Override
    public HashMap<String, GuiElement.Action> getGuiActions() {
        HashMap<String, GuiElement.Action> actions = new HashMap<>();

        return actions;
    }

    @Override
    public String parsePlaceholder(String s) {
        return s;
    }

    @Override
    public List<String> parsePlaceholderAsList(String s) {
        return List.of(s);
    }

    @Override
    public InventoryGui.CloseAction getCloseAction() {
        return (close) -> false;
    }

    public List<GuiElement> generateGuiElements() {
        List<GuiElement> elements = new ArrayList<>();
        ChestsFile chestsFile = ConfigUtils.get(ChestsFile.class);

        yaml.getConfigurationSection("items").getKeys(false).forEach(itemPath -> {
            ChestsFile.ChestConfig chestConfig = chestsFile.getChests().get(itemPath);
            ItemStack itemStack = generateItemStack(player, itemPath, chestConfig);
            char symbol = yaml.getString("items." + itemPath + ".symbol").charAt(0);

            GuiElement.Action action = (click) -> {
                ChestManager chestManager = ChestManager.getInstance();
                SellChestType sellChestType = chestManager.getType(itemPath);
                chestManager.handleShopBuy(player, sellChestType);
                return true;
            };

            StaticGuiElement staticGuiElement = new StaticGuiElement(symbol, itemStack, action);
            elements.add(staticGuiElement);
        });

        return elements;
    }

    private ItemStack generateItemStack(OfflinePlayer player, String chestId, ChestsFile.ChestConfig chestConfig) {
        ItemStack itemStack = super.createItemStack(player.getUniqueId(), "items." + chestId);
        ItemMeta meta = itemStack.getItemMeta();

        List<String> oldLore = meta.getLore();
        List<String> newLore = new ArrayList<>();

        ChestManager chestManager = ChestManager.getInstance();
        int playerChestLimit = chestManager.getMaxPlaceableChests(player);
        int playerChestCount = chestManager.getChestCount(player);

        SellChestType sellChestType = chestManager.getType(chestId);
        String chestName = sellChestType.getName();
        double tax = sellChestType.getTax();
        double sellMultiplier = sellChestType.getSellMultiplier();
        double sellInterval = sellChestType.getSellInterval();
        double price = sellChestType.getPrice();

        for (String part : oldLore) {
            part = part.replace("%name%", chestName);
            part = part.replace("%tax%", tax + "");
            part = part.replace("%sellMultiplier%", sellMultiplier + "");
            part = part.replace("%sellInterval%", sellInterval + "");
            part = part.replace("%price%", price + "");
            part = part.replace("%playerLimit%", playerChestLimit + "");
            part = part.replace("%playerCount%", playerChestCount + "");
            newLore.add(part);
        }

        meta.setLore(newLore);
        String itemName = meta.getDisplayName();
        itemName = itemName.replace("%name%", chestName);
        meta.setDisplayName(itemName);
        itemStack.setItemMeta(meta);

        return itemStack;
    }
}
