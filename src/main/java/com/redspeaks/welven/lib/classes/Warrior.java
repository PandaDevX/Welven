package com.redspeaks.welven.lib.classes;

import com.google.common.base.Strings;
import com.redspeaks.welven.lib.gui.ItemBuilder;
import com.redspeaks.welven.lib.util.Symbol;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Warrior implements AvatarClass {

    private final String name;
    private final int hash;
    public Warrior() {
        this.name = "Warrior";
        this.hash = name.hashCode();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ItemStack toItem() {
        return ItemBuilder.build("&6&lWarrior", Material.IRON_SWORD, "&eTank, Low Multi-Target", "",
                "&7The warrior is the defensive", "&7expert of any team. Capable of", "&7defending allies from the worst", "&7damage themselves. While not",
                "&7the most damaging of foes,", "&7escaping one is nearly impossible.", "", "&e\u2694 Physical Damage: " + Strings.repeat("&a&l⬛", 3) + Strings.repeat("&7&l⬛", 2),
                "&6\uD83D\uDEE1 Physical Defense: " + Strings.repeat("&a&l⬛", 5), "&b★ Magic Damage: " + Strings.repeat("&a&l⬛", 2) + Strings.repeat("&7&l⬛", 3),
                "&1✸ Magic Defense: " + Strings.repeat("&a&l⬛", 4) + "&7&l⬛", "&2" + Symbol.ARROW.get() + " Range: " + Strings.repeat("&a&l⬛", 2) + Strings.repeat("&7&l⬛", 3),
                "&3✺ Abilities: &a&l⬛" + Strings.repeat("&7&l⬛", 4));
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
        if(!(o instanceof Warrior)) {
            return false;
        }
        return getName().equals(((Warrior)o).getName());
    }
}
