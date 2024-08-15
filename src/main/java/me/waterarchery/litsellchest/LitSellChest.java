package me.waterarchery.litsellchest;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.LitLibsPlugin;
import me.waterarchery.litsellchest.database.SQLiteDatabase;
import me.waterarchery.litsellchest.handlers.ChestHandler;
import me.waterarchery.litsellchest.handlers.CommandHandler;
import me.waterarchery.litsellchest.handlers.ConfigHandler;
import me.waterarchery.litsellchest.handlers.GUIHandler;
import me.waterarchery.litsellchest.hooks.VaultHook;
import me.waterarchery.litsellchest.listeners.*;
import me.waterarchery.litsellchest.models.SellChest;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;

public final class LitSellChest extends JavaPlugin {

    private static LitLibs libs;
    private static SQLiteDatabase database;
    private static LitSellChest instance;
    private BukkitCommandManager<CommandSender> manager;

    @Override
    public void onEnable() {
        instance = this;
        libs = LitLibs.of(getInstance());
        ConfigHandler.getInstance();
        ChestHandler.getInstance();
        GUIHandler.getInstance();
        createDatabase();
        registerCommands();
        registerEvents();
        registerHooks();
        libs.reload();
    }

    @Override
    public void onDisable() {
        CommandHandler commandHandler = CommandHandler.getInstance();
        commandHandler.unRegisterCommands(manager);
        for (SellChest sellChest : ChestHandler.getInstance().getLoadedChests()) {
            sellChest.deleteHologram();
        }

        if (database != null) {
            database.shutdownPool();
            try {
                database.getSQLConnection().close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        libs.getLogger().log("Good bye :(");
    }

    public void createDatabase() {
        database = new SQLiteDatabase(libs);
        long startTime = Date.from(Instant.now()).getTime();
        int total = database.loadChests();
        long finishTime = Date.from(Instant.now()).getTime();
        libs.getLogger().log("Loaded §c" + total + " §fchests in §c" + (finishTime - startTime) + "ms");
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new ChestPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new MainMenuListener(), this);
        getServer().getPluginManager().registerEvents(new ShopMenuListener(), this);
        getServer().getPluginManager().registerEvents(new YourChestsMenuListener(), this);
        getServer().getPluginManager().registerEvents(new ChestBreakListener(), this);
        getServer().getPluginManager().registerEvents(new ChestProtectListeners(), this);
    }

    private void registerCommands() {
        CommandHandler commandHandler = CommandHandler.getInstance();
        manager = BukkitCommandManager.create(this);
        commandHandler.registerSuggestions(manager);
        commandHandler.registerCommands(manager);
        commandHandler.registerMessages(manager);
    }

    private void registerHooks() {
        VaultHook vaultHook = VaultHook.getInstance();
        if (!vaultHook.setupEconomy()) {
            libs.getLogger().error("Disabled due to no Vault dependency found! Install a economy plugin!");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        }

        new Metrics(LitLibsPlugin.getInstance(), 22215);
        libs.getHookHandler();
    }

    public LitLibs getLibs() { return libs; }

    public static LitSellChest getInstance() { return instance; }

    public BukkitCommandManager<CommandSender> getManager() { return manager; }

    public static SQLiteDatabase getDatabase() { return database; }

}
