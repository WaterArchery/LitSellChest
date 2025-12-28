package me.waterarchery.litsellchest.models;

import com.chickennw.utils.utils.ChatUtils;
import com.chickennw.utils.utils.NbtUtils;
import lombok.Getter;
import me.waterarchery.litsellchest.configuration.config.ChestsFile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SellChestType {

    private final String id;
    private final String name;
    private final double tax;
    private final double sellMultiplier;
    private final double sellInterval;
    private final double price;
    private final int collectRadius;
    private final ItemStack itemStack;

    public SellChestType(String id, ChestsFile.ChestConfig chestConfig) {
        this.id = id;

        name = chestConfig.getName();
        tax = chestConfig.getTax();
        sellMultiplier = chestConfig.getSellMultiplier();
        sellInterval = chestConfig.getSellInterval();
        price = chestConfig.getPrice();
        collectRadius = chestConfig.getCollectRadius();

        itemStack = createItemStack(chestConfig.getItem());
    }

    private ItemStack createItemStack(ChestsFile.ItemConfig itemConfig) {
        String name = itemConfig.getName().replace("%name%", getName());
        List<String> rawLore = itemConfig.getLore();
        List<String> lore = new ArrayList<>();
        for (String part : rawLore) {
            part = part.replace("%tax%", getTax() + "");
            part = part.replace("%sellMultiplier%", getSellMultiplier() + "");
            part = part.replace("%sellInterval%", getSellInterval() + "");
            part = ChatUtils.colorizeLegacy(part);
            lore.add(part);
        }

        ItemStack itemStack = new ItemStack(itemConfig.getMaterial());
        ItemMeta meta = itemStack.getItemMeta();
        if (itemConfig.getCustomModelData() != -1) meta.setCustomModelData(itemConfig.getCustomModelData());

        meta.setDisplayName(ChatUtils.colorizeLegacy(name));
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        itemStack = NbtUtils.setKey(itemStack, "SellChestType", id);
        return itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }
}
