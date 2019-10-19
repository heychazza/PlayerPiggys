package io.felux.piggybanks.util;

import io.felux.piggybanks.PiggyBanks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Common {
    private static PiggyBanks plugin = JavaPlugin.getPlugin(PiggyBanks.class);

    public static String translate(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private static String prefix = "[PB]";

    public static void loading(String object) {
        sendConsoleMessage(prefix + " Loading " + object + "..");
    }

    public static void sendMessage(String object) {
        sendConsoleMessage(prefix + " " + object);
    }
    public static void sendConsoleMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage(translate(msg));
    }

    public static void debug(String message) {
        if (plugin.getConfig().getBoolean("debug", false)) plugin.getLogger().info("[DEBUG] " + message);
    }

    public static int getVersion() {
        return Integer.parseInt(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].replace("v", "").replace("_", "").split("R", 2)[0]);
    }
}
