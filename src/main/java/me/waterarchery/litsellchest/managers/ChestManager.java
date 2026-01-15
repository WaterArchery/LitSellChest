package me.waterarchery.litsellchest.managers;

import com.chickennw.utils.logger.LoggerFactory;
import com.chickennw.utils.managers.HookManager;
import com.chickennw.utils.models.hooks.impl.other.VaultHook;
import com.chickennw.utils.utils.ChatUtils;
import com.chickennw.utils.utils.ConfigUtils;
import com.chickennw.utils.utils.NbtUtils;
import com.chickennw.utils.utils.SoundUtils;
import lombok.Getter;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.configuration.config.ChestsFile;
import me.waterarchery.litsellchest.configuration.config.ConfigFile;
import me.waterarchery.litsellchest.configuration.config.LangFile;
import me.waterarchery.litsellchest.configuration.config.SoundsFile;
import me.waterarchery.litsellchest.database.SellChestDatabase;
import me.waterarchery.litsellchest.models.ChestStatus;
import me.waterarchery.litsellchest.models.SellChest;
import me.waterarchery.litsellchest.models.SellChestType;
import me.waterarchery.litsellchest.models.SellTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ChestManager {

    private static ChestManager instance;
    private final List<SellChestType> chestTypes = new ArrayList<>();
    private final ConcurrentHashMap<UUID, SellChest> loadedChests = new ConcurrentHashMap<>();
    private final HashMap<UUID, UUID> chestOwners = new HashMap<>();
    private final Logger logger;
    private SellTask task;

    public static ChestManager getInstance() {
        if (instance == null) instance = new ChestManager();
        return instance;
    }

    private ChestManager() {
        logger = LoggerFactory.getLogger();
        loadChestTypes();
        startTask();
    }

    public void startTask() {
        if (task != null) {
            task.cancel();
        }

        ConfigFile configFile = ConfigUtils.get(ConfigFile.class);
        int interval = configFile.getDefaultCheckInterval();
        task = new SellTask(interval);
        task.runTaskTimer(LitSellChest.getInstance(), 20, interval * 20L);
    }

    public boolean isLocationValid(Location location) {
        for (SellChest chest : loadedChests.values()) {
            if (chest.isLoaded()) {
                if (chest.getLocation().getWorld().getName().equalsIgnoreCase(location.getWorld().getName())) {
                    if (chest.getLocation().distance(location) < 2) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void loadChestTypes() {
        ChestsFile chestsFile = ConfigUtils.get(ChestsFile.class);

        chestTypes.clear();
        for (Map.Entry<String, ChestsFile.ChestConfig> entry : chestsFile.getChests().entrySet()) {
            SellChestType type = new SellChestType(entry.getKey(), entry.getValue());
            chestTypes.add(type);
            logger.info("Loaded Sell Chest type: {}", entry.getKey());
        }
    }

    public SellChestType getType(String type) {
        SellChestType chestType = null;
        for (SellChestType sellChestType : getChestTypes()) {
            if (sellChestType.getId().equalsIgnoreCase(type)) {
                chestType = sellChestType;
                break;
            }
        }

        return chestType;
    }

    public int getMaxPlaceableChests(OfflinePlayer p) {
        int current = 0;
        if (!p.isOnline()) {
            return -1;
        }

        ConfigFile configFile = ConfigUtils.get(ConfigFile.class);
        for (String node : configFile.getPlaceLimits()) {
            String permNode = node.split(":")[0];
            int count = Integer.parseInt(node.split(":")[1]);
            if (((Player) p).hasPermission(permNode)) {
                if (count > current) {
                    current = count;
                }
            }
        }
        return current;
    }

    public int getChestCount(OfflinePlayer p) {
        UUID uuid = p.getUniqueId();
        int count = 0;
        for (SellChest sellChest : loadedChests.values()) {
            if (sellChest != null && sellChest.getOwner().equals(uuid)) {
                count++;
            }
        }
        return count;
    }

    public SellChestType getType(ItemStack itemStack) {
        String rawType = NbtUtils.getValue(itemStack, "SellChestType");
        if (rawType != null) {
            return getType(rawType);
        }

        return null;
    }

    public ChestStatus parseStatus(String status) {
        if (status.equalsIgnoreCase("WAITING"))
            return ChestStatus.WAITING;
        else if (status.equalsIgnoreCase("SELLING"))
            return ChestStatus.SELLING;
        else if (status.equalsIgnoreCase("STOPPED"))
            return ChestStatus.STOPPED;

        return null;
    }

    public void giveChest(Player player, SellChestType chestType, int amount) {
        ItemStack itemStack = chestType.getItemStack();
        itemStack.setAmount(amount);
        itemStack = NbtUtils.setKey(itemStack, "SellChestType", chestType.getId());
        player.getInventory().addItem(itemStack);
    }

    public List<String> getChestTypesAsString() {
        List<String> list = new ArrayList<>();
        getChestTypes().forEach((chest) -> list.add(chest.getId()));
        return list;
    }

    public SellChest getSellChest(Block block) {
        for (SellChest chest : loadedChests.values()) {
            if (chest.isLoaded()) {
                if (chest.getLocation().getWorld().getName().equalsIgnoreCase(block.getWorld().getName())) {
                    if (chest.getLocation().distance(block.getLocation()) == 0) {
                        return chest;
                    }
                }
            }
        }
        return null;
    }

    public SellChest getSellChest(UUID uuid) {
        for (SellChest chest : loadedChests.values()) {
            if (chest.isLoaded()) {
                if (chest.getUuid().equals(uuid)) {
                    return chest;
                }
            }
        }
        return null;
    }

    public List<SellChest> getPlayerChests(Player player) {
        List<SellChest> playerChests = new ArrayList<>();

        for (SellChest chest : loadedChests.values()) {
            if (chest.getOwner().equals(player.getUniqueId())) {
                playerChests.add(chest);
            }
        }

        return playerChests;
    }

    public void openChestInventoryOpen(SellChest sellChest, Player player) {
        LangFile langFile = ConfigUtils.get(LangFile.class);
        SoundsFile soundsFile = ConfigUtils.get(SoundsFile.class);

        if (sellChest.isLoaded()) {
            Chest chestBlock = (Chest) sellChest.getLocation().getBlock().getState();
            Inventory inventory = chestBlock.getBlockInventory();
            player.openInventory(inventory);
            SoundUtils.sendSoundRaw(player, soundsFile.getInventoryOpened());

            if (chestOwners.get(player.getUniqueId()) == null) chestOwners.put(player.getUniqueId(), sellChest.getUuid());
            else chestOwners.replace(player.getUniqueId(), sellChest.getUuid());
        } else {
            ChatUtils.sendMessage(player, langFile.getNeedToNear());
            SoundUtils.sendSoundRaw(player, soundsFile.getNeedToNear());
        }
    }

    public void deleteChest(SellChest chest, boolean dropItem, boolean dropContents) {
        chest.deleteHologram();
        loadedChests.remove(chest.getUuid());

        for (UUID playerUUID : chestOwners.keySet()) {
            UUID chestUUID = chestOwners.get(playerUUID);

            if (chestUUID.equals(chest.getUuid())) {
                chestOwners.remove(playerUUID);

                Player player = Bukkit.getPlayer(playerUUID);
                if (player != null) player.closeInventory();
            }
        }

        // Deleting from database
        SellChestDatabase database = SellChestDatabase.getInstance();
        database.delete(chest);

        if (dropItem) {
            ItemStack itemStack = chest.getChestType().getItemStack();
            chest.getLocation().getWorld().dropItemNaturally(chest.getLocation(), itemStack);
        }

        if (dropContents) {
            Chest chestBlock = (Chest) chest.getLocation().getBlock().getState();
            chestBlock.getBlockInventory().forEach((item) -> {
                if (item != null) chest.getLocation().getWorld().dropItemNaturally(chest.getLocation(), item);
            });
        }

        chest.getLocation().getBlock().setType(Material.AIR);
    }

    public void handleShopBuy(Player player, SellChestType sellChestType) {
        VaultHook vaultHook = HookManager.getInstance().getHookWithExact(VaultHook.class);
        LangFile langFile = ConfigUtils.get(LangFile.class);
        SoundsFile soundsFile = ConfigUtils.get(SoundsFile.class);

        if (sellChestType != null) {
            double price = sellChestType.getPrice();
            double balance = vaultHook.getPlayerMoney(player);
            if (price <= balance) {
                vaultHook.takeMoney(player, price);
                ItemStack placeItem = sellChestType.getItemStack();
                player.getInventory().addItem(placeItem);

                String mes = langFile.getChestBought().replace("%money%", (balance - price) + "");
                ChatUtils.sendMessage(player, mes);
                SoundUtils.sendSoundRaw(player, soundsFile.getChestReceive());
            } else {
                ChatUtils.sendMessage(player, langFile.getNotEnoughMoney());
            }
        } else {
            logger.error("SellChestType not found in shop.");
        }
    }
}
