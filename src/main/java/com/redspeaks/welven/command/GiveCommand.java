package com.redspeaks.welven.command;

import com.redspeaks.welven.Welven;
import com.redspeaks.welven.lib.Avatar;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to do that!");
            return true;
        }
        if(args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/giveitem <name>");
            return true;
        }
        String name = args[0];
        Player player = (Player)sender;
        ItemStack stack = Welven.getUtil().getPlugin().getItemsFile().getItem(name, Avatar.getSelectedAvatar(player));
        if(stack == null) {
            player.sendMessage(ChatColor.RED + "No item found!");
            return true;
        }
        Welven.getUtil().setItemOriginName(stack, name.toLowerCase());
        Welven.getUtil().setItemOriginId(stack, "admin");
        player.getInventory().addItem(stack);
        player.updateInventory();
        sender.sendMessage(ChatColor.GREEN + "Item delivered");
        return false;
    }
}
