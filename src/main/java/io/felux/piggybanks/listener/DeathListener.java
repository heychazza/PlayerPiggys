package io.felux.piggybanks.listener;

import io.felux.piggybanks.PiggyBanks;
import io.felux.piggybanks.api.PiggyBank;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class DeathListener implements Listener {

    private PiggyBanks plugin;

    public DeathListener(PiggyBanks plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPigDeath(EntityDeathEvent e) {
        if (!plugin.getConfig().getBoolean("drops.enabled", false)) return;
        if (e.getEntityType() == EntityType.PIG) {
            ThreadLocalRandom random = ThreadLocalRandom.current();

            if (random.nextInt(100) <= plugin.getConfig().getInt("drops.chance", 100))
                e.getDrops().add(PiggyBank.create("CONSOLE", UUID.randomUUID(), random.nextInt(plugin.getConfig().getInt("drops.money.min"), plugin.getConfig().getInt("drops.money.max"))));
        }
    }
}
