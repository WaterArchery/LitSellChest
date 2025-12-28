package me.waterarchery.litsellchest.configuration.config;

import com.chickennw.utils.libs.config.configs.OkaeriConfig;
import com.chickennw.utils.libs.config.configs.annotation.NameModifier;
import com.chickennw.utils.libs.config.configs.annotation.NameStrategy;
import com.chickennw.utils.libs.config.configs.annotation.Names;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class SoundsFile extends OkaeriConfig {

    private String shopMenuOpened = "ENTITY_PLAYER_LEVELUP";
    private String defaultMenuOpened = "ENTITY_PLAYER_LEVELUP";
    private String chestReceive = "ENTITY_PLAYER_LEVELUP";
    private String chestPlace = "ENTITY_PLAYER_LEVELUP";
    private String chestsTooNear = "ENTITY_SPLASH_POTION_BREAK";
    private String noPermission = "ENTITY_SPLASH_POTION_BREAK";
    private String invalidCommand = "ENTITY_SPLASH_POTION_BREAK";
    private String needToNear = "ENTITY_SPLASH_POTION_BREAK";
    private String limitExceedSound = "ENTITY_SPLASH_POTION_BREAK";
    private String sellSoundToPlayer = "ENTITY_PLAYER_LEVELUP";
    private String yourChestsOpened = "ENTITY_PLAYER_LEVELUP";
    private String inventoryOpened = "ENTITY_PLAYER_LEVELUP";
}
