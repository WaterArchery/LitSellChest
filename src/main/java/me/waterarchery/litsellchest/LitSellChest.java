package me.waterarchery.litsellchest;

import com.chickennw.utils.ChickenUtils;
import com.chickennw.utils.managers.CommandManager;
import me.waterarchery.litsellchest.database.SellChestDatabase;
import me.waterarchery.litsellchest.managers.ChestManager;
import me.waterarchery.litsellchest.managers.LoadManager;
import me.waterarchery.litsellchest.models.SellChest;
import org.bukkit.plugin.java.JavaPlugin;

public final class LitSellChest extends JavaPlugin {

    @Override
    public void onEnable() {
        ChickenUtils.setPlugin(this);
        LoadManager.getInstance();
    }

    @Override
    public void onDisable() {
        CommandManager.getInstance().unregisterCommands();

        for (SellChest sellChest : ChestManager.getInstance().getLoadedChests().values()) {
            sellChest.deleteHologram();
        }

        SellChestDatabase.getInstance().close();
    }

    public static LitSellChest getInstance() {
        return JavaPlugin.getPlugin(LitSellChest.class);
    }
}
