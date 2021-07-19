package com.redspeaks.welven.listener;

import com.redspeaks.welven.lib.Avatar;
import com.redspeaks.welven.lib.loot.Rarity;
import com.redspeaks.welven.lib.util.ItemCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.stream.Collectors;

public class EntityDeath implements Listener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if(e.getEntity().getKiller() != null) {
            if(Avatar.hasSelection(e.getEntity().getKiller())) {
                e.getDrops().addAll(Rarity.roll().stream().map(ItemCreator::create).collect(Collectors.toList()));
            }
        }
    }
}
