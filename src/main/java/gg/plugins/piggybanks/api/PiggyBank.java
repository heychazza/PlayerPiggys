package gg.plugins.piggybanks.api;

import de.erethon.headlib.HeadLib;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PiggyBank {

    public static ItemStack create(Player player, int amount) {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta sm = (SkullMeta) is.getItemMeta();
        sm.setDisplayName(ChatColor.LIGHT_PURPLE + player.getName() + "'s Piggy Bank");
        sm.setOwner(player.getName());
        is.setItemMeta(sm);

        String texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTk4ZGY0MmY0NzdmMjEzZmY1ZTlkN2ZhNWE0Y2M0YTY5ZjIwZDljZWYyYjkwYzRhZTRmMjliZDE3Mjg3YjUifX19";
        return HeadLib.setSkullOwner(is, player.getUniqueId(), texture);
    }
}
