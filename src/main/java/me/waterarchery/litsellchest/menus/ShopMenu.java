package me.waterarchery.litsellchest.menus;

import com.chickennw.utils.configurations.menu.SymboledMenuItem;
import com.chickennw.utils.libs.annotations.NotNull;
import com.chickennw.utils.libs.gui.components.GuiAction;
import com.chickennw.utils.models.menus.MenuType;
import com.chickennw.utils.models.menus.TriumphMenuWrapper;
import com.chickennw.utils.models.menus.item.TriumphItemHolder;
import com.chickennw.utils.utils.ChatUtils;
import com.chickennw.utils.utils.ConfigUtils;
import me.waterarchery.litsellchest.configuration.menus.ShopMenuFile;
import me.waterarchery.litsellchest.managers.ChestManager;
import me.waterarchery.litsellchest.models.SellChestType;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ShopMenu extends TriumphMenuWrapper {

    private final Player player;

    public ShopMenu(Player player) {
        super(ConfigUtils.get(ShopMenuFile.class), MenuType.SIMPLE);
        this.player = player;
    }

    @Override
    public Function<String, String> placeholderParser() {
        return (text) -> text;
    }

    @Override
    public TriumphItemHolder generateItem(OfflinePlayer player, SymboledMenuItem symboledMenuItem, String id) {
        TriumphItemHolder holder = super.generateItem(player, symboledMenuItem, id);

        ChestManager chestManager = ChestManager.getInstance();
        SellChestType sellChestType = chestManager.getType(id);

        if (sellChestType != null) {
            ItemStack itemStack = holder.guiItem().getItemStack();
            ItemMeta meta = itemStack.getItemMeta();

            int playerChestLimit = chestManager.getMaxPlaceableChests(player);
            int playerChestCount = chestManager.getChestCount(player);

            String chestName = sellChestType.getName();
            double tax = sellChestType.getTax();
            double sellMultiplier = sellChestType.getSellMultiplier();
            double sellInterval = sellChestType.getSellInterval();
            double price = sellChestType.getPrice();

            if (meta != null && meta.hasDisplayName()) {
                String displayName = meta.getDisplayName().replace("%name%", chestName);
                meta.setDisplayName(ChatUtils.colorizeLegacy(displayName));
            }

            if (meta != null && meta.hasLore()) {
                List<String> newLore = new ArrayList<>();
                for (String part : Objects.requireNonNull(meta.getLore())) {
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
            }

            itemStack.setItemMeta(meta);
            holder.guiItem().setItemStack(itemStack);
        }

        return holder;
    }

    @Override
    public Runnable nextPageRunnable() {
        return null;
    }

    @Override
    public Runnable previousPageRunnable() {
        return null;
    }

    @Override
    public HashMap<String, GuiAction<InventoryClickEvent>> premadeGuiActions() {
        HashMap<String, GuiAction<InventoryClickEvent>> actions = new HashMap<>();
        ChestManager chestManager = ChestManager.getInstance();

        for (SellChestType chestType : chestManager.getChestTypes()) {
            actions.put(chestType.getId(), event -> {
                chestManager.handleShopBuy(player, chestType);
            });
        }

        return actions;
    }

    @Override
    public GuiAction<@NotNull InventoryClickEvent> getDefaultClickAction() {
        return (event) -> {
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
