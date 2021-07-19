package com.redspeaks.welven.lib.util;

import com.redspeaks.welven.Welven;
import com.redspeaks.welven.lib.Avatar;
import com.redspeaks.welven.lib.gear.Identification;
import com.redspeaks.welven.lib.gear.IdentificationType;
import com.redspeaks.welven.lib.items.ItemCache;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemCreator {

    public static ItemStack create(Avatar avatar, ItemCache cache) {
        ItemStack item = new ItemStack(cache.getType());
        if(cache.getDurability() > -1) {
            item.setDurability((short) cache.getDurability());
        }
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Welven.getUtil().translate(cache.getDisplayName()));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        List<String> lore = new ArrayList<>();
        if(avatar.getAvatarClass().equals(cache.getAvatarClass())) {
            lore.addAll(Arrays.asList("", "", "&a" + Symbol.CHECK.get() + " &7Class: &fWarrior", ""));
        } else {
            lore.addAll(Arrays.asList("", "", "&a" + Symbol.CROSS.get() + " &7Class: &fWarrior", ""));
        }
        lore.set(0, cache.getRarity());
        List<Identification> raw = cache.getList().stream().filter(i -> i.getType().isRaw()).collect(Collectors.toList());
        for(Identification r : raw) {
            if(r.getType() == IdentificationType.BASE_HEALTH) {
                lore.add("&6" + r.getValue() + " " + IdentificationType.BASE_HEALTH.getName());
            } else {
                lore.add("&6" + r.getValue() + " " + r.getType().getName());
            }
        }
        lore.add("");
        List<Identification> boosters = cache.getList().stream().filter(i -> !i.getType().isRaw()).collect(Collectors.toList());
        for(Identification r : boosters) {
            if(r.getType().getName().toLowerCase().startsWith("raw") || r.getType() == IdentificationType.HEALTH) {
                lore.add((r.getValue() > 0 ? "&a+" + r.getValue() + " " : "&c" + r.getValue() + "% ") + r.getType().getName());
            } else if (r.getType() == IdentificationType.LIFE_STEAL) {
                lore.add("&a+" + r.getValue() + "/4s " + r.getType().getName());
            } else if (r.getType() == IdentificationType.PHYSICAL_DEFENSE || r.getType() == IdentificationType.MAGIC_RESISTANCE) {
                lore.add("&a+" + r.getValue() + " " + r.getType().getName());
            }
            else {
                lore.add((r.getValue() > 0 ? "&a+" + r.getValue() + "% " : "&c" + r.getValue() + "% ") + r.getType().getName());
            }
        }

        lore.add("");
        lore.addAll(cache.getDescription());
        for(int i = 0; i < lore.size(); i++) {
            lore.set(i, Welven.getUtil().translate(lore.get(i)));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack create(ItemCache cache) {
        ItemStack item = new ItemStack(cache.getType());
        if(cache.getDurability() > -1) {
            item.setDurability((short) cache.getDurability());
        }
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Welven.getUtil().translate(cache.getDisplayName()));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        List<String> lore = new ArrayList<>();
        lore.addAll(Arrays.asList("", "", cache.getAvatarClass().getName(), ""));
        lore.set(0, cache.getRarity());
        List<Identification> raw = cache.getList().stream().filter(i -> i.getType().isRaw()).collect(Collectors.toList());
        for(Identification r : raw) {
            if(r.getType() == IdentificationType.BASE_HEALTH) {
                lore.add("&6" + r.getValue() + " " + IdentificationType.BASE_HEALTH.getName());
            } else {
                lore.add("&6" + r.getValue() + " " + r.getType().getName());
            }
        }
        lore.add("");
        List<Identification> boosters = cache.getList().stream().filter(i -> !i.getType().isRaw()).collect(Collectors.toList());
        for(Identification r : boosters) {
            if(r.getType().getName().toLowerCase().startsWith("raw") || r.getType() == IdentificationType.HEALTH) {
                lore.add((r.getValue() > 0 ? "&a+" + r.getValue() + " " : "&c" + r.getValue() + "% ") + r.getType().getName());
            } else if (r.getType() == IdentificationType.LIFE_STEAL) {
                lore.add("&a+" + r.getValue() + "/4s " + r.getType().getName());
            } else if (r.getType() == IdentificationType.PHYSICAL_DEFENSE || r.getType() == IdentificationType.MAGIC_RESISTANCE) {
                lore.add("&a+" + r.getValue() + " " + r.getType().getName());
            }
            else {
                lore.add((r.getValue() > 0 ? "&a+" + r.getValue() + "% " : "&c" + r.getValue() + "% ") + r.getType().getName());
            }
        }

        lore.add("");
        lore.addAll(cache.getDescription());
        for(int i = 0; i < lore.size(); i++) {
            lore.set(i, Welven.getUtil().translate(lore.get(i)));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        Welven.getUtil().setItemOriginId(item, UUID.randomUUID().toString());
        Welven.getUtil().setItemOriginName(item, cache.getName());
        return item;
    }
}
