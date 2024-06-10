package me.waterarchery.litsellchest.listeners;

import me.waterarchery.litsellchest.handlers.ChestHandler;
import me.waterarchery.litsellchest.models.SellChest;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class ChestProtectListeners implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        List<Block> blocks = event.blockList();
        ChestHandler chestHandler = ChestHandler.getInstance();

        for (Block block : new ArrayList<>(blocks)) {
            SellChest sellChest = chestHandler.getSellChest(block);
            if (sellChest != null) {
                blocks.remove(block);
            }
        }
    }

    @EventHandler
    public void onEntityGrief(EntityChangeBlockEvent event) {
        Block block = event.getBlock();
        ChestHandler chestHandler = ChestHandler.getInstance();
        SellChest sellChest = chestHandler.getSellChest(block);
        if (sellChest != null) {
            event.setCancelled(true);
        }
    }

}
