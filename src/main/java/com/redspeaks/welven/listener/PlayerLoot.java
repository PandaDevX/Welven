package com.redspeaks.welven.listener;

import com.redspeaks.welven.lib.Avatar;
import com.redspeaks.welven.lib.loot.LootChest;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerLoot implements Listener {

    private final static Map<UUID, LootChest> openMap = new HashMap<>();

    @EventHandler
    public void onOpenChest(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(!Avatar.hasSelection(e.getPlayer())) return;
        Avatar avatar = Avatar.getSelectedAvatar(e.getPlayer());
        if(e.getClickedBlock() == null) return;
        if(e.getClickedBlock().getState() instanceof Chest) {
            if(LootChest.getLootChest(e.getClickedBlock().getLocation()) == null) {
                return;
            }
            e.setCancelled(true);
            LootChest.getLootChest(e.getClickedBlock().getLocation()).open(avatar);
            openMap.put(e.getPlayer().getUniqueId(), LootChest.getLootChest(e.getClickedBlock().getLocation()));
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(e.getInventory().getHolder() instanceof LootChest) {
            if(!openMap.containsKey(e.getPlayer().getUniqueId())) return;
            LootChest chest = openMap.get(e.getPlayer().getUniqueId());
            if(chest.getInventory().isEmpty()) {
                chest.vanish();
            }
            openMap.remove(e.getPlayer().getUniqueId());
        }
    }
}
