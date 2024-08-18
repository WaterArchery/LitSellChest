package me.waterarchery.litsellchest.configuration.config;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litlibs.configuration.ConfigPart;

import java.util.Arrays;
import java.util.Collections;

public class Config extends ConfigManager {

    public Config(LitLibs litLibs, String folder, String name, boolean saveAfterLoad) {
        super(litLibs, folder, name, saveAfterLoad);
    }

    @Override
    public void initializeDefaults() {
        addDefault(new ConfigPart("Prefix", "&8[&bLitSellChest&8] ", Collections.emptyList()));
        addDefault(new ConfigPart("PriceHook", "Essentials", Arrays.asList("Usable values are", "EssentialsX, EconomyShopGUI, Essentials, ShopGUIPlus, AxGens")));
        addDefault(new ConfigPart("DebugMessages", false, Collections.singletonList("Do not enable it unless you are told to do")));
        addDefault(ConfigPart.of("PlaceLimits", Arrays.asList(
                        "litsellchest.placedefault:5",
                        "litsellchest.placevip:10",
                        "litsellchest.placemvip:12",
                        "litsellchest.placevip:10"
                ),
                Arrays.asList(
                        "For example if a group named 'VIP' has permission 'litsellchest.placevip'",
                        "they can place 10 sell chests"
                )));
        addDefault(new ConfigPart("SoundsEnabled", true, Collections.emptyList()));
        addDefault(new ConfigPart("NotSellingNotification", true, Collections.emptyList()));
        addDefault(new ConfigPart("SoundsVolume", 5, Collections.emptyList()));
        addDefault(ConfigPart.of("Sounds", null, Arrays.asList("You can completely disable all sounds above.", "",
                        "Please use only the sounds that in here", "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html")));
        addDefault(new ConfigPart("Sounds.ShopMenuOpened", "ENTITY_PLAYER_LEVELUP", Collections.emptyList()));
        addDefault(new ConfigPart("Sounds.DefaultMenuOpened", "ENTITY_PLAYER_LEVELUP", Collections.emptyList()));
        addDefault(new ConfigPart("Sounds.ChestReceive", "ENTITY_PLAYER_LEVELUP", Collections.emptyList()));
        addDefault(new ConfigPart("Sounds.ChestPlace", "ENTITY_PLAYER_LEVELUP", Collections.emptyList()));
        addDefault(new ConfigPart("Sounds.ChestsTooNear", "ENTITY_SPLASH_POTION_BREAK", Collections.emptyList()));
        addDefault(new ConfigPart("Sounds.NoPermission", "ENTITY_SPLASH_POTION_BREAK", Collections.emptyList()));
        addDefault(new ConfigPart("Sounds.InvalidCommand", "ENTITY_SPLASH_POTION_BREAK", Collections.emptyList()));
        addDefault(new ConfigPart("Sounds.NeedToNear", "ENTITY_SPLASH_POTION_BREAK", Collections.emptyList()));
        addDefault(new ConfigPart("Sounds.LimitExceedSound", "ENTITY_SPLASH_POTION_BREAK", Collections.emptyList()));
        addDefault(new ConfigPart("Sounds.SellSoundToPlayer", "ENTITY_PLAYER_LEVELUP", Collections.emptyList()));
        addDefault(new ConfigPart("Sounds.YourChestsOpened", "ENTITY_PLAYER_LEVELUP", Collections.emptyList()));
        addDefault(new ConfigPart("Sounds.InventoryOpened", "ENTITY_PLAYER_LEVELUP", Collections.emptyList()));
        addDefault(new ConfigPart("DefaultCheckInterval", 2, Collections.singletonList("It will check all chests in every 2 seconds")));
        addDefault(new ConfigPart("OnlyWorkOnlinePlayers", true, Arrays.asList("If you make this to true, sell chests only works for online players", "If you make this to false, sell chests work even player is offline")));
    }

}
