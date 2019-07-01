package gg.plugins.piggybanks;

import de.tr7zw.itemnbtapi.NBTAPI;
import gg.plugins.piggybanks.command.util.CommandExecutor;
import gg.plugins.piggybanks.command.util.CommandManager;
import gg.plugins.piggybanks.config.Config;
import gg.plugins.piggybanks.config.Lang;
import gg.plugins.piggybanks.event.PiggyListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PiggyBanks extends JavaPlugin {
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        NBTAPI.setLogging(false);
        handleReload();
        registerCommands();
        registerEvents();
        if(!registerEconomy()) {
            getLogger().warning("The plugin 'Vault' isn't installed on the server.");
            getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {

    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    private void registerCommands() {
        this.commandManager = new CommandManager(this);
        this.getCommand("piggybanks").setExecutor(new CommandExecutor(this));
        if (this.getCommand("piggybanks").getPlugin() != this) {
            this.getLogger().warning("/piggybanks command is being handled by plugin other than " + this.getDescription().getName() + ". You must use /piggybanks:piggybanks instead.");
        }
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PiggyListener(), this);
    }

    public void handleReload() {
        this.reloadConfig();
        Lang.init(new Config(this, "lang.yml"));
    }

    private Economy econ = null;

    public Economy getEcon() {
        return econ;
    }

    private boolean registerEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

}
