package com.codeitforyou.piggys.util;

import com.codeitforyou.piggys.CIFYPiggys;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Common {
    private static CIFYPiggys plugin = JavaPlugin.getPlugin(CIFYPiggys.class);

    public static int getVersion() {
        return Integer.parseInt(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].replace("v", "").replace("_", "").split("R", 2)[0]);
    }
}
