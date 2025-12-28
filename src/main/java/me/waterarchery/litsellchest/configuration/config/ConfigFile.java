package me.waterarchery.litsellchest.configuration.config;

import com.chickennw.utils.libs.config.configs.OkaeriConfig;
import com.chickennw.utils.libs.config.configs.annotation.Comment;
import com.chickennw.utils.libs.config.configs.annotation.NameModifier;
import com.chickennw.utils.libs.config.configs.annotation.NameStrategy;
import com.chickennw.utils.libs.config.configs.annotation.Names;
import com.chickennw.utils.models.config.database.DatabaseConfiguration;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class ConfigFile extends OkaeriConfig {

    private String prefix = "<#47D4FF><bold>ʟɪᴛsᴇʟʟᴄʜᴇsᴛ <reset><dark_gray>»<reset> ";

    @Comment("Do not enable it unless you are told to do")
    private boolean debugMessages = false;

    @Comment({"For example if a group named 'VIP' has permission 'litsellchest.placevip'",
            "they can place 10 sell chests"})
    private List<String> placeLimits = List.of(
            "litsellchest.placedefault:5",
            "litsellchest.placevip:10",
            "litsellchest.placemvip:12"
    );

    private boolean disableChestSellMessage = false;
    private boolean notSellingNotification = true;

    @Comment("It will check all chests in every 2 seconds")
    private int defaultCheckInterval = 2;

    @Comment({"If you make this to true, sell chests only works for online players",
            "If you make this to false, sell chests work even player is offline"})
    private boolean onlyWorkOnlinePlayers = true;

    private DatabaseConfiguration database = new DatabaseConfiguration();
}
