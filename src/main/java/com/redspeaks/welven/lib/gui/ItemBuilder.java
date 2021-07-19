package com.redspeaks.welven.lib.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemBuilder {

    public static ItemStack build(String name, Material type, String... lore) {
        ItemStack itemStack = new ItemStack(type);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore(Arrays.stream(lore).map(l -> ChatColor.translateAlternateColorCodes('&', l)).collect(Collectors.toList()));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack build(String name, Material type) {
        ItemStack itemStack = new ItemStack(type);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack plus() {
        ItemStack stick = new ItemStack(Material.WOODEN_SHOVEL, 1, (short) 7);
        ItemMeta meta = stick.getItemMeta();
        meta.setUnbreakable(true);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lClick to Create Character"));
        stick.setItemMeta(meta);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        return stick;
    }

    public static ItemStack blank() {
        ItemStack stick = new ItemStack(Material.WOODEN_SHOVEL, 1, (short) 2);
        ItemMeta meta = stick.getItemMeta();
        meta.setUnbreakable(true);
        meta.setDisplayName(" ");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        stick.setItemMeta(meta);
        return stick;
    }

    public static ItemStack custom(int damage) {
        ItemStack stick = new ItemStack(Material.WOODEN_SHOVEL, 1, (short) damage);
        ItemMeta meta = stick.getItemMeta();
        meta.setUnbreakable(true);
        meta.setDisplayName(" ");
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        stick.setItemMeta(meta);
        return stick;
    }
}

