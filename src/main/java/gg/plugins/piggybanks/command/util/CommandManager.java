package gg.plugins.piggybanks.command.util;

import gg.plugins.piggybanks.PiggyBanks;
import gg.plugins.piggybanks.api.PiggyBank;
import gg.plugins.piggybanks.command.ReloadCommand;
import gg.plugins.piggybanks.config.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {

    private Map<String, Method> commands = new HashMap<>();

    public Map<String, Method> getCommands() {
        return commands;
    }

    private PiggyBanks plugin;

    public CommandManager(PiggyBanks plugin) {
        this.plugin = plugin;

        List<Class<?>> commandClasses = Arrays.asList(
                ReloadCommand.class
        );

        for (Class clazz : commandClasses) {
            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(Command.class)) continue; // make sure method is marked as an annotation

                if (method.getParameters().length != 3) {
                    plugin.getLogger().warning("Method " + method.toGenericString().replace("public static void ", "") + " annotated as command but parameters count != 2");
                    continue;
                }
                if (method.getParameters()[0].getType() != CommandSender.class && method.getParameters()[0].getType() != Player.class) {
                    plugin.getLogger().warning("Method " + method.toGenericString().replace("public static void ", "") + " annotated as command but parameter 1's type != CommandSender || Player");
                    continue;
                }
                if (method.getParameters()[2].getType() != String[].class) {
                    plugin.getLogger().warning("Method " + method.toGenericString().replace("public static void ", "") + " annotated as command but parameter 3's type != String[]");
                    continue;
                }

                Command annotation = method.getAnnotation(Command.class);
                for (String commandName : annotation.aliases()) commands.put(commandName.toLowerCase(), method);
            }
        }
    }

    boolean handle(CommandSender sender, String command, String[] args) {
        if (command == null) {
            Lang.MAIN_COMMAND.send(sender, Lang.PREFIX.asString(), plugin.getDescription().getName(), plugin.getDescription().getVersion(), plugin.getDescription().getAuthors().get(0));
            return true;
        }

        try {
            int amount = Integer.parseInt(command);

            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (plugin.getEcon().has(player, amount)) {
                    plugin.getEcon().withdrawPlayer(player, amount);
                    player.getInventory().addItem(PiggyBank.create(player.getName(), player.getUniqueId(), amount));
                    Lang.COMMAND_WITHDRAW.send(player, Lang.PREFIX.asString(), amount);
                } else {
                    Lang.INSUFFICIENT_FUNDS.send(player, Lang.PREFIX.asString());
                }
            } else {
                Lang.COMMAND_PLAYER_ONLY.send(sender, Lang.PREFIX.asString());
            }

            // is an integer!
            return true;
        } catch (NumberFormatException e) {
            // not an integer!
        }

        if (commands.containsKey(command.toLowerCase())) {
            try {
                Method commandMethod = commands.get(command.toLowerCase());
                Command commandAnnotation = commandMethod.getAnnotation(Command.class);

                if (!sender.hasPermission(commandAnnotation.permission()) && (sender instanceof Player)) {
                    Lang.COMMAND_NO_PERMISSION.send(sender, Lang.PREFIX.asString());
                    return true;
                }

                if (commandMethod.getParameters()[0].getType() == Player.class && !(sender instanceof Player)) {
                    Lang.COMMAND_PLAYER_ONLY.send(sender, Lang.PREFIX.asString());
                    return true;
                }

                commandMethod.invoke(null, sender, plugin, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            Lang.COMMAND_INVALID.send(sender, Lang.PREFIX.asString());
        }

        return true;
    }

}