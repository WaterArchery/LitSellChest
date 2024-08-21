package me.waterarchery.litsellchest.listeners;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.hooks.other.NBTAPIHook;
import me.waterarchery.litlibs.inventory.Action;
import me.waterarchery.litlibs.inventory.ActionType;
import me.waterarchery.litlibs.utils.CompatibilityUtil;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.handlers.ChestHandler;
import me.waterarchery.litsellchest.handlers.ConfigHandler;
import me.waterarchery.litsellchest.handlers.SoundManager;
import me.waterarchery.litsellchest.hooks.VaultHook;
import me.waterarchery.litsellchest.models.SellChestType;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.joml.RoundingMode;

import java.math.BigDecimal;

public class ShopMenuListener implements Listener {

    private double round(double value) {
        if (decimals < 0) {decimals = 2;}
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(decimals, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @EventHandler
    public void onShopMenuClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        ItemStack itemStack = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        LitLibs libs = LitSellChest.getInstance().getLibs();

        if (inventory != null && itemStack != null && itemStack.getType() != Material.AIR) {
            String menuTitle = CompatibilityUtil.getTitle(event);
            String title = ConfigHandler.getInstance().getGuiString("shop_menu", "shop_menu" + ".name");
            if (title.equalsIgnoreCase(menuTitle))
                event.setCancelled(true);
            NBTAPIHook nbtapiHook = libs.getNBTAPIHook();
            Action action = nbtapiHook.getGUIAction(itemStack);
            VaultHook vaultHook = VaultHook.getInstance();
            Economy economy = vaultHook.getEcon();
            if (action != null && action.getType() == ActionType.PLUGIN) {
                if (action.getGuiName().equalsIgnoreCase("shop_menu")) {
                    ChestHandler chestHandler = ChestHandler.getInstance();
                    SellChestType sellChestType = chestHandler.getType(action.getAction());
                    if (sellChestType != null) {
                        double price = sellChestType.getPrice();
                        double balance = economy.getBalance(player);
                        if (price <= balance) {
                            economy.withdrawPlayer(player, price);
                            ItemStack placeItem = sellChestType.toItemStack();
                            player.getInventory().addItem(placeItem);
                            String msg = ConfigHandler.getInstance().getMessageLang("ChestBought")
                                    .replace("%money%", (balance - price) + "");
                            if(LitSellChest.getInstance().getConfig().getBoolean("PaymentFormatting",false)) {
                                BigDecimal bd = new BigDecimal(Double.toString(balance));
                                bd = bd.setScale(LitSellChest.getInstance().getConfig().getInt("EcoPayRoundupDecimals",2), RoundingMode.HALF_UP);
                                msg.replace("%money%",String.valueOf(bd.doubleValue()));
                            }
                            libs.getMessageHandler().sendMessage(player, msg);
                            SoundManager.sendSound(player, "ChestReceive");
                        }
                        else {
                            ConfigHandler.getInstance().sendMessageLang(player, "NotEnoughMoney");
                        }
                    }
                    else {
                        libs.getLogger().error("SellChestType " + action.getAction() + " not found in shop.");
                    }
                }
            }
        }
    }

}
