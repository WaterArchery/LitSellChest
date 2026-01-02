package me.waterarchery.litsellchest.managers;

import com.chickennw.utils.configurations.HooksFile;
import com.chickennw.utils.libs.cmd.core.suggestion.SuggestionKey;
import com.chickennw.utils.managers.CommandManager;
import com.chickennw.utils.managers.ConfigManager;
import com.chickennw.utils.models.metrics.PluginMetrics;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.commands.SellChestCommand;
import me.waterarchery.litsellchest.configuration.config.*;
import me.waterarchery.litsellchest.database.SellChestDatabase;
import me.waterarchery.litsellchest.listeners.ChestBreakListener;
import me.waterarchery.litsellchest.listeners.ChestPlaceListener;
import me.waterarchery.litsellchest.listeners.ChestProtectListeners;
import me.waterarchery.litsellchest.models.SellChest;
import org.bukkit.Bukkit;

import java.util.List;

public class LoadManager {

    private static LoadManager instance;

    public static LoadManager getInstance() {
        if (instance == null) instance = new LoadManager();

        return instance;
    }

    private LoadManager() {
        loadConfigs();
        ChestManager.getInstance();

        registerEvents();
        registerCommands();
        registerMetrics();
        createDatabase();
    }

    private void loadConfigs() {
        ConfigManager configManager = ConfigManager.getInstance();
        configManager.createFiles("menu");
        configManager.loadOkaeriConfig(ConfigFile.class);
        configManager.loadOkaeriConfig(LangFile.class);
        configManager.loadOkaeriConfig(SoundsFile.class);
        configManager.loadOkaeriConfig(ChestsFile.class);
        configManager.loadOkaeriConfig(HooksFile.class);
    }

    private void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new ChestPlaceListener(), LitSellChest.getInstance());
        Bukkit.getServer().getPluginManager().registerEvents(new ChestBreakListener(), LitSellChest.getInstance());
        Bukkit.getServer().getPluginManager().registerEvents(new ChestProtectListeners(), LitSellChest.getInstance());
    }

    private void registerCommands() {
        CommandManager commandManager = CommandManager.getInstance();
        commandManager.registerSuggestion(SuggestionKey.of("chest-types"), (sender) -> ChestManager.getInstance().getChestTypesAsString());
        commandManager.registerSuggestion(SuggestionKey.of("amount"), (sender) -> List.of("amount"));

        commandManager.registerCommand(new SellChestCommand());
    }

    private void registerMetrics() {
        new PluginMetrics(LitSellChest.getInstance(), 22215);
    }

    public void createDatabase() {
        SellChestDatabase database = SellChestDatabase.getInstance();
        List<SellChest> sellChests = database.loadSellChests();
        ChestManager chestManager = ChestManager.getInstance();
        sellChests.forEach(sellChest -> chestManager.getLoadedChests().put(sellChest.getUuid(), sellChest));
    }
}
