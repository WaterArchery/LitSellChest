package me.waterarchery.litsellchest.database;

import me.waterarchery.litsellchest.LitSellChest;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class SQLite extends Database {

    String dbname;

    public SQLite(LitSellChest instance){
        super(instance);
        dbname = "database";
    }

    public Connection getSQLConnection() {
        File dataFolder = new File(instance.getDataFolder(), dbname + ".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                instance.getLogger().log(Level.SEVERE, "File write error: " + dbname + ".db");
            }
        }
        try {
            if(connection != null && !connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            instance.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            instance.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    @Override
    public void initialize() {
        File dataFolder = new File(instance.getDataFolder().toString(), "database.db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                LitSellChest.getInstance().getLibs().getLogger().log("File write error: database.db");
            }
        }
    }

}
