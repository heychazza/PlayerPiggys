package io.felux.piggybanks.command;

import io.felux.piggybanks.PiggyBanks;
import io.felux.piggybanks.command.util.Command;
import io.felux.piggybanks.config.Lang;
import org.bukkit.command.CommandSender;

public class ReloadCommand {
    @Command(aliases = {"reload"}, about = "Reload the plugin.", permission = "piggybanks.reload", usage = "reload")
    public static void execute(final CommandSender sender, final PiggyBanks plugin, final String[] args) {
        plugin.handleReload();
        Lang.RELOAD_COMMAND.send(sender, Lang.PREFIX.asString());
    }
}
