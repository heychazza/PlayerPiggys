package gg.plugins.piggybanks.event;

import de.tr7zw.itemnbtapi.NBTItem;
import gg.plugins.piggybanks.PiggyBanks;
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

        if (nbtItem.getItem().getType() != Material.AIR && nbtItem.hasNBTData() && nbtItem.hasKey("created-by")) {
            OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(nbtItem.getString("created-by")));
            int value = nbtItem.getInteger("balance");

            e.setCancelled(true);

            if (player.getUniqueId() == owner.getUniqueId()) {
                Lang.REDEEM_SELF.send(player, Lang.PREFIX.asString(), value);
            } else {
                Lang.REDEEM_OTHER.send(player, Lang.PREFIX.asString(), owner.getName(), value);
            }

            plugin.getEcon().depositPlayer(player, value);

            if(e.getItem().getAmount() > 1)
                e.getItem().setAmount(e.getItem().getAmount() - 1);
            else player.setItemInHand(null);

        }
    }
}
