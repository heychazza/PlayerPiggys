package io.felux.piggybanks;

import io.felux.piggybanks.command.util.CommandExecutor;
import io.felux.piggybanks.command.util.CommandManager;
import io.felux.piggybanks.config.Lang;
import io.felux.piggybanks.listener.DeathListener;
import io.felux.piggybanks.listener.PiggyListener;
import io.felux.piggybanks.maven.LibraryLoader;
import io.felux.piggybanks.maven.MavenLibrary;
import io.felux.piggybanks.maven.Repository;
import io.felux.piggybanks.util.Common;
import io.felux.piggybanks.util.Metrics;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@MavenLibrary(groupId = "com.github.DRE2N.headlib", artifactId = "headlib-core", version = "30ceaa7f3a", repo = @Repository(url = "https://jitpack.io"))
public class PiggyBanks extends JavaPlugin {
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        saveDefaultConfig();
        getBanner();

        Common.loading("libraries");
        registerEconomy();
        LibraryLoader.loadAll(PiggyBanks.class);

        Common.loading("events");
        new PiggyListener(this);
        new DeathListener(this);

        handleReload();

        Common.loading("commands");
        registerCommands();

        Common.loading("metrics");
        new Metrics(this);

        Common.sendConsoleMessage(" ");
        getLogger().info("Successfully enabled in " + (System.currentTimeMillis() - start) + "ms.");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        getServer().getScheduler().cancelTasks(this);
    }

    private void getBanner() {
        Common.sendConsoleMessage("&b ");
        Common.sendConsoleMessage("&b    ___  ___ ");
        Common.sendConsoleMessage("&b   / _ \\/ _ )");
        Common.sendConsoleMessage("&b  / ___/ _  |" + "  &7" + getDescription().getName() + " v" + getDescription().getVersion());
        Common.sendConsoleMessage("&b /_/  /____/ " + "  &7Running on Bukkit - " + getServer().getName());
        Common.sendConsoleMessage("&b ");
        Common.sendMessage("Created by Felux.io Development.");
        Common.sendConsoleMessage("&b ");
    }


    public CommandManager getCommandManager() {
        return commandManager;
    }

    private void registerCommands() {
        commandManager = new CommandManager(this);
        getCommand("piggybanks").setExecutor(new CommandExecutor(this));
        if (getCommand("piggybanks").getPlugin() != this) {
            getLogger().warning("/piggybanks command is being handled by plugin other than " + getDescription().getName() + ". You must use /piggybanks:piggybanks instead.");
        }
    }

    public void handleReload() {
        reloadConfig();

        Common.loading("config");
        Lang.init(this);
    }

    private Economy econ = null;

    public Economy getEcon() {
        return econ;
    }

    private void registerEconomy() {
        if (!getServer().getPluginManager().isPluginEnabled("Vault")) {
            Common.sendConsoleMessage("[PB] Vault isn't installed");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().warning("[PB] No economy plugin installed.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        econ = rsp.getProvider();

        if (econ == null) {
            getLogger().warning("An error occurred with Vault.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
    }

}
