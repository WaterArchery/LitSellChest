package me.waterarchery.litsellchest.database;

import me.waterarchery.litlibs.LitLibs;
import me.waterarchery.litlibs.database.SQLite;
import me.waterarchery.litlibs.logger.Logger;
import me.waterarchery.litsellchest.LitSellChest;
import me.waterarchery.litsellchest.handlers.ChestHandler;
import me.waterarchery.litsellchest.models.ChestStatus;
import me.waterarchery.litsellchest.models.SellChest;
import me.waterarchery.litsellchest.models.SellChestType;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SQLiteDatabase extends SQLite {

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(200);
    public static final String TABLE_NAME = "chests";

    public static final String TABLE_TOKEN = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            "`id` varchar(36) NOT NULL PRIMARY KEY," +
            "`owner` varchar(36) NOT NULL," +
            "`x` double(11) NOT NULL," +
            "`y` double(11) NOT NULL," +
            "`z` double(11) NOT NULL," +
            "`world` varchar(32) NOT NULL," +
            "`money` double(11) NOT NULL," +
            "`chestType` varchar(32) NOT NULL," +
            "`status` varchar(32) NOT NULL" +
            ");";


    public SQLiteDatabase(LitLibs litLibs) {
        super(litLibs);
        initialize();
    }

    public void initialize() {
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add(TABLE_TOKEN);

        createDatabase(tokens);
    }

    public void saveChest(SellChest chest) {
        Runnable runnable = () -> {
            Connection connection = getSQLConnection();
            String query = "REPLACE INTO " + TABLE_NAME + " (id,owner,x,y,z,world,money,chestType,status) VALUES(?,?,?,?,?,?,?,?,?)";
            Logger logger = LitSellChest.getInstance().getLibs().getLogger();
            try {
                PreparedStatement ps =connection.prepareStatement(query);
                ps.setString(1, chest.getUUID().toString());
                ps.setString(2, chest.getOwner().toString());
                ps.setInt(3, chest.getLocation().getBlockX());
                ps.setInt(4, chest.getLocation().getBlockY());
                ps.setInt(5, chest.getLocation().getBlockZ());
                ps.setString(6, chest.getLocation().getWorld().getName());
                ps.setDouble(7, chest.getMoney());
                ps.setString(8, chest.getChestType().getId());
                ps.setString(9, chest.getStatus().name());
                ps.executeUpdate();
            }
            catch (SQLException ex) {
                logger.error(ex.getMessage());
            }
        };

        threadPool.submit(runnable);
    }

    public void deleteChest(UUID uuid) {
        Runnable runnable = () -> {
            Connection connection = getSQLConnection();
            String query = "DELETE FROM " + TABLE_NAME + " WHERE id='" + uuid + "';";
            Logger logger = LitSellChest.getInstance().getLibs().getLogger();
            try {
                PreparedStatement ps =connection.prepareStatement(query);
                ps.executeUpdate();
            }
            catch (SQLException ex) {
                logger.error(ex.getMessage());
            }
        };

        threadPool.submit(runnable);
    }

    public int loadChests() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int total = 0;
        try {
            ChestHandler chestHandler = ChestHandler.getInstance();
            Connection conn = getSQLConnection();
            Logger logger = LitSellChest.getInstance().getLibs().getLogger();

            ps = conn.prepareStatement("SELECT * FROM " + TABLE_NAME + ";");
            rs = ps.executeQuery();
            while (rs.next()) {
                String rawID = rs.getString("id");
                String rawOwner = rs.getString("owner");
                double x = rs.getDouble("x");
                double y = rs.getDouble("y");
                double z = rs.getDouble("z");
                String world = rs.getString("world");
                double money = rs.getDouble("money");
                String rawType = rs.getString("chestType");
                String rawStatus = rs.getString("status");

                ChestStatus status = chestHandler.parseStatus(rawStatus);
                SellChestType type = chestHandler.getType(rawType);
                UUID id = UUID.fromString(rawID);
                UUID owner = UUID.fromString(rawOwner);

                if (status == null)
                    logger.error("Error while parsing status. Got value: " + rawStatus);
                else if (type == null)
                    logger.error("Error while parsing type. Got value: " + rawType);
                else {
                    Location loc = new Location(Bukkit.getWorld(world), x, y, z);
                    SellChest sellChest = new SellChest(id, owner, type, loc, money, status);
                    sellChest.createHologram();
                    chestHandler.addLoadedChest(sellChest);
                    total++;
                }
            }
        } catch (SQLException ex) {
            if (ex.getMessage().contains("no such column:")) {
                try {
                    Bukkit.getConsoleSender().sendMessage("§c[IslandNPC] Error in NPC: " + rs.getString("id"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            ex.printStackTrace();
        }
        finally {
            try {
                if (ps != null) ps.close();
            }
            catch (SQLException ignored){}
        }

        return total;
    }

    public void shutdownPool(){
        try {
            Bukkit.getConsoleSender().sendMessage("§7[§bLitSellChest§7] §fShutting down thread pool");
            getPool().shutdownNow();
            boolean closed = getPool().awaitTermination(20, TimeUnit.SECONDS);
            if (!closed)
                Bukkit.getConsoleSender().sendMessage("§7[§bLitSellChest§7] §cShutting down thread pool failed");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static ExecutorService getPool() {
        return threadPool;
    }

}
