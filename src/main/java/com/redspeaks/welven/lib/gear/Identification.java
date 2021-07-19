package com.redspeaks.welven.lib.gear;

import com.redspeaks.welven.Welven;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Identification {

    public static List<Identification> get(Player player) {
        List<Identification> identifications = new ArrayList<>();
        for(EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack item = player.getEquipment().getItem(slot);
            if(item == null || item.getType() == Material.AIR || !item.hasItemMeta() || !item.getItemMeta().hasLore()) {
                continue;
            }
            if(slot == EquipmentSlot.HAND || slot == EquipmentSlot.OFF_HAND) {
                if(Welven.getUtil().isArmor(item)) {
                    continue;
                }
            }
            List<String> lore = item.getItemMeta().getLore();
            if(lore == null) {
                continue;
            }
            for(String l : lore) {
                if(l.isEmpty()) continue;
                if(!isInt(ChatColor.stripColor(l).substring(0, 2).trim().replace("+", ""))) continue;
                String text = ChatColor.stripColor(l);
                Identification identification = new Identification(IdentificationType.fromName(join(text.split(" "), 1)),
                        Integer.parseInt(ChatColor.stripColor(l).split(" ")[0].replace("/4s", "").replace("+", "").replace("%", "")));
                identifications.add(identification);
                identification = null;
            }
        }
        return identifications;
    }

    private static String join(String[] args, int from) {
        StringBuilder builder = new StringBuilder();
        for(int i = from; i < args.length; i++) {
            builder.append(args[i]).append(" ");
        }
        return builder.toString().trim();
    }

    private static boolean isInt(String text) {
        try {
            Integer.parseInt(text);
        }catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Getter private final IdentificationType type;
    @Getter private final int value;
    private final int hash;
    public Identification(IdentificationType type, int value) {
        this.type = type;
        this.value = value;
        this.hash = type.getName().hashCode() + value;
    }

    public void apply(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if(!meta.hasLore()) {
            String value = getValue() < 0 ? "&c" + getValue() + "%" : "&a+" + getValue() + "%";
            meta.setLore(Arrays.asList("", ChatColor.translateAlternateColorCodes('&', value + " " + getType().getName())));
        }
        List<String> lore = meta.getLore();
        lore.add(ChatColor.translateAlternateColorCodes('&', value + " " + getType().getName()));
        item.setItemMeta(meta);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof Identification)) {
            return false;
        }
        Identification toCompare = (Identification)o;
        return (getType().getName().equals(toCompare.getType().getName()) && getValue() == toCompare.getValue());
    }

}
