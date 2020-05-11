package com.codeitforyou.piggys.command;

import com.codeitforyou.lib.api.command.Command;
import com.codeitforyou.piggys.Piggys;
import com.codeitforyou.piggys.api.Piggy;
import com.codeitforyou.piggys.config.Lang;
import com.codeitforyou.piggys.util.ArgUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnstableApiUsage")
public class WithdrawCommand {

    private static final int cooldown = JavaPlugin.getPlugin(Piggys.class).getConfig().getInt("cooldown", 30);
    private static final Cache<UUID, Long> playerCooldowns = CacheBuilder.newBuilder()
            .expireAfterWrite(cooldown, TimeUnit.SECONDS)
            .maximumSize(500)
            .build();

    @Command(aliases = {}, about = "The main withdraw command.", permission = "cifypiggys.withdraw", usage = "<amount>")
    public static void execute(final Player player, final Piggys plugin, final String[] args) {
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
                Long cooldownTime = playerCooldowns.getIfPresent(player.getUniqueId());
                if (cooldownTime != null && !player.hasPermission("cifypiggys.cooldown.bypass")) {
                    // They have a cooldown!
                    Lang.COOLDOWN.send(player, Lang.PREFIX.asString(), TimeUnit.MILLISECONDS.toSeconds((cooldownTime - System.currentTimeMillis())));
                    return;
                }

                plugin.getEcon().withdrawPlayer(player, amount);
                player.getInventory().addItem(Piggy.create(player.getName(), player.getUniqueId(), amount));

                Lang.WITHDRAW_COMMAND.send(player, Lang.PREFIX.asString(), amount);

                if (cooldown != -1 && !player.hasPermission("cifypiggys.cooldown.bypass")) {
                    int cooldownDuration = player.hasPermission("cifypiggys.cooldown.half") ? cooldown / 2 : cooldown;
                    playerCooldowns.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(cooldownDuration));
                }

            } else {
                Lang.INSUFFICIENT_FUNDS.send(player, Lang.PREFIX.asString());
            }
        } else {
            Lang.INVALID_NUMBER.send(player, Lang.PREFIX.asString());
        }
    }
}
