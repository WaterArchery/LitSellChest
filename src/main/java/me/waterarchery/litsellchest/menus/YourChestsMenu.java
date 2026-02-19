package me.waterarchery.litsellchest.menus;

import com.chickennw.utils.libs.annotations.NotNull;
import com.chickennw.utils.libs.gui.builder.item.ItemBuilder;
import com.chickennw.utils.libs.gui.components.GuiAction;
import com.chickennw.utils.libs.gui.guis.GuiItem;
import com.chickennw.utils.libs.gui.guis.PaginatedGui;
import com.chickennw.utils.models.menus.MenuType;
import com.chickennw.utils.models.menus.TriumphMenuWrapper;
import com.chickennw.utils.utils.ChatUtils;
import com.chickennw.utils.utils.ConfigUtils;
import me.waterarchery.litsellchest.configuration.menus.YourChestsMenuFile;
import me.waterarchery.litsellchest.managers.ChestManager;
import me.waterarchery.litsellchest.models.SellChest;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class YourChestsMenu extends TriumphMenuWrapper {

    private final Player player;

    public YourChestsMenu(Player player) {
        super(ConfigUtils.get(YourChestsMenuFile.class), MenuType.PAGINATED);
        this.player = player;
    }

    @Override
    public void createItems(OfflinePlayer offlinePlayer) {
        super.createItems(offlinePlayer);

        ChestManager chestManager = ChestManager.getInstance();
        List<SellChest> chests = chestManager.getPlayerChests(player);
        YourChestsMenuFile menuFile = ConfigUtils.get(YourChestsMenuFile.class);

        for (SellChest chest : chests) {
            ItemStack itemStack = new ItemStack(Material.valueOf(menuFile.getTemplateItem().getMaterial()));
            ItemMeta itemMeta = itemStack.getItemMeta();

            String name = menuFile.getTemplateItem().getName();
            name = name.replace("%player-name%", player.getName());
            itemMeta.setDisplayName(ChatUtils.colorizeLegacy(name));

            List<String> lore = new ArrayList<>();
            for (String part : menuFile.getTemplateItem().getLore()) {
                part = part.replace("%world%", chest.getLocation().getWorld().getName());
                part = part.replace("%x%", chest.getLocation().getBlockX() + "");
                part = part.replace("%y%", chest.getLocation().getBlockY() + "");
                part = part.replace("%z%", chest.getLocation().getBlockZ() + "");
                part = part.replace("%loaded%", chest.isLoaded() + "");
                lore.add(ChatUtils.colorizeLegacy(part));
            }
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            GuiItem guiItem = ItemBuilder.from(itemStack).asGuiItem(event -> {
                chestManager.openChestInventoryOpen(chest, player);
            });

            ((PaginatedGui) cachedGui).addItem(guiItem);
        }
    }

    @Override
    public Function<String, String> placeholderParser() {
        return (text) -> text.replace("%player%", player.getName());
    }

    @Override
    public Runnable nextPageRunnable() {
        return this::update;
    }

    @Override
    public Runnable previousPageRunnable() {
        return this::update;
    }

    @Override
    public HashMap<String, GuiAction<InventoryClickEvent>> premadeGuiActions() {
        return new HashMap<>();
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
