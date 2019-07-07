package gg.plugins.piggybanks.command;

import gg.plugins.piggybanks.PiggyBanks;
import gg.plugins.piggybanks.api.PiggyBank;
import gg.plugins.piggybanks.command.util.Command;
import gg.plugins.piggybanks.config.Lang;
import gg.plugins.piggybanks.util.ArgUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GiveCommand {
    @Command(aliases = {"give"}, about = "Give player a piggy bank.", permission = "piggybanks.give", usage = "give <player> <amount>")
    public static void execute(final CommandSender sender, final PiggyBanks plugin, final String[] args) {
        if(args.length == 2) {

            Player target = Bukkit.getPlayer(args[0]);
            String createdByName = sender instanceof Player ? sender.getName() : "!CONSOLE!";
            UUID createdByUuid = sender instanceof Player ? ((Player) sender).getUniqueId() : UUID.randomUUID();

            if(target == null) {
                Lang.INVALID_PLAYER.send(sender, Lang.PREFIX.asString());
                return;
            }

            if(!ArgUtil.isInt(args[1])) {
                Lang.INVALID_NUMBER.send(sender, Lang.PREFIX.asString());
                return;
            }

            int amount = Integer.valueOf(args[1]);

            target.getInventory().addItem(PiggyBank.create(createdByName, createdByUuid, amount));
            Lang.GIVE_COMMAND.send(sender, Lang.PREFIX.asString(), target.getName(), amount);
        }
    }
}
