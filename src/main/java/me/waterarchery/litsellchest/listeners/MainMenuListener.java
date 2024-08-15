package me.waterarchery.litsellchest.listeners;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.hooks.other.NBTAPIHook;
import me.waterarchery.litlibs.inventory.Action;
import me.waterarchery.litlibs.utils.CompatibilityUtil;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.configuration.gui.YourChestsMenu;
import me.waterarchery.litsellchest.handlers.ConfigHandler;
import me.waterarchery.litsellchest.handlers.GUIHandler;
import me.waterarchery.litsellchest.handlers.SoundManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MainMenuListener implements Listener {

    @EventHandler
    public void onMainMenuClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        ItemStack itemStack = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (inventory != null && itemStack != null && itemStack.getType() != Material.AIR) {
            String menuTitle = CompatibilityUtil.getTitle(event);
            String title = ConfigHandler.getInstance().getGuiString("default_menu", "default_menu" + ".name");
            if (title.equalsIgnoreCase(menuTitle))
                event.setCancelled(true);
            LitLibs libs = LitSellChest.getInstance().getLibs();
            NBTAPIHook nbtapiHook = libs.getNBTAPIHook();
            GUIHandler guiHandler = GUIHandler.getInstance();
            Action action = nbtapiHook.getGUIAction(itemStack);
            if (action != null) {
                event.setCancelled(true);
                if (action.getAction().equalsIgnoreCase("open_shop")) {
                    guiHandler.openShop(player);
                }
                else if (action.getAction().equalsIgnoreCase("your_chests")) {
                    Inventory yourChests = YourChestsMenu.generateInventory(player);
                    player.openInventory(yourChests);
                    SoundManager.sendSound(player, "YourChestsOpened");
                }
            }
        }
    }

}
