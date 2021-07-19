package com.redspeaks.welven.command;

import com.redspeaks.welven.Welven;
import com.redspeaks.welven.lib.Avatar;
import com.redspeaks.welven.lib.locations.LocationsFile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to do that!");
            return true;
        }
        Player player = (Player)sender;
        Avatar avatar = Avatar.getSelectedAvatar(player);
        avatar.setLastLocation(player.getLocation());
        avatar.setLastInventory(player.getInventory().getStorageContents());
        player.teleport(LocationsFile.LOGIN == null ? player.getWorld().getSpawnLocation() : LocationsFile.LOGIN);
        player.getInventory().clear();
        player.updateInventory();
        Welven.getUtil().getPlugin().getClassSelection().open(player);
        return false;
    }
}
