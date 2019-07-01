package gg.plugins.piggybanks.api;

import de.erethon.headlib.HeadLib;
import de.tr7zw.itemnbtapi.NBTItem;
import gg.plugins.piggybanks.config.Lang;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class PiggyBank {

    public static ItemStack create(Player player, int amount) {
        ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta sm = (SkullMeta) is.getItemMeta();
        sm.setDisplayName(Lang.PIGGY_BANK_NAME.asString(player.getName()));
        sm.setLore(new ArrayList<>(Arrays.asList(Lang.PIGGY_BANK_LORE.asString(player.getName(), amount).split("\n"))));
        sm.setOwner(player.getName());
        is.setItemMeta(sm);

        NBTItem nbtItem = new NBTItem(is);
        nbtItem.setString("pbanks-owner", player.getUniqueId().toString());
        nbtItem.setInteger("pbanks-value", amount);

        String texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTk4ZGY0MmY0NzdmMjEzZmY1ZTlkN2ZhNWE0Y2M0YTY5ZjIwZDljZWYyYjkwYzRhZTRmMjliZDE3Mjg3YjUifX19";
        return HeadLib.setSkullOwner(nbtItem.getItem(), player.getUniqueId(), texture);
    }
}
