package com.redspeaks.welven.lib.gui.types;

import com.redspeaks.welven.Welven;
import com.redspeaks.welven.lib.Avatar;
import com.redspeaks.welven.lib.gui.GUI;
import com.redspeaks.welven.lib.gui.ItemBuilder;
import com.redspeaks.welven.lib.locations.LocationsFile;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ClassSelection extends GUI {

    public ClassSelection() {
        super("Class Selection");
    }

    public static HashMap<UUID, Integer> playerSelection = new HashMap<>();

    @Override
    public void onLoad() { }

    public void open(Player player) {
        for(int i = 0; i < 9; i++) {
            getInventory().setItem(i, null);
        }
        if(Avatar.getAvatars(player).isEmpty()) {
            getInventory().setItem(0, ItemBuilder.plus());
        } else {
            for(int i = 0; i < Avatar.getAvatars(player).size(); i++) {
                getInventory().setItem(i, Avatar.getAvatar(player, i).getItem());
            }
            if(Avatar.getAvatars(player).size() < 9) {
                getInventory().setItem(Avatar.getAvatars(player).size(), ItemBuilder.plus());
            }
        }
        for(int i = 9; i < getInventory().getSize(); i++) {
            getInventory().setItem(i, ItemBuilder.blank());
        }
        player.openInventory(getInventory());
    }

    @Override
    public void onClick(Player player, InventoryClickEvent e) {
        if(!(e.getInventory().getHolder() instanceof ClassSelection)) return;
        List<Avatar> characters = Avatar.getAvatars(player);
        if(e.getSlot() > characters.size()) {
            e.setCancelled(true);
            return;
        }
        if(e.getSlot() == characters.size() && Avatar.getAvatars(player).size() < 9) {
            player.openInventory(Welven.getUtil().getPlugin().getCreateGui().getInventory());
            e.setCancelled(true);
            return;
        }
        playerSelection.put(player.getUniqueId(), e.getSlot());
        if(e.getClick() == ClickType.RIGHT) {
            e.setCancelled(true);
            Welven.getUtil().getPlugin().getDeleteGui().open(player);
            return;
        }
        player.teleport(LocationsFile.SPAWN == null ? player.getWorld().getSpawnLocation() : LocationsFile.SPAWN);
        if(Avatar.getAvatar(player, e.getSlot()).getLastInventory() != null) {
            player.getInventory().setStorageContents(Avatar.getAvatar(player, e.getSlot()).getLastInventory());
            player.updateInventory();
        }
        playerSelection.remove(player.getUniqueId());
        Avatar.select(player, e.getSlot());
        player.sendMessage(ChatColor.GREEN + "Successfully chosen default warrior");
        e.setCancelled(true);
    }
}
