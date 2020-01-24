package com.codeitforyou.piggys.command.sub;

import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.piggys.Piggys;
import com.codeitforyou.piggys.config.Lang;
import org.bukkit.command.CommandSender;

public class ReloadCommand {
    @Command(aliases = {"reload"}, about = "Reload the plugin.", permission = "cifypiggys.reload", usage = "reload")
    public static void execute(final CommandSender sender, final Piggys plugin, final String[] args) {
        plugin.handleReload();
        Lang.RELOAD_COMMAND.send(sender, Lang.PREFIX.asString());
    }
}
