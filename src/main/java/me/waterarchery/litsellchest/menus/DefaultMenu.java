package me.waterarchery.litsellchest.menus;

import com.chickennw.utils.libs.annotations.NotNull;
import com.chickennw.utils.libs.gui.components.GuiAction;
import com.chickennw.utils.models.menus.MenuType;
import com.chickennw.utils.models.menus.TriumphMenuWrapper;
import com.chickennw.utils.utils.ConfigUtils;
import me.waterarchery.litsellchest.configuration.menus.DefaultMenuFile;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.function.Function;

public class DefaultMenu extends TriumphMenuWrapper {

    private final Player player;

    public DefaultMenu(Player player) {
        super(ConfigUtils.get(DefaultMenuFile.class), MenuType.SIMPLE);
        this.player = player;
    }

    @Override
    public Function<String, String> placeholderParser() {
        return (text) -> text;
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

        actions.put("open-shop", event -> {
            ShopMenu shopMenu = new ShopMenu(player);
            shopMenu.openAsync(player);
        });

        actions.put("your-chests", event -> {
            YourChestsMenu yourChestsMenu = new YourChestsMenu(player);
            yourChestsMenu.openAsync(player);
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
