package me.waterarchery.litsellchest.configuration.menus;

import com.chickennw.utils.configurations.menu.MenuFiller;
import com.chickennw.utils.configurations.menu.SymboledMenuItem;
import com.chickennw.utils.libs.config.configs.OkaeriConfig;
import com.chickennw.utils.models.config.menu.TriumphMenuConfiguration;
import lombok.Getter;
import lombok.Setter;
import me.waterarchery.litsellchest.utils.MapUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ChestMenuFile extends OkaeriConfig implements TriumphMenuConfiguration {

    private List<String> pattern = List.of(
        "---------",
        "-o-c-p-s-",
        "---------"
    );

    private String openSound = "ENTITY_PLAYER_LEVELUP";

    private String title = "<dark_gray>SellChest - Chest Menu";

    private MenuFiller filler = new MenuFiller();

    private SymboledMenuItem templateItem = null;

    private LinkedHashMap<String, SymboledMenuItem> items = MapUtils.orderedMapOf(
        Map.entry("open-chest-inventory", new SymboledMenuItem(
            "<#47D4FF>Open Inventory",
            "CHEST",
            -1,
            List.of(
                "",
                "<#CCFFEE>You can click here to",
                "<#CCFFEE>access <#47D4FF>chest Inventory."
            ),
            "open-chest-inventory",
            'o'
        )),
        Map.entry("collect-money", new SymboledMenuItem(
            "<#47D4FF>Collect Money",
            "GOLD_INGOT",
            -1,
            List.of(
                "",
                "<#CCFFEE>You can click here to",
                "<#CCFFEE>collect <#47D4FF>the money.",
                "",
                "<#CCFFEE>Waiting to collect: <#47D4FF>%money%"
            ),
            "collect-money",
            'c'
        )),
        Map.entry("properties", new SymboledMenuItem(
            "<#47D4FF>Properties",
            "BOOK",
            -1,
            List.of(
                "",
                "<#CCFFEE>You can click here to",
                "<#CCFFEE>view <#47D4FF>Sell Chest properties.",
                "",
                "<#47D4FF><bold>INFO",
                "<#CCFFEE>Tax: <#47D4FF>%tax%%",
                "<#CCFFEE>Sell Multiplier: <#47D4FF>%sellMultiplier%",
                "<#CCFFEE>Interval: <#47D4FF>%sellInterval%s",
                "<#CCFFEE>Status: <#47D4FF>%status%"
            ),
            "properties",
            'p'
        )),
        Map.entry("start-stop", new SymboledMenuItem(
            "<#47D4FF>Start/Stop Selling",
            "LEVER",
            -1,
            List.of(
                "",
                "<#CCFFEE>You can click here to",
                "<#CCFFEE>start or stop <#47D4FF>selling.",
                "",
                "<#CCFFEE>Current Status: <#47D4FF>%status%"
            ),
            "start-stop",
            's'
        ))
    );
}

