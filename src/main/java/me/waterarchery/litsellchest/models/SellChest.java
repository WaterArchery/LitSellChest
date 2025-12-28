package me.waterarchery.litsellchest.models;

import com.chickennw.utils.managers.HookManager;
import com.chickennw.utils.models.hooks.types.HologramHook;
import com.chickennw.utils.utils.ConfigUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.waterarchery.litsellchest.configuration.config.LangFile;
import me.waterarchery.litsellchest.managers.ChestManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "lsc_sellchests")
@NoArgsConstructor
public class SellChest {

    @Id
    private UUID uuid;

    @Column
    private UUID owner;

    @Column
    private String world;

    @Column
    private String chestTypeId;

    @Column
    private int x;

    @Column
    private int y;

    @Column
    private int z;

    @Column
    private double money;

    @Transient
    private int remainingTime;

    @Transient
    private ChestStatus status;

    @Transient
    private SellChestType chestType;

    @Transient
    private double heightOffset;

    public SellChest(UUID uuid, UUID owner, SellChestType chestType, String worldName, int x, int y, int z, double money, ChestStatus status) {
        this.uuid = uuid;
        this.owner = owner;
        this.chestType = chestType;
        this.money = money;
        this.status = status;
        this.world = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.remainingTime = (int) chestType.getSellInterval();
        chestTypeId = chestType.getId();

        LangFile langFile = ConfigUtils.get(LangFile.class);
        heightOffset = langFile.getChestHologram().getHeight();
    }

    @PostLoad
    public void onLoad() {
        ChestManager chestManager = ChestManager.getInstance();
        chestType = chestManager.getType(chestTypeId);

        this.remainingTime = (int) chestType.getSellInterval();
        status = ChestStatus.WAITING;

        LangFile langFile = ConfigUtils.get(LangFile.class);
        heightOffset = langFile.getChestHologram().getHeight();
    }

    public void createHologram() {
        if (!isLoaded()) return;

        HologramHook hologramHook = HookManager.getInstance().getHookWithType(HologramHook.class);
        List<String> lines = getHologramLines();
        hologramHook.create(getHologramLocation(), lines);
    }

    public void deleteHologram() {
        if (!isLoaded()) return;

        HologramHook hologramHook = HookManager.getInstance().getHookWithType(HologramHook.class);
        hologramHook.remove(getHologramLocation());
    }

    public void updateHologram() {
        if (!isLoaded()) return;

        HologramHook hologramHook = HookManager.getInstance().getHookWithType(HologramHook.class);
        hologramHook.update(getHologramLocation(), getHologramLines());
    }

    public List<String> getHologramLines() {
        LangFile langFile = ConfigUtils.get(LangFile.class);
        List<String> lines = new ArrayList<>();

        for (String line : langFile.getChestHologram().getLines()) {
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
        LangFile langFile = ConfigUtils.get(LangFile.class);

        String statusText = "";
        if (status == ChestStatus.WAITING) statusText = langFile.getStatus().getWaiting();
        else if (status == ChestStatus.SELLING) statusText = langFile.getStatus().getSelling();
        else if (status == ChestStatus.STOPPED) statusText = langFile.getStatus().getStopped();

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

    public Location getLocation() {
        World world = Bukkit.getWorld(this.world);
        return new Location(world, x, y, z);
    }

    public int getRemainingTime() {
        return Math.max(remainingTime, 0);
    }

    public Location getHologramLocation() {
        return this.getLocation().clone().add(0.5, heightOffset, 0.5);
    }
}
