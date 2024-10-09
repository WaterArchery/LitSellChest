package me.waterarchery.litsellchest.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.waterarchery.litsellchest.LitSellChest;
import org.bukkit.configuration.file.FileConfiguration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySQL extends Database {

    private final HikariConfig hikariConfig = new HikariConfig();
    private DataSource dataSource;

    public MySQL(LitSellChest instance) {
        super(instance);
    }

    public Connection getSQLConnection() {
        try {
            if(connection != null && !connection.isClosed()){
                return connection;
            }
            connection = dataSource.getConnection();
            return connection;
        } catch (SQLException ex) {
            LitSellChest.getInstance().getLibs().getLogger().log("MySQL exception on initialize");
        }
        return null;
    }

    @Override
    public void load() {

    }

    @Override
    public void initialize() {
        FileConfiguration config = instance.getConfig();

        hikariConfig.setDriverClassName("org.mariadb.jdbc.Driver");
        hikariConfig.setJdbcUrl(String.format("jdbc:mariadb://%s:%s/%s",
                config.getString("Database.MySQL.host"),
                config.getString("Database.MySQL.port"),
                config.getString("Database.MySQL.database")
        ));

        hikariConfig.setUsername(config.getString("Database.MySQL.user"));
        hikariConfig.setPassword(config.getString("Database.MySQL.password"));

        hikariConfig.addDataSourceProperty("useUnicode", "true");
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
        hikariConfig.setMaximumPoolSize(10);

        dataSource = new HikariDataSource(hikariConfig);
    }

}
