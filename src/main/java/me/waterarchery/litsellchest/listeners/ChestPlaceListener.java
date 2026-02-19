package me.waterarchery.litsellchest.listeners;

import com.chickennw.utils.utils.ChatUtils;
import com.chickennw.utils.utils.ConfigUtils;
import com.chickennw.utils.utils.SoundUtils;
import me.waterarchery.litsellchest.configuration.config.LangFile;
import me.waterarchery.litsellchest.configuration.config.SoundsFile;
import me.waterarchery.litsellchest.database.SellChestDatabase;
import me.waterarchery.litsellchest.managers.ChestManager;
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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChestPlace(BlockPlaceEvent event) {
        if (!event.isCancelled()) {
            Block block = event.getBlock();
            Player player = event.getPlayer();
            ItemStack itemStack = event.getItemInHand();
            ChestManager chestManager = ChestManager.getInstance();
            SellChestType chestType = chestManager.getType(itemStack);
            LangFile langFile = ConfigUtils.get(LangFile.class);
            SoundsFile soundsFile = ConfigUtils.get(SoundsFile.class);

            if (chestType != null) {
                if (chestManager.isLocationValid(block.getLocation())) {
                    int chestCount = chestManager.getChestCount(player);
                    int maxChests = chestManager.getMaxPlaceableChests(player);
                    if (chestCount < maxChests) {
                        int x = block.getX();
                        int y = block.getY();
                        int z = block.getZ();
                        String world = block.getWorld().getName();

                        SellChest sellChest = new SellChest(UUID.randomUUID(), player.getUniqueId(), chestType, world, x, y, z, 0, ChestStatus.WAITING);
                        sellChest.createHologram();
                        chestManager.getLoadedChests().put(sellChest.getUuid(), sellChest);

                        SellChestDatabase database = SellChestDatabase.getInstance();
                        database.save(sellChest);

                        ChatUtils.sendMessage(player, langFile.getChestPlaced());
                        SoundUtils.sendSoundRaw(player, soundsFile.getChestPlace());
                    } else {
                        String mes = langFile.getLimitReached().replace("%current%", chestCount + "")
                            .replace("%max%", maxChests + "");
                        ChatUtils.sendMessage(player, mes);
                        SoundUtils.sendSoundRaw(player, soundsFile.getLimitExceedSound());
                        event.setCancelled(true);
                    }
                } else {
                    event.setCancelled(true);
                    ChatUtils.sendMessage(player, langFile.getChestsTooNear());
                    SoundUtils.sendSoundRaw(player, soundsFile.getChestsTooNear());
                }
            }
        }
    }

}
