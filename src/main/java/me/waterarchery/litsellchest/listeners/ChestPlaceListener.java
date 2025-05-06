package me.waterarchery.litsellchest.listeners;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.database.Database;
import me.waterarchery.litsellchest.handlers.ChestHandler;
import me.waterarchery.litsellchest.handlers.ConfigHandler;
import me.waterarchery.litsellchest.handlers.SoundManager;
import me.waterarchery.litsellchest.models.ChestStatus;
import me.waterarchery.litsellchest.models.SellChest;
import me.waterarchery.litsellchest.models.SellChestType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ChestPlaceListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onChestPlace(BlockPlaceEvent event) {
        if (!event.isCancelled()) {
            Block block = event.getBlock();
            Player player = event.getPlayer();
            ItemStack itemStack = event.getItemInHand();
            ChestHandler chestHandler = ChestHandler.getInstance();
            SellChestType chestType = chestHandler.getType(itemStack);
            ConfigHandler configHandler = ConfigHandler.getInstance();
            LitLibs libs = LitSellChest.getLibs();

            if (chestType != null) {
                if (chestHandler.isLocationValid(block.getLocation())) {
                    int chestCount = chestHandler.getChestCount(player);
                    int maxChests = chestHandler.getMaxPlaceableChests(player);
                    if (chestCount < maxChests) {
                        int x = block.getX();
                        int y = block.getY();
                        int z = block.getZ();
                        String world = block.getWorld().getName();
                        SellChest sellChest = new SellChest(UUID.randomUUID(), player.getUniqueId(), chestType,
                                world, x, y, z, 0, ChestStatus.WAITING);
                        sellChest.createHologram();
                        chestHandler.addLoadedChest(sellChest);
                        configHandler.sendMessageLang(player, "ChestPlaced");
                        Database database = LitSellChest.getDatabase();
                        database.saveChest(sellChest);
                        SoundManager.sendSound(player, "ChestPlace");
                    }
                    else {
                        String mes = configHandler.getRawMessageLang("LimitReached").replace("%current%", chestCount + "")
                                .replace("%max%", maxChests + "");
                        libs.getMessageHandler().sendMessage(player, mes);
                        SoundManager.sendSound(player, "LimitExceedSound");
                    }
                }
                else {
                    event.setCancelled(true);
                    configHandler.sendMessageLang(player, "ChestsTooNear");
                    SoundManager.sendSound(player, "ChestsTooNear");
                }
            }
        }
    }

}
