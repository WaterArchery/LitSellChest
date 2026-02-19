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
public class YourChestsMenuFile extends OkaeriConfig implements TriumphMenuConfiguration {

    private List<String> pattern = List.of(
        "---------",
        "--iiiii--",
        "--iiiii--",
        "--n---p--"
    );

    private String openSound = "BLOCK_NOTE_BLOCK_PLING";

    private String title = "<dark_gray>SellChest - Your Chests";

    private MenuFiller filler = new MenuFiller();

    private SymboledMenuItem templateItem = new SymboledMenuItem(
        "%player-name%",
        "CHEST",
        -1,
        List.of(
            "",
            "<#47D4FF><bold>INFO",
            " <#CCFFEE>This is your personal",
            " <#47D4FF>SellChest. <#CCFFEE>You can",
            " <#CCFFEE>store items here to",
            " <#CCFFEE>sell them <#47D4FF>automatically.",
            "",
            "<#47D4FF><bold>LOCATION",
            " <#CCFFEE>World: <#47D4FF>%world%",
            " <#CCFFEE>X: <#47D4FF>%x%",
            " <#CCFFEE>Y: <#47D4FF>%y%",
            " <#CCFFEE>Z: <#47D4FF>%z%",
            " <#CCFFEE>Is Loaded: <#47D4FF>%loaded%",
            "",
            "<#CCFFEE>Click here to <#47D4FF>open <#CCFFEE>this",
            "<#47D4FF>chest's inventory."
        ),
        "template",
        'i'
    );

    private LinkedHashMap<String, SymboledMenuItem> items = MapUtils.orderedMapOf(
        Map.entry("previous-page", new SymboledMenuItem(
            "<#47D4FF>Previous Page",
            "ARROW",
            -1,
            List.of(
                "",
                "<#CCFFEE>Click here to navigate",
                "<#47D4FF>previous page."
            ),
            "previous-page",
            'p'
        )),
        Map.entry("next-page", new SymboledMenuItem(
            "<#47D4FF>Next Page",
            "ARROW",
            -1,
            List.of(
                "",
                "<#CCFFEE>Click here to navigate",
                "<#47D4FF>next page."
            ),
            "next-page",
            'n'
        ))
    );
}

