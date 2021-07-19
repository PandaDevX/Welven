package com.redspeaks.welven.lib.runnables;

import com.redspeaks.welven.lib.Avatar;
import org.bukkit.Bukkit;

public class Regeneration implements Runnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().stream().filter(Avatar::hasSelection).filter(player -> player.getHealth() > 0).forEach(player -> {
            Avatar avatar = Avatar.getSelectedAvatar(player);
            if (avatar.getHealth() < avatar.getMaxHealth()) {
                if (avatar.getHurtInterval() <= System.currentTimeMillis()) {
                    avatar.regenHealth();
                }
            }
            if(avatar.getMana() < avatar.getMaxMana()) {
                avatar.regenMana();
            }
        });
    }
}
