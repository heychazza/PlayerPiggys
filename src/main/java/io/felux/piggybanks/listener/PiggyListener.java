package io.felux.piggybanks.listener;

import io.felux.piggybanks.PiggyBanks;
import io.felux.piggybanks.api.PiggyRedeemEvent;
import io.felux.piggybanks.config.Lang;
import io.felux.piggybanks.nbt.NBT;
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

public class PiggyListener implements Listener {

    private PiggyBanks plugin;

    public PiggyListener(PiggyBanks plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPiggyUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (!Bukkit.getVersion().contains("1.8")) {
            if (e.getHand() == EquipmentSlot.OFF_HAND) {
                return; // off hand packet, ignore.
            }
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = e.hasItem() ? e.getItem() : null;

            if (item == null) return;
            NBT nbtItem = NBT.get(item);

            if (nbtItem != null && nbtItem.hasNBTData() && nbtItem.hasKey("created-by-name")) {
                UUID createdByUuid = UUID.fromString(nbtItem.getString("created-by-uuid"));

                UUID createdBy = nbtItem.getString("created-by-name").equalsIgnoreCase("!CONSOLE!") ? null : createdByUuid;

                int value = nbtItem.getInt("balance");

                e.setCancelled(true);
                PiggyRedeemEvent piggyRedeemEvent = new PiggyRedeemEvent(player, createdBy, value);
                Bukkit.getPluginManager().callEvent(piggyRedeemEvent);
            }
        }
    }

    @EventHandler
    public void onPiggyUse(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        ItemStack item = e.getItemInHand();

        if (item.getType() == Material.AIR) return;
        NBT nbtItem = NBT.get(item);
        if (nbtItem != null && nbtItem.hasNBTData() && nbtItem.hasKey("created-by-name")) {
            UUID createdByUuid = UUID.fromString(nbtItem.getString("created-by-uuid"));

            UUID createdBy = nbtItem.getString("created-by-name").equalsIgnoreCase("!CONSOLE!") ? null : createdByUuid;

            int value = nbtItem.getInt("balance");

            e.setCancelled(true);
            PiggyRedeemEvent piggyRedeemEvent = new PiggyRedeemEvent(player, createdBy, value);
            Bukkit.getPluginManager().callEvent(piggyRedeemEvent);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPiggyRedeem(PiggyRedeemEvent e) {
        Player player = e.getPlayer();
        UUID createdBy = e.getOwnedBy();
        int value = e.getAmount();

        if (createdBy == null) {
            Lang.REDEEM_OTHER.send(player, Lang.PREFIX.asString(), value, Lang.CONSOLE.asString());
        } else if (player.getUniqueId() != createdBy) {
            Lang.REDEEM_OTHER.send(player, Lang.PREFIX.asString(), value, Bukkit.getOfflinePlayer(createdBy).getName());
        } else {
            Lang.REDEEM_SELF.send(player, Lang.PREFIX.asString(), value);
        }
        plugin.getEcon().depositPlayer(player, value);

        if (player.getItemInHand().getAmount() > 1)
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        else player.setItemInHand(null);
    }
}
