package me.waterarchery.litsellchest.handlers;

import com.google.common.collect.Lists;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.exceptions.CommandRegistrationException;
import dev.triumphteam.cmd.core.message.MessageKey;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litlibs.handlers.MessageHandler;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.commands.SellChestCommand;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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
        if (manager != null) {
            manager.unregisterCommands(command);
            Lists.newArrayList("islandnpc", "isnpc").forEach(this::unregisterCommand);
        }
    }

    // @author efekurbann
    public void unregisterCommand(String name) {
        getBukkitCommands(getCommandMap()).remove(name);
    }

    // @author efekurbann
    @NotNull
    private CommandMap getCommandMap() {
        try {
            final Server server = Bukkit.getServer();
            final Method getCommandMap = server.getClass().getDeclaredMethod("getCommandMap");
            getCommandMap.setAccessible(true);

            return (CommandMap) getCommandMap.invoke(server);
        } catch (final Exception ignored) {
            throw new CommandRegistrationException("Unable get Command Map. Commands will not be registered!");
        }
    }

    // copied from triumph-cmd, credit goes to triumph-team
    @NotNull
    private Map<String, Command> getBukkitCommands(@NotNull final CommandMap commandMap) {
        try {
            final Field bukkitCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            bukkitCommands.setAccessible(true);
            //noinspection unchecked
            return (Map<String, org.bukkit.command.Command>) bukkitCommands.get(commandMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new CommandRegistrationException("Unable get Bukkit commands. Commands might not be registered correctly!");
        }
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
