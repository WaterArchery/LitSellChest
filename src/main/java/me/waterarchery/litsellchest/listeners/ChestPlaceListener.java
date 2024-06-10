package me.waterarchery.litsellchest.listeners;

import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.database.SQLiteDatabase;
import me.waterarchery.litsellchest.handlers.ChestHandler;
import me.waterarchery.litsellchest.handlers.ConfigHandler;
import me.waterarchery.litsellchest.handlers.SoundManager;
import me.waterarchery.litsellchest.models.ChestStatus;
import me.waterarchery.litsellchest.models.SellChest;
import me.waterarchery.litsellchest.models.SellChestType;
import org.bukkit.Location;
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

            if (chestType != null) {
                if (chestHandler.isLocationValid(block.getLocation())) {
                    Location location = block.getLocation();
                    SellChest sellChest = new SellChest(UUID.randomUUID(), player.getUniqueId(), chestType, location, 0, ChestStatus.WAITING);
                    sellChest.createHologram();
                    chestHandler.addLoadedChest(sellChest);
                    ConfigHandler.getInstance().sendMessageLang(player, "ChestPlaced");
                    SoundManager.sendSound(player, "ChestPlace");
                    SQLiteDatabase database = LitSellChest.getDatabase();
                    database.saveChest(sellChest);
                }
                else {
                    event.setCancelled(true);
                    ConfigHandler.getInstance().sendMessageLang(player, "ChestsTooNear");
                    SoundManager.sendSound(player, "ChestsTooNear");
                }
            }
        }
    }

}
