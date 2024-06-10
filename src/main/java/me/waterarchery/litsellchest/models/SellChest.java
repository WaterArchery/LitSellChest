package me.waterarchery.litsellchest.models;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.handlers.MessageHandler;
import me.waterarchery.litlibs.hooks.HologramHook;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.handlers.ConfigHandler;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SellChest {

    private final UUID uuid;
    private final UUID owner;
    private final SellChestType chestType;
    private final Location location;
    private final Location hologramLocation;
    private int remainingTime;
    private double money;
    private ChestStatus status;

    public SellChest(UUID uuid, UUID owner, SellChestType chestType, Location location, double money, ChestStatus status) {
        this.uuid = uuid;
        this.owner = owner;
        this.chestType = chestType;
        this.location = location;
        this.remainingTime = (int) chestType.getSellInterval();
        this.money = money;
        this.status = status;

        FileConfiguration yml = ConfigHandler.getInstance().getLang().getYml();
        double heightOffset = yml.getDouble("ChestHologram.height");
        hologramLocation = this.getLocation().clone().add(0.5, heightOffset, 0.5);
    }

    public void createHologram() {
        LitSellChest litSellChest = LitSellChest.getInstance();
        LitLibs libs = litSellChest.getLibs();
        HologramHook hologramHook = libs.getHookHandler().getHologramHook();
        List<String> lines = getHologramLines();
        hologramHook.createHologram(hologramLocation, lines);
    }

    public void deleteHologram() {
        LitSellChest litSellChest = LitSellChest.getInstance();
        LitLibs libs = litSellChest.getLibs();
        HologramHook hologramHook = libs.getHookHandler().getHologramHook();
        hologramHook.deleteHologram(hologramLocation);
    }

    public void updateHologram() {
        LitSellChest litSellChest = LitSellChest.getInstance();
        LitLibs libs = litSellChest.getLibs();
        HologramHook hologramHook = libs.getHookHandler().getHologramHook();
        hologramHook.updateHologram(hologramLocation, getHologramLines());
    }

    public List<String> getHologramLines() {
        FileConfiguration yml = ConfigHandler.getInstance().getLang().getYml();
        List<String> rawLines = yml.getStringList("ChestHologram.lines");
        List<String> lines = new ArrayList<>();

        for (String line : rawLines) {
            line = line.replace("%remainingTime%", getRemainingTime() + "")
                    .replace("%money%", getMoney() + "")
                    .replace("%status%", statusToText())
                    .replace("%name%", getChestType().getName())
                    .replace("%sellMultiplier%", getChestType().getSellMultiplier() + "")
                    .replace("%tax%", getChestType().getTax() + "")
                    .replace("%sellInterval%", getChestType().getSellInterval() + "");
            lines.add(line);
        }
        return lines;
    }

    public String statusToText() {
        FileConfiguration yml = ConfigHandler.getInstance().getLang().getYml();
        LitLibs libs = LitSellChest.getInstance().getLibs();
        MessageHandler mHandler = libs.getMessageHandler();

        String statusText = "";
        if (status == ChestStatus.WAITING)
            statusText = yml.getString("Status.waiting");
        else if (status == ChestStatus.SELLING)
            statusText = yml.getString("Status.selling");
        else if (status == ChestStatus.STOPPED)
            statusText = yml.getString("Status.stopped");

        return mHandler.updateColors(statusText);
    }

    public boolean isLoaded() {
        int chunkX = (int) (location.getX() / 16);
        int chunkZ = (int) (location.getZ() / 16);
        World world = location.getWorld();
        if (world != null) {
            return world.isChunkLoaded(chunkX, chunkZ);
        }
        return false;
    }

    public UUID getUUID() {
        return uuid;
    }

    public SellChestType getChestType() {
        return chestType;
    }

    public Location getLocation() {
        return location;
    }

    public int getRemainingTime() {
        return Math.max(remainingTime, 0);
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public ChestStatus getStatus() {
        return status;
    }

    public void setStatus(ChestStatus status) {
        this.status = status;
    }

    public UUID getOwner() {
        return owner;
    }

}
