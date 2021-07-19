package com.redspeaks.welven.lib.gui.types;

import com.google.common.base.Strings;
import com.redspeaks.welven.Welven;
import com.redspeaks.welven.lib.Avatar;
import com.redspeaks.welven.lib.gui.GUI;
import com.redspeaks.welven.lib.gui.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;

public class CreateGUI extends GUI {

    public CreateGUI() {
        super("Class Selection");
    }

    @Override
    public void onLoad() {
        getInventory().setItem(0, ItemBuilder.build("&6&lWarrior", Material.IRON_SWORD, "&eTank, Low Multi-Target", "",
                "&7The warrior is the defensive", "&7expert of any team. Capable of", "&7defending allies from the worst", "&7damage themselves. While not",
                "&7the most damaging of foes,", "&7escaping one is nearly impossible.", "", "&e\u2694 Physical Damage: " + Strings.repeat("&a&l⬛", 3) + Strings.repeat("&7&l⬛", 2),
                "&6\uD83D\uDEE1 Physical Defense: " + Strings.repeat("&a&l⬛", 5), "&b★ Magic Damage: " + Strings.repeat("&a&l⬛", 2) + Strings.repeat("&7&l⬛", 3),
                "&1✸ Magic Defense: " + Strings.repeat("&a&l⬛", 4) + "&7&l⬛", "&2⬛ Range: " + Strings.repeat("&a&l⬛", 2) + Strings.repeat("&7&l⬛", 3),
                "&3✺ Abilities: &a&l⬛" + Strings.repeat("&7&l⬛", 4)));
        Arrays.stream(new int[] {9, 10, 11,12,13,14,15,16,17,36,37,38,39,40,41,42,43,44}).forEach(i -> {
            getInventory().setItem(i, ItemBuilder.custom(2));
        });
        Arrays.stream(new int[] {22, 31,45,46,47,48,50,51,52,53}).forEach(i -> {
            getInventory().setItem(i, ItemBuilder.custom(1));
        });
        getInventory().setItem(49, ItemBuilder.build("&cClose", Material.BARRIER));
    }

    @Override
    public void open(Player player) {

    }

    @Override
    public void onClick(Player player, InventoryClickEvent e) {
        Welven welven = Welven.getUtil().getPlugin();
        if(!(e.getInventory().getHolder() instanceof CreateGUI)) return;
        if(e.getSlot() == 49) {
            welven.getClassSelection().open(player);
            e.setCancelled(true);
        }
        if(e.getSlot() == 0) {
            Avatar.create(player, "warrior");
            welven.getClassSelection().open(player);
            e.setCancelled(true);
            return;
        }
        e.setCancelled(true);
    }
}
