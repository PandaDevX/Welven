package com.redspeaks.welven.listener;

import com.redspeaks.welven.lib.Avatar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Quit implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if(!Avatar.hasSelection(e.getPlayer())) return;
        Avatar avatar = Avatar.getSelectedAvatar(e.getPlayer());
        avatar.setLastLocation(e.getPlayer().getLocation());
        avatar.setLastInventory(e.getPlayer().getInventory().getStorageContents());
    }
}
