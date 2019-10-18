package io.felux.piggybanks.api;

import de.erethon.headlib.HeadLib;
import io.felux.piggybanks.config.Lang;
import io.felux.piggybanks.nbt.NBT;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class PiggyBank {

    public static ItemStack create(String name, UUID uuid, int amount) {
        String altName = name;
        if (name.equalsIgnoreCase("CONSOLE")) {
            name = Lang.CONSOLE.asString();
            altName = "!CONSOLE!";
            uuid = UUID.randomUUID();
        }

        Material material = Integer.valueOf(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].replace("v", "").replace("_", "").split("R", 2)[0]) >= 114 ? Material.getMaterial("LEGACY_SKULL_ITEM") : Material.getMaterial("SKULL_ITEM");
        ItemStack is = new ItemStack(material, 1, (short) 3);
        SkullMeta sm = (SkullMeta) is.getItemMeta();
        sm.setDisplayName(Lang.PIGGY_BANK_NAME.asString(name));
        sm.setLore(new ArrayList<>(Arrays.asList(Lang.PIGGY_BANK_LORE.asString(name, amount).split("\n"))));
        sm.setOwner(altName);
        is.setItemMeta(sm);

        NBT nbtItem = NBT.get(is);
        if (nbtItem != null) {
            nbtItem.setString("created-by-name", altName);
            nbtItem.setString("created-by-uuid", uuid.toString());
            nbtItem.setInt("balance", amount);
        }

        String texture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTk4ZGY0MmY0NzdmMjEzZmY1ZTlkN2ZhNWE0Y2M0YTY5ZjIwZDljZWYyYjkwYzRhZTRmMjliZDE3Mjg3YjUifX19";
        return HeadLib.setSkullOwner(nbtItem.apply(is), uuid, texture);
    }
}