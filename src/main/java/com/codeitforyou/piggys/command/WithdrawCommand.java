package com.codeitforyou.piggys.command;

import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.piggys.CIFYPiggys;
import com.codeitforyou.piggys.api.Piggy;
import com.codeitforyou.piggys.config.Lang;
import com.codeitforyou.piggys.util.ArgUtil;
import org.bukkit.entity.Player;

public class WithdrawCommand {
    @Command(aliases = {}, about = "The main withdraw command.", permission = "cifypiggys.withdraw", usage = "<amount>")
    public static void execute(final Player player, final CIFYPiggys plugin, final String[] args) {
        if (args.length < 1) {
            Lang.WITHDRAW_COMMAND_USAGE.send(player, Lang.PREFIX.asString());
            return;
        }

        if (ArgUtil.isLong(args[0])) {
            long amount = Long.parseLong(args[0]);

            if (plugin.getEcon().has(player, amount)) {
                if (amount < plugin.getConfig().getLong("settings.min", 1) || amount > plugin.getConfig().getLong("settings.max", 50)) {
                    Lang.OUT_OF_RANGE.send(player, Lang.PREFIX.asString(), plugin.getConfig().getLong("settings.min"), plugin.getConfig().getLong("settings.max"));
                    return;
                }
                plugin.getEcon().withdrawPlayer(player, amount);
                player.getInventory().addItem(Piggy.create(player.getName(), player.getUniqueId(), amount));
                Lang.WITHDRAW_COMMAND.send(player, Lang.PREFIX.asString(), amount);
            } else {
                Lang.INSUFFICIENT_FUNDS.send(player, Lang.PREFIX.asString());
            }
        } else {
            Lang.INVALID_NUMBER.send(player, Lang.PREFIX.asString());
        }
    }
}
