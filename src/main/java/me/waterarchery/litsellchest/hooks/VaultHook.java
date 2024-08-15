package me.waterarchery.litsellchest.hooks;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    private Economy econ = null;

    private static VaultHook instance = null;

    public static synchronized VaultHook getInstance() {
        if (instance == null)
            instance = new VaultHook();

        return instance;
    }

    private VaultHook() { }

    public boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) return false;

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;

        econ = rsp.getProvider();

        return true;
    }

    public Economy getEcon() { return econ; }

}
