package me.waterarchery.litsellchest.models;

import com.chickennw.utils.ChickenUtils;
import com.chickennw.utils.libs.folia.impl.PlatformScheduler;
import com.chickennw.utils.managers.HookManager;
import com.chickennw.utils.models.hooks.impl.other.VaultHook;
import com.chickennw.utils.models.hooks.types.PriceHook;
import com.chickennw.utils.utils.ChatUtils;
import com.chickennw.utils.utils.ConfigUtils;
import com.chickennw.utils.utils.SoundUtils;
import me.waterarchery.litsellchest.configuration.config.ConfigFile;
import me.waterarchery.litsellchest.configuration.config.LangFile;
import me.waterarchery.litsellchest.configuration.config.SoundsFile;
import me.waterarchery.litsellchest.managers.ChestManager;
import me.waterarchery.litsellchest.utils.LangUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import java.util.Collection;

public class SellTask extends BukkitRunnable {

    private final int interval;
    private final ConfigFile configFile;
    private final LangFile langFile;
    private final SoundsFile soundsFile;

    public SellTask(int interval) {
        this.interval = interval;
        configFile = ConfigUtils.get(ConfigFile.class);
        langFile = ConfigUtils.get(LangFile.class);
        soundsFile = ConfigUtils.get(SoundsFile.class);
    }

    @Override
    public void run() {
        ChestManager chestManager = ChestManager.getInstance();
        boolean checkForOnline = configFile.isOnlyWorkOnlinePlayers();

        for (SellChest sellChest : chestManager.getLoadedChests().values()) {
            if (sellChest.isLoaded() && (!checkForOnline || Bukkit.getPlayer(sellChest.getOwner()) != null)) {
                int remainingTime = sellChest.getRemainingTime();
                if (sellChest.getStatus() != ChestStatus.STOPPED)
                    sellChest.setStatus(ChestStatus.WAITING);

                if (remainingTime == 0) remainingTime = (int) sellChest.getChestType().getSellInterval();
                else remainingTime -= interval;

                sellChest.setRemainingTime(remainingTime);
                if (remainingTime <= 0) {
                    if (sellChest.getStatus() != ChestStatus.STOPPED) {
                        sellChest.setStatus(ChestStatus.SELLING);
                        handleSelling(sellChest);
                    }
                }

                sellChest.updateHologram();
            }
        }
    }

    public void handleSelling(SellChest sellChest) {
        boolean notifyOnUnsellable = configFile.isNotSellingNotification();
        boolean disableSellMessage = configFile.isDisableChestSellMessage();

        PlatformScheduler scheduler = ChickenUtils.getFoliaLib().getScheduler();
        scheduler.runNextTick((task) -> {
            PriceHook priceHook = HookManager.getInstance().getHookWithType(PriceHook.class);

            double totalPrice = 0;
            boolean hasInvalids = false;
            Block block = sellChest.getLocation().getBlock();
            BlockState state = block.getState();
            if (state instanceof Chest) {
                Chest chest = (Chest) block.getState();

                for (ItemStack itemStack : chest.getInventory()) {
                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                        double price = priceHook.calculatePrice(itemStack);
                        if (price > 0) {
                            totalPrice += price;
                            itemStack.setAmount(0);
                        }
                    }
                }

                // Selling nearby items
                SellChestType type = sellChest.getChestType();
                BoundingBox boundingBox = BoundingBox.of(block.getLocation(),
                    type.getCollectRadius(),
                    type.getCollectRadius(),
                    type.getCollectRadius());

                Collection<Entity> nearbyItems = block.getWorld().getNearbyEntities(boundingBox,
                    (entity) -> entity.getType().name().contains("ITEM"));
                for (Entity item : nearbyItems) {
                    ItemStack itemStack = ((Item) item).getItemStack().clone();
                    if (itemStack != null && itemStack.getType() != Material.AIR) {
                        double price = priceHook.calculatePrice(itemStack);
                        if (price > 0) {
                            totalPrice += price;
                            item.remove();
                        } else {
                            hasInvalids = true;
                        }
                    }
                }

                totalPrice = totalPrice * sellChest.getChestType().getSellMultiplier();

                if (totalPrice > 0) {
                    VaultHook vaultHook = HookManager.getInstance().getHookWithExact(VaultHook.class);
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(sellChest.getOwner());
                    double tax = totalPrice * (sellChest.getChestType().getTax() / 100);
                    totalPrice -= tax;
                    vaultHook.giveMoney(offlinePlayer, totalPrice);

                    if (!disableSellMessage && offlinePlayer.isOnline()) {
                        Player player = offlinePlayer.getPlayer();
                        SoundUtils.sendSoundRaw(player, soundsFile.getSellSoundToPlayer());
                        String msg = langFile.getMoneyDeposited().replace("%money%", LangUtils.formatNumber(totalPrice))
                            .replace("%tax%", tax + "");
                        ChatUtils.sendMessage(player, msg);
                    }
                }
                if (hasInvalids && notifyOnUnsellable) {
                    Player player = Bukkit.getPlayer(sellChest.getOwner());

                    if (player != null) {
                        ChatUtils.sendMessage(player, langFile.getInvalidPriceOrFree());
                    }
                }
            } else {
                block.setType(Material.CHEST);
            }
        });
    }

}
