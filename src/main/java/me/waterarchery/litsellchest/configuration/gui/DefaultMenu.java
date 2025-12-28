package me.waterarchery.litsellchest.configuration.gui;

import com.chickennw.utils.libs.themoep.inventorygui.GuiElement;
import com.chickennw.utils.libs.themoep.inventorygui.InventoryGui;
import com.chickennw.utils.models.menus.LitMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class DefaultMenu extends LitMenu {

    public DefaultMenu(Player player) {
        super("default_menu", player);
    }

    @Override
    public HashMap<String, GuiElement.Action> getGuiActions() {
        HashMap<String, GuiElement.Action> actions = new HashMap<>();

        actions.put("open_shop", inventoryGui -> {
            ShopMenu shopMenu = new ShopMenu(player);
            shopMenu.openAsync(player, shopMenu.generateGuiElements());
            return true;
        });

        actions.put("your_chests", inventoryGui -> {
            YourChestsMenu yourChestsMenu = new YourChestsMenu(player);
            yourChestsMenu.openAsync(player);
            return true;
        });

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
}
