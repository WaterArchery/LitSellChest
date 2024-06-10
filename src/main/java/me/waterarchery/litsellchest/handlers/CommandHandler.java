package me.waterarchery.litsellchest.handlers;

import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.message.MessageKey;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litlibs.handlers.MessageHandler;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.commands.SellChestCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandHandler {

    private static CommandHandler instance;
    SellChestCommand command;

    public static CommandHandler getInstance() {
        if (instance == null) instance = new CommandHandler();
        return instance;
    }

    private CommandHandler() { }

    public void registerCommands(BukkitCommandManager<CommandSender> manager) {
        command = new SellChestCommand();
        manager.registerCommand(command);
    }

    public void unRegisterCommands(BukkitCommandManager<CommandSender> manager) {
        manager.unregisterCommands(command);
    }

    public void registerSuggestions(BukkitCommandManager<CommandSender> manager) {
        manager.registerSuggestion(SuggestionKey.of("chest-types"), (sender, context) -> ChestHandler.getInstance().getChestTypesAsString());
        manager.registerSuggestion(SuggestionKey.of("amount"), (sender, context) -> List.of("amount"));
    }

    public void registerMessages(BukkitCommandManager<CommandSender> manager) {
        LitLibs libs = LitSellChest.getInstance().getLibs();
        ConfigHandler configHandler = ConfigHandler.getInstance();
        MessageHandler mHandler = libs.getMessageHandler();
        ConfigManager lang = configHandler.getLang();

        manager.registerMessage(MessageKey.NOT_ENOUGH_ARGUMENTS, (sender, context) -> {
            String message = lang.getString("TooFewArgs");
            mHandler.sendMessage(sender, message);
            if (sender instanceof Player) SoundManager.sendSound((Player) sender, "InvalidCommand");
        });

        manager.registerMessage(MessageKey.TOO_MANY_ARGUMENTS, (sender, context) -> {
            String message = lang.getString("TooManyArgs");
            mHandler.sendMessage(sender, message);
            if (sender instanceof Player) SoundManager.sendSound((Player) sender, "InvalidCommand");
        });

        manager.registerMessage(MessageKey.INVALID_ARGUMENT, (sender, context) -> {
            String message = lang.getString("InvalidArg");
            mHandler.sendMessage(sender, message);
            if (sender instanceof Player) SoundManager.sendSound((Player) sender, "InvalidCommand");
        });

        manager.registerMessage(MessageKey.UNKNOWN_COMMAND, (sender, context) -> {
            String message = lang.getString("UnknownCommand");
            mHandler.sendMessage(sender, message);
            if (sender instanceof Player) SoundManager.sendSound((Player) sender, "InvalidCommand");
        });

        manager.registerMessage(BukkitMessageKey.NO_PERMISSION, (sender, context) -> {
            String message = lang.getString("NoPermission");
            mHandler.sendMessage(sender, message);
            if (sender instanceof Player) SoundManager.sendSound((Player) sender, "NoPermission");
        });
    }

}
