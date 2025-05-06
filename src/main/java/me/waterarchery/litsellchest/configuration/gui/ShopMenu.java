package me.waterarchery.litsellchest.configuration.gui;

import me.waterarchery.litlibs.hooks.other.NBTAPIHook;
import me.waterarchery.litlibs.impl.gui.LitGui;
import me.waterarchery.litlibs.impl.gui.items.LitMenuItem;
import me.waterarchery.litlibs.inventory.Action;
import me.waterarchery.litlibs.libs.gui.components.GuiAction;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.handlers.ChestHandler;
import me.waterarchery.litsellchest.handlers.ConfigHandler;
import me.waterarchery.litsellchest.handlers.SoundManager;
import me.waterarchery.litsellchest.hooks.VaultHook;
import me.waterarchery.litsellchest.models.SellChestType;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopMenu extends LitGui {

    public ShopMenu() {
        super("shop_menu", ConfigHandler.getInstance().getGUIYaml("shop_menu"), LitSellChest.getInstance());
    }

    @Override
    public LitMenuItem generateItem(OfflinePlayer player, String itemPath) {
        LitMenuItem item = super.generateItem(player, itemPath);

        ItemStack itemStack = item.getGuiItem().getItemStack();
        ItemMeta meta = itemStack.getItemMeta();

        List<String> oldLore = meta.getLore();
        List<String> newLore = new ArrayList<>();

        ChestHandler chestHandler = ChestHandler.getInstance();
        int playerChestLimit = chestHandler.getMaxPlaceableChests(player);
        int playerChestCount = chestHandler.getChestCount(player);

        SellChestType sellChestType = chestHandler.getType(itemPath);
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

        return item;
    }

    @Override
    public HashMap<String, String> getPlaceholders() {
        return null;
    }

    @Override
    public HashMap<String, GuiAction<InventoryClickEvent>> premadeGuiActions() {
        return new HashMap<>();
    }

    @Override
    public GuiAction<@NotNull InventoryClickEvent> getDefaultClickAction() {
        return (event) -> {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            NBTAPIHook nbtapiHook = litLibs.getNBTAPIHook();
            ItemStack itemStack = event.getCurrentItem();
            if (itemStack == null || itemStack.getType() == Material.AIR || itemStack.getAmount() < 1) return;

            Action action = nbtapiHook.getGUIAction(itemStack);
            VaultHook vaultHook = VaultHook.getInstance();
            Economy economy = vaultHook.getEcon();

            ChestHandler chestHandler = ChestHandler.getInstance();
            SellChestType sellChestType = chestHandler.getType(action.getAction());

            if (sellChestType != null) {
                double price = sellChestType.getPrice();
                double balance = economy.getBalance(player);
                if (price <= balance) {
                    economy.withdrawPlayer(player, price);
                    ItemStack placeItem = sellChestType.toItemStack();
                    player.getInventory().addItem(placeItem);
                    String mes = ConfigHandler.getInstance().getRawMessageLang("ChestBought")
                            .replace("%money%", (balance - price) + "");

                    litLibs.getMessageHandler().sendMessage(player, mes);
                    SoundManager.sendSound(player, "ChestReceive");
                } else {
                    ConfigHandler.getInstance().sendMessageLang(player, "NotEnoughMoney");
                }
            } else {
                litLibs.getLogger().error("SellChestType " + action.getAction() + " not found in shop.");
            }
        };
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
