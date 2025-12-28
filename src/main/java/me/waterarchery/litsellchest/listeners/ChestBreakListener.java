package me.waterarchery.litsellchest.listeners;

import com.chickennw.utils.utils.ChatUtils;
import com.chickennw.utils.utils.ConfigUtils;
import me.waterarchery.litsellchest.configuration.config.LangFile;
import me.waterarchery.litsellchest.managers.ChestManager;
import me.waterarchery.litsellchest.models.SellChest;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class ChestBreakListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChestBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ChestManager chestManager = ChestManager.getInstance();

        if (block.getType() == Material.CHEST) {
            SellChest sellChest = chestManager.getSellChest(block);
            if (sellChest != null) {
                event.setCancelled(true);
                chestManager.deleteChest(sellChest, true, true);
                LangFile langFile = ConfigUtils.get(LangFile.class);
                ChatUtils.sendMessage(player, langFile.getBrokeChest());
            }
        }
    }
}
