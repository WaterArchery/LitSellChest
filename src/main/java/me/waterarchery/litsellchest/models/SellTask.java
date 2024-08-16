package me.waterarchery.litsellchest.models;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.handlers.MessageHandler;
import me.waterarchery.litlibs.hooks.PriceHook;
import me.waterarchery.litlibs.version.Version;
import me.waterarchery.litlibs.version.VersionHandler;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.handlers.ChestHandler;
import me.waterarchery.litsellchest.handlers.ConfigHandler;
import me.waterarchery.litsellchest.handlers.SoundManager;
import me.waterarchery.litsellchest.hooks.VaultHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import java.util.Collection;

public class SellTask extends BukkitRunnable {

    private final int interval;
    private final LitLibs libs;

    public SellTask(int interval, LitLibs libs) {
        this.interval = interval;
        this.libs = libs;
    }

    @Override
    public void run() {
        ChestHandler chestHandler = ChestHandler.getInstance();
        boolean checkForOnline = ConfigHandler.getInstance().getConfig().getYml().getBoolean("OnlyWorkOnlinePlayers", true);

        for (SellChest sellChest : chestHandler.getLoadedChests()) {
            if (sellChest.isLoaded() && (!checkForOnline || Bukkit.getPlayer(sellChest.getOwner()) != null)) {
                int remainingTime = sellChest.getRemainingTime();
                if (sellChest.getStatus() != ChestStatus.STOPPED)
                    sellChest.setStatus(ChestStatus.WAITING);

                if (remainingTime == 0)
                    remainingTime = (int) sellChest.getChestType().getSellInterval();
                else
                    remainingTime -= interval;

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
        boolean isSellWithLore = LitSellChest.getInstance().getConfig().getBoolean("SellOnlyItemsWithLore");

        new BukkitRunnable() {
            @Override
            public void run() {
                PriceHook priceHook = libs.getHookHandler().getPriceHook();
                MessageHandler messageHandler = libs.getMessageHandler();
                ConfigHandler configHandler = ConfigHandler.getInstance();

                double totalPrice = 0;
                Block block = sellChest.getLocation().getBlock();
                BlockState state = block.getState();
                if (state instanceof Chest) {
                    Chest chest = (Chest) block.getState();
                    // Selling chest contents
                    for (ItemStack itemStack : chest.getInventory()) {
                        if (itemStack != null && itemStack.getType() != Material.AIR) {
                            double price = priceHook.getPrice(itemStack);
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

                    VersionHandler versionHandler = LitSellChest.getInstance().getLibs().getVersionHandler();

                    Collection<Entity> nearbyItems = block.getWorld().getNearbyEntities(boundingBox,
                            (entity) -> versionHandler.isServerNewerThan(Version.v1_21)
                                    ? entity.getType() == EntityType.ITEM
                                    : entity.getType() == EntityType.valueOf("DROPPED_ITEM"));
                    for (Entity item : nearbyItems) {
                        ItemStack itemStack = ((Item) item).getItemStack().clone();
                        if (itemStack != null && itemStack.getType() != Material.AIR) {
                            double price = priceHook.getPrice(itemStack);
                            if (price > 0) {
                                totalPrice += price;
                                item.remove();
                            }
                        }
                    }

                    if (totalPrice > 0) {
                        Economy econ = VaultHook.getInstance().getEcon();
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(sellChest.getOwner());
                        double tax = totalPrice * (sellChest.getChestType().getTax() / 100);
                        totalPrice -= tax;
                        econ.depositPlayer(offlinePlayer, totalPrice);
                        if (offlinePlayer.isOnline()) {
                            Player player = offlinePlayer.getPlayer();
                            SoundManager.sendSound(player, "SellSoundToPlayer");
                            String mes = configHandler.getMessageLang("MoneyDeposited");
                            String formatted = String.format("%,.2f", totalPrice);
                            mes = mes.replace("%money%", formatted);
                            mes = mes.replace("%tax%", tax + "");
                            messageHandler.sendMessage(player, mes);
                        }
                    }
                }
                else {
                    block.setType(Material.CHEST);
                }

            }
        }.runTask(LitSellChest.getInstance());
    }

}
