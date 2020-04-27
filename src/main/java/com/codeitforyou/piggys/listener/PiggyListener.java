package com.codeitforyou.piggys.listener;

import com.codeitforyou.piggys.Piggys;
import com.codeitforyou.piggys.api.PiggyRedeemEvent;
import com.codeitforyou.piggys.api.PiggySlot;
import com.codeitforyou.piggys.config.Lang;
import com.codeitforyou.piggys.nbt.NBT;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnstableApiUsage")
public class PiggyListener implements Listener {

    private final Piggys plugin;
    private final Cache<UUID, Long> playerCooldowns;
    final int cooldown;

    public PiggyListener(Piggys plugin) {
        this.plugin = plugin;
        this.cooldown = plugin.getConfig().getInt("cooldown", 30);
        this.playerCooldowns = CacheBuilder.newBuilder()
                .expireAfterWrite(cooldown, TimeUnit.SECONDS)
                .maximumSize(500)
                .build();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPiggyUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack item;

        PiggySlot piggySlot = PiggySlot.MAIN_HAND;
        if (!Bukkit.getVersion().contains("1.8")) {
            if (e.getHand() == EquipmentSlot.OFF_HAND)
                piggySlot = PiggySlot.OFF_HAND;
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            item = e.hasItem() ? e.getItem() : null;

            if (item == null) return;
            NBT nbtItem = NBT.get(item);

            if (nbtItem != null && nbtItem.hasNBTData() && nbtItem.hasKey("created-by-name")) {
                UUID createdByUuid = UUID.fromString(nbtItem.getString("created-by-uuid"));
                UUID createdBy = nbtItem.getString("created-by-name").equalsIgnoreCase("!CONSOLE!") ? null : createdByUuid;

                long value = nbtItem.getLong("balance");

                e.setCancelled(true);
                Long cooldownTime = playerCooldowns.getIfPresent(player.getUniqueId());
                if (cooldownTime != null) {
                    // They have a cooldown!
                    Lang.COOLDOWN.send(player, Lang.PREFIX.asString(), TimeUnit.MILLISECONDS.toSeconds((cooldownTime - System.currentTimeMillis())));
                    return;
                }

                PiggyRedeemEvent piggyRedeemEvent = new PiggyRedeemEvent(player, createdBy, value, item, piggySlot);
                Bukkit.getPluginManager().callEvent(piggyRedeemEvent);
            }
        }
    }

    @EventHandler
    public void onPiggyUse(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        ItemStack item = e.getItemInHand();

        PiggySlot piggySlot = PiggySlot.MAIN_HAND;
        if (!Bukkit.getVersion().contains("1.8")) {
            if (e.getHand() == EquipmentSlot.OFF_HAND)
                piggySlot = PiggySlot.OFF_HAND;
        }

        if (item.getType() == Material.AIR) return;
        NBT nbtItem = NBT.get(item);
        if (nbtItem != null && nbtItem.hasNBTData() && nbtItem.hasKey("created-by-name")) {
            UUID createdByUuid = UUID.fromString(nbtItem.getString("created-by-uuid"));
            UUID createdBy = nbtItem.getString("created-by-name").equalsIgnoreCase("!CONSOLE!") ? null : createdByUuid;
            long value = nbtItem.getLong("balance");

            e.setCancelled(true);
            Long cooldownTime = playerCooldowns.getIfPresent(player.getUniqueId());
            if (cooldownTime != null) {
                // They have a cooldown!
                Lang.COOLDOWN.send(player, Lang.PREFIX.asString(), TimeUnit.MILLISECONDS.toSeconds((cooldownTime - System.currentTimeMillis())));
                return;
            }

            PiggyRedeemEvent piggyRedeemEvent = new PiggyRedeemEvent(player, createdBy, value, item, piggySlot);
            Bukkit.getPluginManager().callEvent(piggyRedeemEvent);
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(ignoreCancelled = true)
    public void onPiggyRedeem(PiggyRedeemEvent e) {
        Player player = e.getPlayer();
        UUID createdBy = e.getOwnedBy();
        Long value = e.getAmount();
        ItemStack piggyItem = e.getItem();

        if (createdBy == null) {
            // Console
            Lang.REDEEM_OTHER.send(player, Lang.PREFIX.asString(), value, Lang.CONSOLE.asString());
        } else if (player.getUniqueId().equals(createdBy)) {
            Lang.REDEEM_SELF.send(player, Lang.PREFIX.asString(), value);
        } else {
            Lang.REDEEM_OTHER.send(player, Lang.PREFIX.asString(), value, Bukkit.getOfflinePlayer(createdBy).getName());
        }

        plugin.getEcon().depositPlayer(player, value);

        if (cooldown != -1 && !player.hasPermission("cifypiggys.cooldown.bypass")) {
            int cooldownDuration = player.hasPermission("cifypiggys.cooldown.half") ? cooldown / 2 : cooldown;
            playerCooldowns.put(player.getUniqueId(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(cooldownDuration));
        }

        if (e.getSlot() == PiggySlot.MAIN_HAND) {
            if (piggyItem.getAmount() == 1) player.setItemInHand(null);
            else player.getItemInHand().setAmount(piggyItem.getAmount() - 1);
        } else {
            if (piggyItem.getAmount() == 1) player.getInventory().setItemInOffHand(null);
            else player.getInventory().getItemInOffHand().setAmount(piggyItem.getAmount() - 1);
        }

        if (player.getItemInHand().getAmount() > 1)
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        else player.setItemInHand(null);
    }
}
