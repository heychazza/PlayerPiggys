package com.codeitforyou.piggys.config;

import com.codeitforyou.piggys.Piggys;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;

public enum Lang {
    PREFIX("&8[&5Piggys&8]"),
    NO_PERMISSION("{0} &cYou don't have permissions to do that."),
    PLAYER_ONLY("{0} &cThe command or args specified can only be used by a player."),
    INVALID_COMMAND("{0} &7That command couldn't be found."),
    INVALID_PLAYER("{0} &7That player couldn't be found."),
    INVALID_NUMBER("{0} &7You need to specify a number."),
    INSUFFICIENT_FUNDS("{0} &7You do not have enough money to withdraw that."),

    MAIN_COMMAND("{0} &7Running &f{1} &7version &5{2} &7by &d{3}&7."),
    RELOAD_COMMAND("{0} &7Successfully reloaded the configuration file."),
    WITHDRAW_COMMAND("{0} &7You've moved &d{1} &7to a Piggy Bank&7."),
    GIVE_COMMAND("{0} &7You've given &d{1} &7a piggy bank worth &f${2}&7."),

    PIGGY_COMMAND_USAGE("{0} &7Usage: &d/piggys {1}&7."),
    WITHDRAW_COMMAND_USAGE("{0} &7Usage: &d/withdraw <amount>&7."),
    COMMAND_INVALID("{0} &7That command doesn't exist, use &f/piggys help&7."),

    PIGGY_BANK_NAME("&d{0}'s Piggy Bank"),
    PIGGY_BANK_LORE("&a", "&7Balance: &f${1}", "&d", "&7Right click to redeem!", "&c"),

    REDEEM_SELF("{0} &7You've deposited &d${1} &7from your Piggy Bank to your balance."),
    REDEEM_OTHER("{0} &7You've deposited &d${1} &7from &f{2} &7to your balance."),

    CONSOLE("Console"),

    OUT_OF_RANGE("{0} &7Please choose an amount between &d{1} &7and &d{2}&7.");

    private String message;
    private static FileConfiguration c;

    Lang(final String... def) {
        this.message = String.join("\n", def);
    }

    private String getMessage() {
        return this.message;
    }

    public static String format(String s, final Object... objects) {
        for (int i = 0; i < objects.length; ++i) {
            s = s.replace("{" + i + "}", String.valueOf(objects[i]));
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static boolean init(Piggys Piggys) {
        Lang.c = Piggys.getConfig();
        for (final Lang value : values()) {
            if (value.getMessage().split("\n").length == 1) {
                Lang.c.addDefault(value.getPath().toLowerCase(), value.getMessage());
            } else {
                Lang.c.addDefault(value.getPath().toLowerCase(), value.getMessage().split("\n"));
            }
        }
        Lang.c.options().copyDefaults(true);
        Piggys.saveConfig();
        return true;
    }

    public String getPath() {
        return "message." + this.name().toLowerCase().toLowerCase();
    }

    public void send(final Player player, final Object... args) {
        final String message = this.asString(args);
        Arrays.stream(message.split("\n")).forEach(player::sendMessage);
    }

    public void sendRaw(final Player player, final Object... args) {
        final String message = this.asString(args);
        Arrays.stream(message.split("\n")).forEach(player::sendRawMessage);
    }

    public void send(final CommandSender sender, final Object... args) {
        if (sender instanceof Player) {
            this.send((Player) sender, args);
        } else {
            Arrays.stream(this.asString(args).split("\n")).forEach(sender::sendMessage);
        }
    }

    public String asString(final Object... objects) {
        Optional<String> opt = Optional.empty();
        if (Lang.c.contains(this.getPath())) {
            if (Lang.c.isList(getPath())) {
                opt = Optional.of(String.join("\n", Lang.c.getStringList(this.getPath())));
            } else if (Lang.c.isString(this.getPath())) {
                opt = Optional.ofNullable(Lang.c.getString(this.getPath()));
            }
        }
        return this.format(opt.orElse(this.message), objects);
    }

}
