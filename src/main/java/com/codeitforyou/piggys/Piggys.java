package com.codeitforyou.piggys;

import com.codeitforyou.lib.api.command.CommandManager;
import com.codeitforyou.piggys.command.PiggyCommand;
import com.codeitforyou.piggys.command.WithdrawCommand;
import com.codeitforyou.piggys.command.sub.GiveCommand;
import com.codeitforyou.piggys.command.sub.ReloadCommand;
import com.codeitforyou.piggys.config.Lang;
import com.codeitforyou.piggys.listener.DeathListener;
import com.codeitforyou.piggys.listener.PiggyListener;
import com.codeitforyou.piggys.maven.LibraryLoader;
import com.codeitforyou.piggys.maven.MavenLibrary;
import com.codeitforyou.piggys.maven.Repository;
import com.codeitforyou.piggys.util.Metrics;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;

@MavenLibrary(groupId = "com.github.DRE2N.HeadLib", artifactId = "headlib", version = "30ceaa7f3a", repo = @Repository(url = "https://jitpack.io"))
public class Piggys extends JavaPlugin {
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerEconomy();
        LibraryLoader.loadAll(Piggys.class);
        new PiggyListener(this);
        new DeathListener(this);

        handleReload();
        registerCommands();
        new Metrics(this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        getServer().getScheduler().cancelTasks(this);
    }

    private void registerCommands() {
        commandManager = new CommandManager(Arrays.asList(GiveCommand.class, ReloadCommand.class), "cifypiggys", this);
        commandManager.setMainCommand(PiggyCommand.class);

        Arrays.asList("piggybanks", "pbanks", "piggy", "piggys").forEach(commandManager::addAlias);

        commandManager.register();
        commandManager.getLocale().setNoPermission(Lang.NO_PERMISSION.asString(Lang.PREFIX.asString()));
        commandManager.getLocale().setPlayerOnly(Lang.PLAYER_ONLY.asString(Lang.PREFIX.asString()));
        commandManager.getLocale().setUnknownCommand(Lang.COMMAND_INVALID.asString(Lang.PREFIX.asString()));
        commandManager.getLocale().setUsage(Lang.PIGGY_COMMAND_USAGE.asString(Lang.PREFIX.asString(), "{usage}"));

        final CommandManager withdrawManager = new CommandManager(Collections.emptyList(), "withdraw", this);
        withdrawManager.setMainCommand(WithdrawCommand.class);
        withdrawManager.setMainCommandArgs(true);
        withdrawManager.addAlias("deposit");
        withdrawManager.register();
    }

    public void handleReload() {
        reloadConfig();
        Lang.init(this);
    }

    private Economy econ = null;

    public Economy getEcon() {
        return econ;
    }

    private void registerEconomy() {
        if (!getServer().getPluginManager().isPluginEnabled("Vault")) {
            throw new RuntimeException("Vault isn't installed");
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            throw new RuntimeException("No economy plugin installed.");
        }
        econ = rsp.getProvider();

        if (econ == null) {
            throw new RuntimeException("An error occurred when initialising Vault.");
        }
    }

}
