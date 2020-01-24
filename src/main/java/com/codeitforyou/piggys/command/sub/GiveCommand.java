package com.codeitforyou.piggys.command.sub;

import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.piggys.Piggys;
import com.codeitforyou.piggys.api.Piggy;
import com.codeitforyou.piggys.config.Lang;
import com.codeitforyou.piggys.util.ArgUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GiveCommand {
    @Command(aliases = {"give"}, about = "Give player a piggy bank.", permission = "cifypiggys.give", usage = "give <player> <amount>", requiredArgs = 2)
    public static void execute(final CommandSender sender, final Piggys plugin, final String[] args) {
        Player target = Bukkit.getPlayer(args[0]);
        String createdByName = sender instanceof Player ? sender.getName() : "CONSOLE";
        UUID createdByUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : UUID.randomUUID();

        if (target == null) {
            Lang.INVALID_PLAYER.send(sender, Lang.PREFIX.asString());
            return;
        }

        if (!ArgUtil.isLong(args[1])) {
            Lang.INVALID_NUMBER.send(sender, Lang.PREFIX.asString());
            return;
        }

        int amount = Integer.parseInt(args[1]);

        target.getInventory().addItem(Piggy.create(createdByName, createdByUuid, amount));
        Lang.GIVE_COMMAND.send(sender, Lang.PREFIX.asString(), target.getName(), amount);
    }
}
