package me.waterarchery.litsellchest.commands;

import com.chickennw.utils.libs.cmd.bukkit.annotation.Permission;
import com.chickennw.utils.libs.cmd.core.annotations.ArgName;
import com.chickennw.utils.libs.cmd.core.annotations.Command;
import com.chickennw.utils.libs.cmd.core.annotations.Optional;
import com.chickennw.utils.libs.cmd.core.annotations.Suggestion;
import com.chickennw.utils.models.commands.BaseCommand;
import com.chickennw.utils.utils.ChatUtils;
import com.chickennw.utils.utils.ConfigUtils;
import me.waterarchery.litsellchest.configuration.config.LangFile;
import me.waterarchery.litsellchest.managers.ChestManager;
import me.waterarchery.litsellchest.menus.DefaultMenu;
import me.waterarchery.litsellchest.menus.ShopMenu;
import me.waterarchery.litsellchest.menus.YourChestsMenu;
import me.waterarchery.litsellchest.models.SellChestType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

@Command(value = "litsellchest", alias = {"sellchest", "lsc"})
public class SellChestCommand extends BaseCommand {

    private final LangFile langFile;

    public SellChestCommand() {
        langFile = ConfigUtils.get(LangFile.class);
    }

    @Command
    public void mainCommand(Player player) {
        DefaultMenu defaultMenu = new DefaultMenu(player);
        defaultMenu.openAsync(player);
    }

    @Command("shop")
    public void shop(Player player) {
        ShopMenu shopMenu = new ShopMenu(player);
        shopMenu.openAsync(player);
    }

    @Command("chests")
    public void chests(Player player) {
        YourChestsMenu menu = new YourChestsMenu(player);
        menu.openAsync(player);
    }

    @Permission("litsellchest.admin.give")
    @Command("give")
    public void give(CommandSender sender, Player target,
                     @ArgName("chestType") @Suggestion("chest-types") String rawType,
                     @ArgName("amount") @Optional Integer amount) {
        ChestManager chestManager = ChestManager.getInstance();
        SellChestType chestType = chestManager.getType(rawType);

        if (chestType != null) {
            chestManager.giveChest(target, chestType, Objects.requireNonNullElse(amount, 1));

            String senderMes = langFile.getChestGaveAdmin().replace("%player%", target.getName())
                .replace("%name%", chestType.getName());
            ChatUtils.sendMessage(sender, senderMes);

            String targetMes = langFile.getChestGaveTarget().replace("%name%", chestType.getName());
            ChatUtils.sendMessage(target, targetMes);
        } else {
            ChatUtils.sendMessage(sender, langFile.getNoChestWithType());
        }
    }

    @Permission("litsellchest.admin.reload")
    @Command("reload")
    public void reload(CommandSender sender) {
        ChestManager chestManager = ChestManager.getInstance();

        chestManager.loadChestTypes();
        chestManager.startTask();
        ChatUtils.sendMessage(sender, langFile.getPluginReloaded());
    }
}
