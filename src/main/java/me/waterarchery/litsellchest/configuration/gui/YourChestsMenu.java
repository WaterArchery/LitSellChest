package me.waterarchery.litsellchest.configuration.gui;

import com.chickennw.utils.libs.themoep.inventorygui.GuiElement;
import com.chickennw.utils.libs.themoep.inventorygui.InventoryGui;
import com.chickennw.utils.models.menus.LitMenuItemHolder;
import com.chickennw.utils.models.menus.LitPaginatedMenu;
import com.chickennw.utils.utils.ChatUtils;
import me.waterarchery.litsellchest.managers.ChestManager;
import me.waterarchery.litsellchest.models.SellChest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YourChestsMenu extends LitPaginatedMenu {

    public YourChestsMenu(Player player) {
        super("your_chests", player);
    }

    @Override
    public HashMap<String, GuiElement.Action> getGuiActions() {
        return new HashMap<>();
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

    @Override
    public List<LitMenuItemHolder> getTemplateItems() {
        List<LitMenuItemHolder> items = new ArrayList<>();
        ChestManager chestManager = ChestManager.getInstance();
        List<SellChest> chests = chestManager.getPlayerChests(player);

        for (SellChest chest : chests) {
            LitMenuItemHolder itemHolder = createItem(player.getUniqueId(), "items.template-item");
            ItemStack itemStack = getItemStack(itemHolder, chest);
            itemHolder.setItemStack(itemStack);
            items.add(itemHolder);

            itemHolder.getItem().setAction(click -> {
                chestManager.openChestInventoryOpen(chest, player);
                return true;
            });
        }

        return items;
    }

    private ItemStack getItemStack(LitMenuItemHolder itemHolder, SellChest chest) {
        ItemStack itemStack = itemHolder.getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<String> oldLore = itemMeta.getLore();
        List<String> lore = new ArrayList<>();
        oldLore.forEach(part -> {
            part = ChatUtils.colorizeLegacy(part);
            part = part.replace("%world%", chest.getLocation().getWorld().getName());
            part = part.replace("%x%", chest.getLocation().getBlockX() + "");
            part = part.replace("%y%", chest.getLocation().getBlockY() + "");
            part = part.replace("%z%", chest.getLocation().getBlockZ() + "");
            part = part.replace("%loaded%", chest.isLoaded() + "");

            lore.add(part);
        });
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
