package com.redspeaks.welven.lib.runnables;

import com.redspeaks.welven.Welven;
import com.redspeaks.welven.lib.Avatar;
import com.redspeaks.welven.lib.gear.Identification;
import com.redspeaks.welven.lib.loot.LootChest;
import org.bukkit.Bukkit;

public class Checker implements Runnable{


    @Override
    public void run() {
        Bukkit.getOnlinePlayers().stream().filter(Avatar::hasSelection).filter(player -> player.getHealth() > 0).forEach(player -> {
            Avatar avatar = Avatar.getSelectedAvatar(player);
            Welven.getUtil().checkIfDupe(player);
            avatar.setIdentifications(Identification.get(avatar.getPlayer()));
            avatar.updateIdentification();
            avatar.updateHealth();
            avatar.updateArmor();
        });
        LootChest.getLootChests().values().forEach(LootChest::respawnIfTime);
    }
}
