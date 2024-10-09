package me.waterarchery.litsellchest.handlers;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.configuration.ConfigManager;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.database.Database;
import me.waterarchery.litsellchest.models.ChestStatus;
import me.waterarchery.litsellchest.models.SellChest;
import me.waterarchery.litsellchest.models.SellChestType;
import me.waterarchery.litsellchest.models.SellTask;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChestHandler {

    private final List<SellChestType> chestTypes = new ArrayList<>();
    private final List<SellChest> loadedChests = new ArrayList<>();
    private SellTask task;
    private static ChestHandler instance;

    public static ChestHandler getInstance() {
        if (instance == null) instance = new ChestHandler();
        return instance;
    }

    private ChestHandler() {
        loadChestTypes();
        startTask();
    }

    public void startTask() {
        if (task != null) {
            task.cancel();
        }

        ConfigHandler configHandler = ConfigHandler.getInstance();
        ConfigManager manager = configHandler.getConfig();
        int interval = manager.getYml().getInt("DefaultCheckInterval");
        LitLibs libs = LitSellChest.getLibs();
        task = new SellTask(interval, libs);
        task.runTaskTimer(LitSellChest.getInstance(), 20, interval * 20L);
    }

    public boolean isLocationValid(Location location) {
        for (SellChest chest : getLoadedChests()) {
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
        LitLibs libs = LitSellChest.getLibs();
        ConfigHandler configHandler = ConfigHandler.getInstance();
        ConfigManager manager = configHandler.getChests();
        FileConfiguration yml = manager.getYml();

        chestTypes.clear();
        for (String chestID : yml.getConfigurationSection("").getKeys(false)) {
            SellChestType type = new SellChestType(chestID);
            chestTypes.add(type);
            libs.getLogger().log("Loaded Sell Chest type: " + chestID);
        }
    }

    public @Nullable SellChestType getType(String type) {
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
        for (String node : LitSellChest.getInstance().getConfig().getStringList("PlaceLimits")) {
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
        for (SellChest sellChest : loadedChests) {
            if (sellChest != null && sellChest.getOwner().equals(uuid)) {
                count++;
            }
        }
        return count;
    }

    public @Nullable SellChestType getType(ItemStack itemStack) {
        LitLibs libs = LitSellChest.getLibs();
        String rawType = libs.getNBTAPIHook().getNBT(itemStack, "SellChestType");
        if (rawType != null) {
            return getType(rawType);
        }

        return null;
    }

    public @Nullable ChestStatus parseStatus(String status) {
        if (status.equalsIgnoreCase("WAITING"))
            return ChestStatus.WAITING;
        else if (status.equalsIgnoreCase("SELLING"))
            return ChestStatus.SELLING;
        else if (status.equalsIgnoreCase("STOPPED"))
            return ChestStatus.STOPPED;

        return null;
    }

    public void giveChest(Player player, SellChestType chestType, int amount) {
        LitSellChest instance = LitSellChest.getInstance();
        LitLibs libs = instance.getLibs();
        ItemStack itemStack = chestType.toItemStack();
        itemStack.setAmount(amount);
        itemStack = libs.getNBTAPIHook().setNBT(itemStack, "SellChestType", chestType.getId());
        player.getInventory().addItem(itemStack);
    }

    public List<String> getChestTypesAsString() {
        List<String> list = new ArrayList<>();
        getChestTypes().forEach((chest) -> list.add(chest.getId()));
        return list;
    }

    public @Nullable SellChest getSellChest(Block block) {
        for (SellChest chest : getLoadedChests()) {
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

    public @Nullable SellChest getSellChest(UUID uuid) {
        for (SellChest chest : getLoadedChests()) {
            if (chest.isLoaded()) {
                if (chest.getUUID().equals(uuid)) {
                    return chest;
                }
            }
        }
        return null;
    }

    public List<SellChest> getPlayerChests(Player player) {
        List<SellChest> playerChests = new ArrayList<>();

        for (SellChest chest : getLoadedChests()) {
            if (chest.getOwner().equals(player.getUniqueId())) {
                playerChests.add(chest);
            }
        }

        return playerChests;
    }

    public void openChestInventoryOpen(@NotNull SellChest sellChest, @NotNull Player player) {
        if (sellChest.isLoaded()) {
            Chest chestBlock = (Chest) sellChest.getLocation().getBlock().getState();
            Inventory inventory = chestBlock.getBlockInventory();
            player.openInventory(inventory);
            SoundManager.sendSound(player, "InventoryOpened");
        }
        else {
            ConfigHandler.getInstance().sendMessageLang(player, "NeedToNear");
            SoundManager.sendSound(player, "NeedToNear");
        }
    }

    public void deleteChest(SellChest chest, boolean dropItem, boolean dropContents) {
        chest.deleteHologram();
        loadedChests.remove(chest);

        // Deleting from database
        Database database = LitSellChest.getDatabase();
        database.deleteChest(chest.getUUID());

        if (dropItem) {
            ItemStack itemStack = chest.getChestType().toItemStack();
            chest.getLocation().getWorld().dropItemNaturally(chest.getLocation(), itemStack);
        }

        if (dropContents) {
            Chest chestBlock = (Chest) chest.getLocation().getBlock().getState();
            chestBlock.getBlockInventory().forEach((item) -> {
                if (item != null)
                    chest.getLocation().getWorld().dropItemNaturally(chest.getLocation(), item);
            });
        }

        chest.getLocation().getBlock().setType(Material.AIR);
    }

    public void addLoadedChest(SellChest chest) {
        loadedChests.add(chest);
    }

    public List<SellChestType> getChestTypes() {
        return chestTypes;
    }

    public List<SellChest> getLoadedChests() {
        return new ArrayList<>(loadedChests);
    }

}
