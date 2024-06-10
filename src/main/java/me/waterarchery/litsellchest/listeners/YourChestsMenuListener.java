package me.waterarchery.litsellchest.listeners;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.hooks.other.NBTAPIHook;
import me.waterarchery.litlibs.inventory.Action;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.handlers.ChestHandler;
import me.waterarchery.litsellchest.handlers.ConfigHandler;
import me.waterarchery.litsellchest.models.SellChest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class YourChestsMenuListener implements Listener {

    @EventHandler
    public void onMainMenuClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        ItemStack itemStack = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (inventory != null && itemStack != null && itemStack.getType() != Material.AIR) {
            String menuTitle = event.getView().getTitle();
            String title = ConfigHandler.getInstance().getGuiString("your_chests", "your_chests" + ".name");

            if (title.equalsIgnoreCase(menuTitle)) event.setCancelled(true);
            LitLibs libs = LitSellChest.getInstance().getLibs();
            NBTAPIHook nbtapiHook = libs.getNBTAPIHook();
            Action action = nbtapiHook.getGUIAction(itemStack);

            if (action != null) {
                event.setCancelled(true);
                if (action.getAction().equalsIgnoreCase("your_chests")) {
                    String rawID = libs.getNBTAPIHook().getNBT(itemStack, "chestID");
                    if (rawID != null) {
                        UUID uuid = UUID.fromString(rawID);
                        ChestHandler chestHandler = ChestHandler.getInstance();
                        SellChest sellChest = chestHandler.getSellChest(uuid);
                        if (sellChest != null) {
                            chestHandler.openChestInventoryOpen(sellChest, player);
                        }
                    }
                }
            }
        }
    }

}
