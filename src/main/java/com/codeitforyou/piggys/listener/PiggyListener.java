package com.codeitforyou.piggys.listener;

import com.codeitforyou.piggys.Piggys;
import com.codeitforyou.piggys.api.PiggyRedeemEvent;
import com.codeitforyou.piggys.api.PiggySlot;
import com.codeitforyou.piggys.config.Lang;
import com.codeitforyou.piggys.nbt.NBT;
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

@SuppressWarnings("UnstableApiUsage")
public class PiggyListener implements Listener {

    private final Piggys plugin;

    public PiggyListener(Piggys plugin) {
        this.plugin = plugin;
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
