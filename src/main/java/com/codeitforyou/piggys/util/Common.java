package com.codeitforyou.piggys.util;

import com.codeitforyou.piggys.Piggys;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Common {
    private static Piggys plugin = JavaPlugin.getPlugin(Piggys.class);

    public static final String isUpdated = "%%__SONGODA__%%";
    private static final String updateIdentifier = "%%__USER__%%";
    private static String updateTimestamp = "%%__TIMESTAMP__%%";

    public static int getVersion() {
        return Integer.parseInt(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].replace("v", "").replace("_", "").split("R", 2)[0]);
    }
}
