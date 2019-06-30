package gg.plugins.piggybanks;

import de.tr7zw.itemnbtapi.NBTAPI;
import gg.plugins.piggybanks.command.util.CommandExecutor;
import gg.plugins.piggybanks.command.util.CommandManager;
import gg.plugins.piggybanks.config.Config;
import gg.plugins.piggybanks.config.Lang;
import org.bukkit.plugin.java.JavaPlugin;

public class PiggyBanks extends JavaPlugin {
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        NBTAPI.setLogging(false);
        this.handleReload();
        this.registerCommands();
    }

    @Override
    public void onDisable() {

    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    private void registerCommands() {
        this.commandManager = new CommandManager(this);
        this.getCommand("levellingtools").setExecutor(new CommandExecutor(this));
        if (this.getCommand("levellingtools").getPlugin() != this) {
            this.getLogger().warning("/levellingtools command is being handled by plugin other than " + this.getDescription().getName() + ". You must use /levellingtools:levellingtools instead.");
        }
    }

    public void handleReload() {
        this.reloadConfig();
        Lang.init(new Config(this, "lang.yml"));
    }
}
