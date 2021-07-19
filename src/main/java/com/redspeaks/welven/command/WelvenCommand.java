package com.redspeaks.welven.command;

import com.redspeaks.welven.Welven;
import com.redspeaks.welven.lib.loot.LootChest;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WelvenCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            sender.sendMessage("/welven setspawn");
            sender.sendMessage("/welven setlogin");
            sender.sendMessage("/welven reload");
            sender.sendMessage("/welven updateitem");
            sender.sendMessage("/welven lootchest");
            return true;
        }
        switch(args[0].toLowerCase()) {
            case "setspawn":
                Welven.getUtil().getPlugin().getLocationsFile().setSpawn(((Player)sender).getLocation());
                sender.sendMessage(ChatColor.GREEN + "Changed spawn");
                break;
            case "setlogin":
                Welven.getUtil().getPlugin().getLocationsFile().setLogin(((Player)sender).getLocation());
                sender.sendMessage(ChatColor.GREEN + "Changed login");
                break;
            case "reload":
                Welven.getUtil().getPlugin().getLocationsFile().reload();
                Welven.getUtil().getPlugin().getItemsFile().reload();
                sender.sendMessage(ChatColor.GREEN + "Successfully reloaded all files");
                break;
            case "updateitem":
                Welven.getUtil().reloadItems(sender);
                break;
            case "lootchest":
                if(args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "/welven lootchest <content-size> <regen-mins>");
                    break;
                }
                int size = 0, minutes = 0;
                try {
                    size = Integer.parseInt(args[1]);
                    minutes = Integer.parseInt(args[2]);
                }catch(NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Please choose a correct format of number!");
                }

                if(!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "You must be a player to do that!");
                    return true;
                }

                Player player = (Player)sender;
                Block block = player.getTargetBlockExact(10);
                if(block == null) {
                    sender.sendMessage(ChatColor.RED + "Select a chest by looking at it!");
                    break;
                }
                if(!(block.getState() instanceof Chest)) {
                    sender.sendMessage(ChatColor.RED + "Select a chest by looking at it!");
                    break;
                }
                LootChest.createLootChest(block.getLocation(), size, minutes);
                sender.sendMessage(ChatColor.GREEN + "Successfully created a lootchest!");
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Unknown command.");
                break;
        }
        return false;
    }
}
