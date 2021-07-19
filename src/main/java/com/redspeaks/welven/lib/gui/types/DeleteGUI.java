package com.redspeaks.welven.lib.gui.types;

import com.redspeaks.welven.Welven;
import com.redspeaks.welven.lib.Avatar;
import com.redspeaks.welven.lib.gui.GUI;
import com.redspeaks.welven.lib.gui.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Arrays;

public class DeleteGUI extends GUI {

    public DeleteGUI() {
        super("Confirm Deletion", 3);
    }

    @Override
    public void onLoad() {
        for(int i = 0; i < 9; i++) {
            getInventory().setItem(i, ItemBuilder.custom(2));
        }
        Arrays.stream(new int[] {9, 10, 12,13,14,16,17}).forEach(i -> getInventory().setItem(i, ItemBuilder.custom(1)));
        for(int i = 18; i < getInventory().getSize(); i++) {
            getInventory().setItem(i, ItemBuilder.custom(2));
        };
        getInventory().setItem(15, ItemBuilder.build("&4&lClick to Cancel", Material.BARRIER));
    }

    @Override
    public void onClick(Player player, InventoryClickEvent e) {
        if(e.getSlot() == 15) {
            Welven.getUtil().getPlugin().getClassSelection().open(player);
            e.setCancelled(true);
            return;
        }
        if(e.getSlot() == 11) {
            Avatar.getAvatars(player).remove((int)ClassSelection.playerSelection.get(player.getUniqueId()));
            ClassSelection.playerSelection.remove(player.getUniqueId());
            Welven.getUtil().getPlugin().getClassSelection().open(player);
            e.setCancelled(true);
            return;
        }
        e.setCancelled(true);
    }

    public void open(Player player) {
        Avatar selectedChar = Avatar.getAvatar(player, ClassSelection.playerSelection.get(player.getUniqueId()));
        getInventory().setItem(11, ItemBuilder.build("&6&lClick to Confirm", Material.YELLOW_CONCRETE, "&7This will delete your: " +
                selectedChar.getAvatarClass().getName() + " [Lvl. "+ selectedChar.getLevel() + "]", "&7This is irreversible."));
        player.openInventory(getInventory());
    }
}
