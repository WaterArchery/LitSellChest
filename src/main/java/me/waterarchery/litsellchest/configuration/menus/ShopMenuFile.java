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
public class ShopMenuFile extends OkaeriConfig implements TriumphMenuConfiguration {

    private List<String> pattern = List.of(
        "---------",
        "--1-2-3--",
        "---------"
    );

    private String openSound = "ENTITY_PLAYER_LEVELUP";

    private String title = "<dark_gray>SellChest - Shop";

    private MenuFiller filler = new MenuFiller();

    private SymboledMenuItem templateItem = null;

    private LinkedHashMap<String, SymboledMenuItem> items = MapUtils.orderedMapOf(
        Map.entry("chest1", new SymboledMenuItem(
            "%name%",
            "CHEST",
            -1,
            List.of(
                "",
                "<#47D4FF><bold>INFO",
                "<#CCFFEE>You can place your items",
                "<#CCFFEE>inside this chest to sell them.",
                "<#CCFFEE>",
                "<#47D4FF><bold>PROPERTIES",
                "<#CCFFEE>Tax: <#47D4FF>%tax%%",
                "<#CCFFEE>Sell Multiplier: <#47D4FF>%sellMultiplier%",
                "<#CCFFEE>Interval: <#47D4FF>%sellInterval%s",
                "<#CCFFEE>",
                "<#47D4FF><bold>BUY NOW",
                "<#CCFFEE>Price: <#47D4FF>%price%",
                "<#CCFFEE>Chest Place Limit: <#47D4FF>%playerLimit%/%playerCount%",
                "<#CCFFEE>Click here to <#47D4FF>buy now!"
            ),
            "chest1",
            '1'
        )),
        Map.entry("chest2", new SymboledMenuItem(
            "%name%",
            "CHEST",
            -1,
            List.of(
                "",
                "<#47D4FF><bold>INFO",
                "<#CCFFEE>You can place your items",
                "<#CCFFEE>inside this chest to sell them.",
                "<#CCFFEE>",
                "<#47D4FF><bold>PROPERTIES",
                "<#CCFFEE>Tax: <#47D4FF>%tax%%",
                "<#CCFFEE>Sell Multiplier: <#47D4FF>%sellMultiplier%",
                "<#CCFFEE>Interval: <#47D4FF>%sellInterval%s",
                "<#CCFFEE>",
                "<#47D4FF><bold>BUY NOW",
                "<#CCFFEE>Price: <#47D4FF>%price%",
                "<#CCFFEE>Chest Place Limit: <#47D4FF>%playerLimit%/%playerCount%",
                "<#CCFFEE>Click here to <#47D4FF>buy now!"
            ),
            "chest2",
            '2'
        )),
        Map.entry("chest3", new SymboledMenuItem(
            "%name%",
            "CHEST",
            -1,
            List.of(
                "",
                "<#47D4FF><bold>INFO",
                "<#CCFFEE>You can place your items",
                "<#CCFFEE>inside this chest to sell them.",
                "<#CCFFEE>",
                "<#47D4FF><bold>PROPERTIES",
                "<#CCFFEE>Tax: <#47D4FF>%tax%%",
                "<#CCFFEE>Sell Multiplier: <#47D4FF>%sellMultiplier%",
                "<#CCFFEE>Interval: <#47D4FF>%sellInterval%s",
                "<#CCFFEE>",
                "<#47D4FF><bold>BUY NOW",
                "<#CCFFEE>Price: <#47D4FF>%price%",
                "<#CCFFEE>Chest Place Limit: <#47D4FF>%playerLimit%/%playerCount%",
                "<#CCFFEE>Click here to <#47D4FF>buy now!"
            ),
            "chest3",
            '3'
        ))
    );
}

