package me.waterarchery.litsellchest.menus;

import com.chickennw.utils.configurations.menu.SymboledMenuItem;
import com.chickennw.utils.libs.annotations.NotNull;
import com.chickennw.utils.libs.gui.components.GuiAction;
import com.chickennw.utils.managers.HookManager;
import com.chickennw.utils.models.hooks.impl.other.VaultHook;
import com.chickennw.utils.models.menus.MenuType;
import com.chickennw.utils.models.menus.TriumphMenuWrapper;
import com.chickennw.utils.models.menus.item.TriumphItemHolder;
import com.chickennw.utils.utils.ChatUtils;
import com.chickennw.utils.utils.ConfigUtils;
import me.waterarchery.litsellchest.configuration.config.LangFile;
import me.waterarchery.litsellchest.configuration.menus.ChestMenuFile;
import me.waterarchery.litsellchest.managers.ChestManager;
import me.waterarchery.litsellchest.models.ChestStatus;
import me.waterarchery.litsellchest.models.SellChest;
import me.waterarchery.litsellchest.models.SellChestType;
import me.waterarchery.litsellchest.utils.LangUtils;
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

public class ChestMenu extends TriumphMenuWrapper {

    private final Player player;
    private final SellChest sellChest;

    public ChestMenu(Player player, SellChest sellChest) {
        super(ConfigUtils.get(ChestMenuFile.class), MenuType.SIMPLE);
        this.player = player;
        this.sellChest = sellChest;
    }

    @Override
    public Function<String, String> placeholderParser() {
        return (text) -> {
            SellChestType type = sellChest.getChestType();
            text = text.replace("%money%", LangUtils.formatNumber(sellChest.getMoney()));
            text = text.replace("%tax%", type.getTax() + "");
            text = text.replace("%sellMultiplier%", type.getSellMultiplier() + "");
            text = text.replace("%sellInterval%", type.getSellInterval() + "");
            text = text.replace("%status%", sellChest.statusToText());
            text = text.replace("%name%", type.getName());
            return text;
        };
    }

    @Override
    public TriumphItemHolder generateItem(OfflinePlayer player, SymboledMenuItem symboledMenuItem, String id) {
        TriumphItemHolder holder = super.generateItem(player, symboledMenuItem, id);
        ItemStack itemStack = holder.guiItem().getItemStack();
        ItemMeta meta = itemStack.getItemMeta();

        if (meta.hasLore()) {
            List<String> newLore = new ArrayList<>();
            for (String part : Objects.requireNonNull(meta.getLore())) {
                newLore.add(placeholderParser().apply(part));
            }
            meta.setLore(newLore);
        }

        if (meta.hasDisplayName()) {
            meta.setDisplayName(placeholderParser().apply(meta.getDisplayName()));
        }

        itemStack.setItemMeta(meta);
        holder.guiItem().setItemStack(itemStack);
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
        LangFile langFile = ConfigUtils.get(LangFile.class);

        actions.put("open-chest-inventory", event -> {
            chestManager.openChestInventoryOpen(sellChest, player);
        });

        actions.put("collect-money", event -> {
            double money = sellChest.getMoney();
            if (money > 0) {
                sellChest.setMoney(0);
                HookManager hookManager = HookManager.getInstance();
                VaultHook vaultHook = hookManager.getHookWithExact(VaultHook.class);
                vaultHook.giveMoney(player, money);

                String message = langFile.getMoneyDeposited()
                    .replace("%money%", LangUtils.formatNumber(money))
                    .replace("%tax%", "0");
                ChatUtils.sendMessage(player, message);
            }
            this.openAsync(player);
        });

        actions.put("properties", event -> {
            // todo
        });

        actions.put("start-stop", event -> {
            if (sellChest.getStatus() == ChestStatus.STOPPED) {
                sellChest.setStatus(ChestStatus.WAITING);
            } else {
                sellChest.setStatus(ChestStatus.STOPPED);
            }
            sellChest.updateHologram();
            this.openAsync(player);
        });

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


