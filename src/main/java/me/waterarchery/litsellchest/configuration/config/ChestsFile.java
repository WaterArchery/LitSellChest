package me.waterarchery.litsellchest.configuration.config;

import com.chickennw.utils.libs.config.configs.OkaeriConfig;
import com.chickennw.utils.libs.config.configs.annotation.NameModifier;
import com.chickennw.utils.libs.config.configs.annotation.NameStrategy;
import com.chickennw.utils.libs.config.configs.annotation.Names;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Names(strategy = NameStrategy.IDENTITY, modifier = NameModifier.TO_LOWER_CASE)
public class ChestsFile extends OkaeriConfig {

    private Map<String, ChestConfig> chests = createDefaultChests();

    private static Map<String, ChestConfig> createDefaultChests() {
        Map<String, ChestConfig> defaults = new HashMap<>();

        ChestConfig chest1 = new ChestConfig();
        chest1.setName("<#47D4FF>Sell Chest #1");
        chest1.setPrice(1000);
        chest1.setTax(0.1);
        chest1.setSellMultiplier(1.0);
        chest1.setSellInterval(60);
        chest1.setCollectRadius(5);
        chest1.setItem(createDefaultItem());
        defaults.put("chest1", chest1);

        ChestConfig chest2 = new ChestConfig();
        chest2.setName("<#47D4FF>Sell Chest #2");
        chest2.setPrice(5000);
        chest2.setTax(0.05);
        chest2.setSellMultiplier(1.05);
        chest2.setSellInterval(30);
        chest2.setCollectRadius(10);
        chest2.setItem(createDefaultItem());
        defaults.put("chest2", chest2);

        ChestConfig chest3 = new ChestConfig();
        chest3.setName("<#47D4FF>Sell Chest #3");
        chest3.setPrice(10000);
        chest3.setTax(0);
        chest3.setSellMultiplier(1.1);
        chest3.setSellInterval(30);
        chest3.setCollectRadius(15);
        chest3.setItem(createDefaultItem());
        defaults.put("chest3", chest3);

        return defaults;
    }

    private static ItemConfig createDefaultItem() {
        ItemConfig item = new ItemConfig();
        item.setName("%name%");
        item.setMaterial(Material.CHEST);
        item.setCustomModelData(-1);
        item.setLore(List.of(
            "<#CCFFEE>",
            "<#47D4FF><bold>INFO",
            "<#CCFFEE>You can place your items",
            "<#CCFFEE>inside this chest to sell them.",
            "<#CCFFEE>",
            "<#47D4FF><bold>PROPERTIES",
            "<#CCFFEE>Tax: <#47D4FF>%tax%%",
            "<#CCFFEE>Sell Multiplier: <#47D4FF>%sellMultiplier%",
            "<#CCFFEE>Interval: <#47D4FF>%sellInterval%s"
        ));
        return item;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ChestConfig extends OkaeriConfig {
        private String name;
        private double price;
        private double tax;
        private double sellMultiplier;
        private int sellInterval;
        private int collectRadius;
        private ItemConfig item = new ItemConfig();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ItemConfig extends OkaeriConfig {
        private String name = "%name%";
        private Material material = Material.CHEST;
        private int customModelData = -1;
        private List<String> lore = List.of();
    }
}

