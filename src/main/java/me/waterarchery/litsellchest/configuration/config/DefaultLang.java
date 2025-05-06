package me.waterarchery.litsellchest.configuration.config;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litlibs.configuration.ConfigPart;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DefaultLang extends ConfigManager {

    public DefaultLang(LitLibs litLibs, String folder, String name, boolean saveAfterLoad) {
        super(litLibs, folder, name, saveAfterLoad);
    }

    @Override
    public void initializeDefaults() {
        ArrayList<String> chestHologramLines = getHologramLines();
        addDefault(ConfigPart.noComment("ChestHologram.height", 3.5f));
        addDefault(new ConfigPart("ChestHologram.lines", chestHologramLines,
                Arrays.asList("You can define all the lines of",
                        "the Sell Chest Hologram.", "",
                        "Usable Placeholders:",
                        "%tax% -> replaces with tax",
                        "%name% -> replaces with Sell Chest name",
                        "%sellMultiplier% -> replaces with sell multiplier",
                        "%status% -> replaces with Sell Chest status",
                        "%sellInterval% -> replaces with selling interval",
                        "%remainingTime% -> replaces with remaining time")));
        addDefault(new ConfigPart("TooManyArgs", "<#CCFFEE>You entered <#47D4FF>too many arguments <#CCFFEE>for this command.",
                Collections.singletonList("This is called when command args exceed the maximum")));
        addDefault(new ConfigPart("TooFewArgs", "<#CCFFEE>You entered <#47D4FF>too few arguments <#CCFFEE>for this command.",
                Collections.singletonList("This is called when command args are too few for the command")));
        addDefault(new ConfigPart("InvalidArg",
                "<#CCFFEE>You entered a <#47D4FF>invalid argument <#CCFFEE>for this command. You entered <#47D4FF>%used% <#CCFFEE>but correct one is <#47D4FF>%correct%!",
                Collections.singletonList("This is called when on of the command arg is invalid")));
        addDefault(new ConfigPart("UnknownCommand", "<#CCFFEE>You entered a <#47D4FF>unknown command <#CCFFEE>for this command.",
                Collections.singletonList("This is called when on of the sub command arg is unknown")));
        addDefault(ConfigPart.noComment("NoPermission", "<#CCFFEE>You don't <#47D4FF>have permission <#CCFFEE>to execute this command."));
        addDefault(ConfigPart.noComment("NoChestWithType", "<#CCFFEE>There is <#47D4FF>no chest type <#CCFFEE>with this name."));
        addDefault(ConfigPart.noComment("InvalidPriceOrFree","<#CCFFEE>There are items in the chest that are not sellable or have a 0 price"));
        addDefault(ConfigPart.noComment("ChestGaveTarget", "<#CCFFEE>You received a <#47D4FF>%name%!"));
        addDefault(ConfigPart.noComment("ChestGaveAdmin", "<#CCFFEE>You gave <#47D4FF>%name% <#CCFFEE>to <#47D4FF>%player% <#CCFFEE>successfully."));
        addDefault(ConfigPart.noComment("ChestPlaced", "<#CCFFEE>You successfully placed your <#47D4FF>sell chest. <#CCFFEE>It will start selling soon!"));
        addDefault(ConfigPart.noComment("ChestsTooNear", "<#CCFFEE>You can't place a <#47D4FF>Sell Chest <#CCFFEE>here!"));
        addDefault(ConfigPart.noComment("NotEnoughMoney", "<#CCFFEE>You don't have <#47D4FF>enough money <#CCFFEE>to buy this chest!"));
        addDefault(ConfigPart.noComment("ChestBought", "<#CCFFEE>You successfully bought <#47D4FF>a SellChest! <#CCFFEE>Your new balance <#47D4FF>%money%$!"));
        addDefault(ConfigPart.noComment("BrokeChest", "<#CCFFEE>You successfully <#47D4FF>broke your Sell Chest"));
        addDefault(ConfigPart.noComment("NeedToNear", "<#CCFFEE>You need to be near to your <#47D4FF>Sell Chest <#CCFFEE>to do this!"));
        addDefault(ConfigPart.noComment("LimitReached",
                "<#CCFFEE>Your Sell Chest limit is exceed <#CCFFEE>(<red>%current%/%max%<#CCFFEE>). You need to <#47D4FF>rank up <#CCFFEE>in order to place more sell chests."));
        addDefault(ConfigPart.noComment("MoneyDeposited", "<#CCFFEE>Your items are <#47D4FF>sold <#CCFFEE>and you received <#47D4FF>%money%$! You also paid %tax%$ tax!"));
        addDefault(ConfigPart.noComment("PluginReloaded", "<green>Plugin reloaded successfully!"));
        addDefault(ConfigPart.noComment("Status.waiting", "<#CCFFEE>Waiting to sell!"));
        addDefault(ConfigPart.noComment("Status.selling", "<green>Selling now!"));
        addDefault(ConfigPart.noComment("Status.stopped", "<red>Stopped!"));
    }

    @NotNull
    private static ArrayList<String> getHologramLines() {
        ArrayList<String> chestHologramLines = new ArrayList<>();
        chestHologramLines.add("%name%");
        chestHologramLines.add("");
        chestHologramLines.add("<white>Sell Multiplier: <#47D4FF>%sellMultiplier%");
        chestHologramLines.add("<white>Tax: <#47D4FF>%tax%");
        chestHologramLines.add("<white>Status: <#47D4FF>%status%");
        chestHologramLines.add("<white>Sell Interval: <#47D4FF>%sellInterval%");
        chestHologramLines.add("");
        chestHologramLines.add("<#CCFFEE>Wait for selling action");
        chestHologramLines.add("<#CCFFEE>Remaining time: <#47D4FF>%remainingTime%");
        return chestHologramLines;
    }

}
