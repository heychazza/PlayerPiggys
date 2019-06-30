package gg.plugins.piggybanks.config;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public enum Lang {
    PREFIX("&8[&5PiggyBanks&8]"),
    COMMAND_NO_PERMISSION("{0} &cYou don't have permissions to do that."),
    COMMAND_PLAYER_ONLY("{0} &cThe command or args specified can only be used by a player."),
    COMMAND_INVALID("{0} &7That command doesn't exist, use &d/pbanks help&c."),
    COMMAND_UNKNOWN("{0} &7That player couldn't be found."),
    MAIN_COMMAND("{0} &7Running &f{1} &7version &5{2} &7by &d{3}&7."),
    HELP_COMMAND_HEADER("", "{0} &7Listing Commands:", "&7"),
    HELP_COMMAND_FORMAT(" &d/pbanks {1} &8- &7{2}"),
    HELP_COMMAND_FOOTER("", "{0} &7Total of &f{1} &7commands."),
    RELOAD_COMMAND("{0} &7Successfully reloaded the configuration file."),
    ;

    private String message;
    private static Config config;
    private static FileConfiguration c;

    Lang(String... def) {
        this.message = String.join("\n", (CharSequence[]) def);
    }

    private String getMessage() {
        return this.message;
    }

    public String getPath() {
        return this.name();
    }

    private String format(String s, final Object... objects) {
        for (int i = 0; i < objects.length; ++i) {
            s = s.replace("{" + i + "}", String.valueOf(objects[i]));
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String asString(final Object... objects) {
        Optional<String> opt = Optional.empty();
        if (c.contains(this.name())) {
            if (c.isList(this.name())) {
                opt = Optional.ofNullable(c.getStringList(this.name()).stream().collect(Collectors.joining("\n")));
            } else if (c.isString(this.name())) {
                opt = Optional.ofNullable(c.getString(this.name()));
            }
        }
        return this.format(opt.orElse(this.message), objects);
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

    public static Config getConfig() {
        return config;
    }

    public static boolean init(final Config wrapper) {
        wrapper.loadConfig();
        if (wrapper.getConfig() == null) {
            return false;
        }
        config = wrapper;
        c = wrapper.getConfig();
        for (final Lang value : values()) {
            if (value.getMessage().split("\n").length == 1) {
                c.addDefault(value.getPath(), value.getMessage());
            } else {
                c.addDefault(value.getPath(), value.getMessage().split("\n"));
            }
        }
        c.options().copyDefaults(true);
        wrapper.saveConfig();
        return true;
    }

}
