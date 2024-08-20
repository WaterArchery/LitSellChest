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
        addDefault(new ConfigPart("TooManyArgs", "&7You entered &btoo many arguments &7for this command.",
                Collections.singletonList("This is called when command args exceed the maximum")));
        addDefault(new ConfigPart("TooFewArgs", "&7You entered &btoo few arguments &7for this command.",
                Collections.singletonList("This is called when command args are too few for the command")));
        addDefault(new ConfigPart("InvalidArg",
                "&7You entered a &binvalid argument &7for this command. You entered &e%used% &7but correct one is &e%correct%!",
                Collections.singletonList("This is called when on of the command arg is invalid")));
        addDefault(new ConfigPart("UnknownCommand", "&7You entered a &bunknown command &7for this command.",
                Collections.singletonList("This is called when on of the sub command arg is unknown")));
        addDefault(ConfigPart.noComment("NoPermission", "&7You don't &bhave permission &7to execute this command."));
        addDefault(ConfigPart.noComment("NoChestWithType", "&7There is &bno chest type &7with this name."));
        addDefault(ConfigPart.noComment("InvalidPriceOrFree","&7There are items in the chest that are not sellable or have a 0 price"));
        addDefault(ConfigPart.noComment("ChestGaveTarget", "&7You received a &b%name%!"));
        addDefault(ConfigPart.noComment("ChestGaveAdmin", "&7You gave &b%name% &7to &b%player% &7successfully."));
        addDefault(ConfigPart.noComment("ChestPlaced", "&7You successfully placed your &bsell chest. &7It will start selling soon!"));
        addDefault(ConfigPart.noComment("ChestsTooNear", "&7You can't place a &bSell Chest &7here!"));
        addDefault(ConfigPart.noComment("NotEnoughMoney", "&7You don't have &benough money &7to buy this chest!"));
        addDefault(ConfigPart.noComment("ChestBought", "&7You successfully bought &ba SellChest! &7Your new balance &b%money%$!"));
        addDefault(ConfigPart.noComment("BrokeChest", "&7You successfully &bbroke your Sell Chest"));
        addDefault(ConfigPart.noComment("NeedToNear", "&7You need to be near to your &bSell Chest &7to do this!"));
        addDefault(ConfigPart.noComment("LimitReached",
                "&7Your Sell Chest limit is exceed &7(&c%current%/%max%&7). You need to &brank up &7in order to place more sell chests."));
        addDefault(ConfigPart.noComment("MoneyDeposited", "&7Your items are &bsold &7and you received &b%money%$! You also paid %tax%$ tax!"));
        addDefault(ConfigPart.noComment("PluginReloaded", "&aPlugin reloaded successfully!"));
        addDefault(ConfigPart.noComment("Status.waiting", "&7Waiting to sell!"));
        addDefault(ConfigPart.noComment("Status.selling", "&aSelling now!"));
        addDefault(ConfigPart.noComment("Status.stopped", "&cStopped!"));
    }

    @NotNull
    private static ArrayList<String> getHologramLines() {
        ArrayList<String> chestHologramLines = new ArrayList<>();
        chestHologramLines.add("%name%");
        chestHologramLines.add("");
        chestHologramLines.add("&fSell Multiplier: &b%sellMultiplier%");
        chestHologramLines.add("&fTax: &b%tax%");
        chestHologramLines.add("&fStatus: &b%status%");
        chestHologramLines.add("&fSell Interval: &b%sellInterval%");
        chestHologramLines.add("");
        chestHologramLines.add("&7Wait for selling action");
        chestHologramLines.add("&7Remaining time: &b%remainingTime%");
        return chestHologramLines;
    }

}
