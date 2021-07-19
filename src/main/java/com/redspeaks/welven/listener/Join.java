package com.redspeaks.welven.listener;

import com.redspeaks.welven.Welven;
import com.redspeaks.welven.lib.locations.LocationsFile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().teleport(LocationsFile.LOGIN == null ? e.getPlayer().getWorld().getSpawnLocation() : LocationsFile.LOGIN);
        e.getPlayer().getInventory().clear();
        e.getPlayer().updateInventory();
        Welven.getUtil().getPlugin().getClassSelection().open(e.getPlayer());
    }
}
