package com.codeitforyou.piggys.command;

import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.piggys.CIFYPiggys;
import com.codeitforyou.piggys.config.Lang;
import org.bukkit.command.CommandSender;

public class PiggyCommand {
    @Command(aliases = {}, about = "The main piggy command.")
    public static void execute(final CommandSender sender, final CIFYPiggys plugin, final String[] args) {
        Lang.MAIN_COMMAND.send(sender, Lang.PREFIX.asString(), plugin.getDescription().getName(), plugin.getDescription().getVersion(), plugin.getDescription().getAuthors().get(0));
    }
}
