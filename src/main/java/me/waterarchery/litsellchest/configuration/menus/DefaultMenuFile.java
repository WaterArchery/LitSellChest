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
public class DefaultMenuFile extends OkaeriConfig implements TriumphMenuConfiguration {

    private List<String> pattern = List.of(
        "---------",
        "---s-c---",
        "---------"
    );

    private String openSound = "ENTITY_PLAYER_LEVELUP";

    private String title = "<dark_gray>SellChest - Main Menu";

    private MenuFiller filler = new MenuFiller();

    private SymboledMenuItem templateItem = null;

    private LinkedHashMap<String, SymboledMenuItem> items = MapUtils.orderedMapOf(
        Map.entry("open-shop", new SymboledMenuItem(
            "<#47D4FF>Open Shop",
            "CHEST",
            -1,
            List.of(
                "",
                "<#CCFFEE>You can click here to",
                "<#CCFFEE>open <#47D4FF>Chest Shop."
            ),
            "open-shop",
            's'
        )),
        Map.entry("your-chests", new SymboledMenuItem(
            "<#47D4FF>Your Sell Chests",
            "ENDER_CHEST",
            -1,
            List.of(
                "",
                "<#CCFFEE>You can click here to",
                "<#CCFFEE>open <#47D4FF>your Sell Chests."
            ),
            "your-chests",
            'c'
        ))
    );
}

