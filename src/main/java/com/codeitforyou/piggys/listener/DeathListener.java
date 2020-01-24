package com.codeitforyou.piggys.listener;

import com.codeitforyou.piggys.Piggys;
import com.codeitforyou.piggys.api.Piggy;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class DeathListener implements Listener {

    private Piggys plugin;

    public DeathListener(Piggys plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPigDeath(EntityDeathEvent e) {
        if (!plugin.getConfig().getBoolean("drops.enabled", false)) return;
        if (e.getEntityType() == EntityType.PIG) {
            ThreadLocalRandom random = ThreadLocalRandom.current();

            if (random.nextInt(100) <= plugin.getConfig().getInt("drops.chance", 100))
                e.getDrops().add(Piggy.create("CONSOLE", UUID.randomUUID(), random.nextInt(plugin.getConfig().getInt("drops.money.min"), plugin.getConfig().getInt("drops.money.max"))));
        }
    }
}
