package me.waterarchery.litsellchest.configuration.config;

import com.chickennw.utils.libs.config.configs.OkaeriConfig;
import com.chickennw.utils.libs.config.configs.annotation.Comment;
import com.chickennw.utils.libs.config.configs.annotation.NameModifier;
import com.chickennw.utils.libs.config.configs.annotation.NameStrategy;
import com.chickennw.utils.libs.config.configs.annotation.Names;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class LangFile extends OkaeriConfig {

    private ChestHologram chestHologram = new ChestHologram();

    @Comment("This is called when command args exceed the maximum")
    private String tooManyArgs = "<#CCFFEE>You entered <#47D4FF>too many arguments <#CCFFEE>for this command.";

    @Comment("This is called when command args are too few for the command")
    private String tooFewArgs = "<#CCFFEE>You entered <#47D4FF>too few arguments <#CCFFEE>for this command.";

    @Comment("This is called when on of the command arg is invalid")
    private String invalidArg = "<#CCFFEE>You entered a <#47D4FF>invalid argument <#CCFFEE>for this command. You entered <#47D4FF>%used% <#CCFFEE>but correct one is <#47D4FF>%correct%!";

    @Comment("This is called when on of the sub command arg is unknown")
    private String unknownCommand = "<#CCFFEE>You entered a <#47D4FF>unknown command <#CCFFEE>for this command.";

    private String noPermission = "<#CCFFEE>You don't <#47D4FF>have permission <#CCFFEE>to execute this command.";
    private String noChestWithType = "<#CCFFEE>There is <#47D4FF>no chest type <#CCFFEE>with this name.";
    private String invalidPriceOrFree = "<#CCFFEE>There are items in the chest that are not sellable or have a 0 price";
    private String chestGaveTarget = "<#CCFFEE>You received a <#47D4FF>%name%!";
    private String chestGaveAdmin = "<#CCFFEE>You gave <#47D4FF>%name% <#CCFFEE>to <#47D4FF>%player% <#CCFFEE>successfully.";
    private String chestPlaced = "<#CCFFEE>You successfully placed your <#47D4FF>sell chest. <#CCFFEE>It will start selling soon!";
    private String chestsTooNear = "<#CCFFEE>You can't place a <#47D4FF>Sell Chest <#CCFFEE>here!";
    private String notEnoughMoney = "<#CCFFEE>You don't have <#47D4FF>enough money <#CCFFEE>to buy this chest!";
    private String chestBought = "<#CCFFEE>You successfully bought <#47D4FF>a SellChest! <#CCFFEE>Your new balance <#47D4FF>%money%$!";
    private String brokeChest = "<#CCFFEE>You successfully <#47D4FF>broke your Sell Chest";
    private String needToNear = "<#CCFFEE>You need to be near to your <#47D4FF>Sell Chest <#CCFFEE>to do this!";
    private String limitReached = "<#CCFFEE>Your Sell Chest limit is exceed <#CCFFEE>(<red>%current%/%max%<#CCFFEE>). You need to <#47D4FF>rank up <#CCFFEE>in order to place more sell chests.";
    private String moneyDeposited = "<#CCFFEE>Your items are <#47D4FF>sold <#CCFFEE>and you received <#47D4FF>%money%$! You also paid %tax%$ tax!";
    private String pluginReloaded = "<green>Plugin reloaded successfully!";

    private Status status = new Status();

    @Getter
    @Setter
    public static class ChestHologram extends OkaeriConfig {
        private float height = 2.3f;

        @Comment({"You can define all the lines of",
                "the Sell Chest Hologram.", "",
                "Usable Placeholders:",
                "%tax% -> replaces with tax",
                "%name% -> replaces with Sell Chest name",
                "%sellMultiplier% -> replaces with sell multiplier",
                "%status% -> replaces with Sell Chest status",
                "%sellInterval% -> replaces with selling interval",
                "%remainingTime% -> replaces with remaining time"})
        private List<String> lines = List.of(
                "%name%",
                "",
                "<white>Sell Multiplier: <#47D4FF>%sellMultiplier%",
                "<white>Tax: <#47D4FF>%tax%",
                "<white>Status: <#47D4FF>%status%",
                "<white>Sell Interval: <#47D4FF>%sellInterval%",
                "",
                "<#CCFFEE>Wait for selling action",
                "<#CCFFEE>Remaining time: <#47D4FF>%remainingTime%"
        );
    }

    @Getter
    @Setter
    public static class Status extends OkaeriConfig {
        private String waiting = "<#CCFFEE>Waiting to sell!";
        private String selling = "<green>Selling now!";
        private String stopped = "<red>Stopped!";
    }
}
