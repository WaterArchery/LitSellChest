package me.waterarchery.litsellchest.database;

import com.chickennw.utils.database.sql.Database;
import com.chickennw.utils.models.config.database.DatabaseConfiguration;
import com.chickennw.utils.utils.ConfigUtils;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.configuration.config.ConfigFile;
import me.waterarchery.litsellchest.models.SellChest;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.Session;

import java.util.List;

public class SellChestDatabase extends Database {

    private static SellChestDatabase instance;

    public static SellChestDatabase getInstance() {
        if (instance == null) {
            ConfigFile configFile = ConfigUtils.get(ConfigFile.class);
            DatabaseConfiguration databaseConfiguration = configFile.getDatabase();
            instance = new SellChestDatabase(LitSellChest.getInstance(), databaseConfiguration);
        }

        return instance;
    }

    private SellChestDatabase(JavaPlugin plugin, DatabaseConfiguration config) {
        super(plugin, config);
    }

    public List<SellChest> loadSellChests() {
        try (Session session = this.getSessionFactory().openSession()) {
            return session.createQuery("FROM SellChest", SellChest.class).list();
        }
    }
}
