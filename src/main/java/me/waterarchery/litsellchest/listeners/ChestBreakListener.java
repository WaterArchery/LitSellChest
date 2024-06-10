package me.waterarchery.litsellchest.listeners;

import me.waterarchery.litsellchest.handlers.ChestHandler;
import me.waterarchery.litsellchest.handlers.ConfigHandler;
import me.waterarchery.litsellchest.models.SellChest;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class ChestBreakListener implements Listener {

    @EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChestBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ChestHandler chestHandler  = ChestHandler.getInstance();

        if (block.getType() == Material.CHEST) {
            SellChest sellChest = chestHandler.getSellChest(block);
            if (sellChest != null) {
                event.setCancelled(true);
                chestHandler.deleteChest(sellChest, true, true);
                ConfigHandler configHandler = ConfigHandler.getInstance();
                configHandler.sendMessageLang(player, "BrokeChest");
            }
        }
    }

}
