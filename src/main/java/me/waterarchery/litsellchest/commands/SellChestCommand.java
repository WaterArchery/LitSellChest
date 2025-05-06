package me.waterarchery.litsellchest.commands;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.*;
import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.configuration.gui.DefaultMenu;
import me.waterarchery.litsellchest.handlers.ChestHandler;
import me.waterarchery.litsellchest.handlers.ConfigHandler;
import me.waterarchery.litsellchest.handlers.GUIHandler;
import me.waterarchery.litsellchest.handlers.SoundManager;
import me.waterarchery.litsellchest.models.SellChestType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

@Command(value = "litsellchest", alias = {"sellchest", "lsc"})
public class SellChestCommand extends BaseCommand {

    @Default
    public void mainCommand(CommandSender sender) {
        if (sender instanceof Player player) {
            DefaultMenu defaultMenu = new DefaultMenu();

            defaultMenu.openAsync(player);
            SoundManager.sendSound(player, "DefaultMenuOpened");
        }
        else {
            LitLibs libs = LitSellChest.getLibs();
            libs.getLogger().log("You can only use this command on in game.");
        }
    }

    @SubCommand("shop")
    public void shop(CommandSender sender) {
        if (sender instanceof Player) {
            GUIHandler guiHandler = GUIHandler.getInstance();
            guiHandler.openShop((Player) sender);
        }
        else {
            LitLibs libs = LitSellChest.getLibs();
            libs.getLogger().log("You can only use this command on in game.");
        }
    }

    @Permission("litsellchest.admin.give")
    @SubCommand("give")
    public void give(CommandSender sender, Player target,
                            @ArgName("chestType") @Suggestion("chest-types") String rawType,
                            @ArgName("amount") @Optional Integer amount) {
        ChestHandler chestHandler = ChestHandler.getInstance();
        SellChestType chestType = chestHandler.getType(rawType);
        ConfigHandler configHandler = ConfigHandler.getInstance();

        if (chestType != null) {
            chestHandler.giveChest(target, chestType, Objects.requireNonNullElse(amount, 1));
            String senderMes = configHandler.getMessageLang("ChestGaveAdmin")
                    .replace("%player%", target.getName())
                    .replace("%name%", chestType.getName());
            String targetMes = configHandler.getMessageLang("ChestGaveTarget").replace("%name%", chestType.getName());
            sender.sendMessage(senderMes);
            if (!sender.equals(target))
                target.sendMessage(targetMes);
        }
        else {
            configHandler.sendMessageLang(target, "NoChestWithType");
        }
    }

    @Permission("litsellchest.admin.reload")
    @SubCommand("reload")
    public void reload(CommandSender sender) {
        ChestHandler chestHandler = ChestHandler.getInstance();
        ConfigHandler configHandler = ConfigHandler.getInstance();

        chestHandler.loadChestTypes();
        chestHandler.startTask();
        configHandler.sendMessageLang(sender, "PluginReloaded");
    }

}
