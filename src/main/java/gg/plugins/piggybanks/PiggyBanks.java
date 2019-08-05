package gg.plugins.piggybanks;

import gg.plugins.piggybanks.command.util.CommandExecutor;
import gg.plugins.piggybanks.command.util.CommandManager;
import gg.plugins.piggybanks.config.Config;
import gg.plugins.piggybanks.config.Lang;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PiggyBanks extends JavaPlugin {
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        handleReload();
        registerCommands();
        registerEvents();
        registerEconomy();
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
        Bukkit.getPluginManager().registerEvents(new PiggyListener(this), this);
    }

    public void handleReload() {
        this.reloadConfig();
        Lang.init(this);
    }

    private Economy econ = null;

    public Economy getEcon() {
        return econ;
    }

    private void registerEconomy() {
        if (!getServer().getPluginManager().isPluginEnabled("Vault")) {
            getLogger().warning("The plugin 'Vault' is required but isn't installed.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().warning("You don't have any economy plugin installed.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        econ = rsp.getProvider();

        if (econ == null) {
            getLogger().warning("An error occurred with Vault.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().warning("Successfully found Vault.");
    }

}
