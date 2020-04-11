package com.codeitforyou.piggys.api;

import com.codeitforyou.lib.api.xseries.XMaterial;
import com.codeitforyou.piggys.Piggys;
import com.codeitforyou.piggys.config.Lang;
import com.codeitforyou.piggys.nbt.NBT;
import de.erethon.headlib.HeadLib;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Piggy {

    private static Piggys plugin = (Piggys) JavaPlugin.getProvidingPlugin(Piggys.class);

    public static ItemStack create(String name, UUID uuid, long amount) {
        String altName = name;
        if (name.equalsIgnoreCase("CONSOLE")) {
            name = Lang.CONSOLE.asString();
            altName = "!CONSOLE!";
            uuid = UUID.randomUUID();
        }

        String withdrawItem = plugin.getConfig().getString("settings.item", "SKULL_ITEM").toUpperCase();
        Material material = XMaterial.valueOf(withdrawItem).parseMaterial();

        if (material == null) material = Material.valueOf("SKULL_ITEM");

        ItemStack is;
        if (material.name().contains("SKULL_ITEM")) {
            is = new ItemStack(material, 1, (short) 3);
            SkullMeta sm = (SkullMeta) is.getItemMeta();
            sm.setDisplayName(Lang.PIGGY_BANK_NAME.asString(name, amount));
            sm.setLore(new ArrayList<>(Arrays.asList(Lang.PIGGY_BANK_LORE.asString(name, amount).split("\n"))));
            sm.setOwner(altName);
            is.setItemMeta(sm);
        } else {
            is = new ItemStack(material, 1);
            ItemMeta sm = is.getItemMeta();
            sm.setDisplayName(Lang.PIGGY_BANK_NAME.asString(name, amount));
            sm.setLore(new ArrayList<>(Arrays.asList(Lang.PIGGY_BANK_LORE.asString(name, amount).split("\n"))));
            is.setItemMeta(sm);
        }

        NBT nbtItem = NBT.get(is);
        if (nbtItem != null) {
            nbtItem.setString("created-by-name", altName);
            nbtItem.setString("created-by-uuid", uuid.toString());
            nbtItem.setLong("balance", amount);
        }

        String texture = plugin.getConfig().getString("skull", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTk4ZGY0MmY0NzdmMjEzZmY1ZTlkN2ZhNWE0Y2M0YTY5ZjIwZDljZWYyYjkwYzRhZTRmMjliZDE3Mjg3YjUifX19");
        if (nbtItem != null) {
            return HeadLib.setSkullOwner(nbtItem.apply(is), uuid, texture);
        }

        return null;
    }
}
