package gg.plugins.piggybanks;

import de.tr7zw.itemnbtapi.NBTItem;
import gg.plugins.piggybanks.api.PiggyRedeemEvent;
import gg.plugins.piggybanks.config.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class PiggyListener implements Listener {

    private PiggyBanks plugin;

    public PiggyListener(PiggyBanks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPiggyUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (!Bukkit.getVersion().contains("1.8")) {
            if (e.getHand() == EquipmentSlot.OFF_HAND) {
                return; // off hand packet, ignore.
            }
        }

        NBTItem nbtItem = new NBTItem(e.hasItem() ? e.getItem() : new ItemStack(Material.AIR));

        if (nbtItem.getItem().getType() != Material.AIR && nbtItem.hasNBTData() && nbtItem.hasKey("created-by-name")) {
            UUID createdByUuid = UUID.fromString(nbtItem.getString("created-by-uuid"));
            String createdBy = nbtItem.getString("created-by-name").equalsIgnoreCase("!CONSOLE!") ? Lang.CONSOLE.asString() : Bukkit.getOfflinePlayer(createdByUuid).getName();
            OfflinePlayer owner = createdBy.equalsIgnoreCase("CONSOLE") ? null : Bukkit.getOfflinePlayer(UUID.fromString(nbtItem.getString("created-by-uuid")));

            int value = nbtItem.getInteger("balance");

            PiggyRedeemEvent piggyRedeemEvent = new PiggyRedeemEvent(owner, value);

            if (piggyRedeemEvent.isCancelled()) {
                return;
            }

            e.setCancelled(true);

            player.sendMessage("Created By: " + createdBy);
            player.sendMessage("Owner: " + owner);

            if (createdBy.equalsIgnoreCase(Lang.CONSOLE.asString()) || (owner != null && owner.getUniqueId() != createdByUuid)) {
                Lang.REDEEM_OTHER.send(player, Lang.PREFIX.asString(), value, createdBy);
            } else {
                Lang.REDEEM_SELF.send(player, Lang.PREFIX.asString(), value);
            }

            plugin.getEcon().depositPlayer(player, value);

            if (e.getItem().getAmount() > 1)
                e.getItem().setAmount(e.getItem().getAmount() - 1);
            else player.setItemInHand(null);
        }
    }
}
