package me.waterarchery.litsellchest.models;

import lombok.Getter;
import lombok.Setter;
import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.hooks.HologramHook;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.handlers.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SellChest {

    private final UUID uuid;
    private final UUID owner;
    private final SellChestType chestType;
    private int remainingTime;
    private double money;
    private ChestStatus status;
    private final String worldName;
    private final int x;
    private final int y;
    private final int z;
    private final double heightOffset;

    public SellChest(UUID uuid, UUID owner, SellChestType chestType, String worldName, int x, int y, int z, double money, ChestStatus status) {
        this.uuid = uuid;
        this.owner = owner;
        this.chestType = chestType;
        this.remainingTime = (int) chestType.getSellInterval();
        this.money = money;
        this.status = status;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        FileConfiguration yml = ConfigHandler.getInstance().getLang().getYml();
        heightOffset = yml.getDouble("ChestHologram.height");
    }

    public void createHologram() {
        if (!isLoaded()) return;

        LitLibs libs = LitSellChest.getLibs();
        HologramHook hologramHook = libs.getHookHandler().getHologramHook();
        List<String> lines = getHologramLines();
        hologramHook.createHologram(getHologramLocation(), lines);
    }

    public void deleteHologram() {
        if (!isLoaded()) return;

        LitLibs libs = LitSellChest.getLibs();
        HologramHook hologramHook = libs.getHookHandler().getHologramHook();
        hologramHook.deleteHologram(getHologramLocation());
    }

    public void updateHologram() {
        if (!isLoaded()) return;

        LitLibs libs = LitSellChest.getLibs();
        HologramHook hologramHook = libs.getHookHandler().getHologramHook();
        hologramHook.updateHologram(getHologramLocation(), getHologramLines());
    }

    public List<String> getHologramLines() {
        FileConfiguration yml = ConfigHandler.getInstance().getLang().getYml();
        List<String> rawLines = yml.getStringList("ChestHologram.lines");
        List<String> lines = new ArrayList<>();

        for (String line : rawLines) {
            line = line.replace("%remainingTime%", getRemainingTime() + "")
                    .replace("%money%", getMoney() + "")
                    .replace("%status%", statusToText())
                    .replace("%name%", getChestType().getRawName())
                    .replace("%sellMultiplier%", getChestType().getSellMultiplier() + "")
                    .replace("%tax%", getChestType().getTax() + "")
                    .replace("%sellInterval%", getChestType().getSellInterval() + "");
            lines.add(line);
        }
        return lines;
    }

    public String statusToText() {
        FileConfiguration yml = ConfigHandler.getInstance().getLang().getYml();

        String statusText = "";
        if (status == ChestStatus.WAITING)
            statusText = yml.getString("Status.waiting");
        else if (status == ChestStatus.SELLING)
            statusText = yml.getString("Status.selling");
        else if (status == ChestStatus.STOPPED)
            statusText = yml.getString("Status.stopped");

        return statusText;
    }

    public boolean isLoaded() {
        int chunkX = (int) (getLocation().getX() / 16);
        int chunkZ = (int) (getLocation().getZ() / 16);
        World world = getLocation().getWorld();
        if (world != null) {
            return world.isChunkLoaded(chunkX, chunkZ);
        }
        return false;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Location getLocation() {
        World world = Bukkit.getWorld(worldName);
        return new Location(world, x, y, z);
    }

    public int getRemainingTime() {
        return Math.max(remainingTime, 0);
    }

    public Location getHologramLocation() {
        return this.getLocation().clone().add(0.5, heightOffset, 0.5);
    }

}
