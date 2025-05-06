package me.waterarchery.litsellchest.configuration.gui;

import me.waterarchery.litlibs.impl.gui.LitGui;
import me.waterarchery.litlibs.libs.gui.components.GuiAction;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.handlers.ConfigHandler;
import me.waterarchery.litsellchest.handlers.GUIHandler;
import me.waterarchery.litsellchest.handlers.SoundManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class DefaultMenu extends LitGui {

    public DefaultMenu() {
        super("default_menu", ConfigHandler.getInstance().getGUIYaml("default_menu"), LitSellChest.getInstance());
    }

    @Override
    public HashMap<String, String> getPlaceholders() {
        return null;
    }

    @Override
    public HashMap<String, GuiAction<InventoryClickEvent>> premadeGuiActions() {
        HashMap<String, GuiAction<InventoryClickEvent>> actions = new HashMap<>();
        GUIHandler guiHandler = GUIHandler.getInstance();

        actions.put("your_chests", (event) -> {
            Player player = (Player) event.getWhoClicked();

            Inventory yourChests = YourChestsMenu.generateInventory(player);
            player.openInventory(yourChests);
            SoundManager.sendSound(player, "YourChestsOpened");
        });

        actions.put("open_shop", (event) -> {
            Player player = (Player) event.getWhoClicked();

            guiHandler.openShop(player);
        });

        return actions;
    }

    @Override
    public GuiAction<@NotNull InventoryClickEvent> getDefaultClickAction() {
        return (event) -> event.setCancelled(true);
    }

    @Override
    public GuiAction<@NotNull InventoryClickEvent> getDefaultTopClickAction() {
        return (event) -> event.setCancelled(true);
    }

    @Override
    public GuiAction<@NotNull InventoryCloseEvent> getCloseGuiAction() {
        return null;
    }

}
