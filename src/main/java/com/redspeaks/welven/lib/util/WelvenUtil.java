package com.redspeaks.welven.lib.util;

import com.redspeaks.welven.Welven;
import com.redspeaks.welven.lib.Avatar;
import com.redspeaks.welven.lib.classes.AvatarClass;
import com.redspeaks.welven.lib.classes.Warrior;
import com.redspeaks.welven.lib.items.ItemCache;
import com.redspeaks.welven.lib.items.ItemsFile;
import com.redspeaks.welven.lib.loot.Rarity;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WelvenUtil {

    private final Welven plugin;
    private final Warrior warrior;
    private final static HashMap<UUID, Long> admins = new HashMap<>();
    private final Pattern HEX_PATTERN = Pattern.compile("#[A-Fa-f0-9]{6}");
    @Getter
    private final Random random;

    public WelvenUtil(Welven plugin) {
        this.plugin = plugin;
        this.warrior = new Warrior();
        this.random = new Random();
    }

    public AvatarClass getAvatarClass(String name) {
        if(name.equalsIgnoreCase("warrior")) {
            return warrior;
        }
        return null;
    }

    public void showDamage(Entity damageTaker, int damage) {
        if(damage <= 0) {
            return;
        }
        double randomX = Math.random();
        double randomY = Math.random();
        double randomZ = Math.random();
        randomX = -1+randomX;
        randomY = -1+randomY;
        randomZ = -1+randomZ;
        ArmorStand as = damageTaker.getWorld().spawn(damageTaker.getLocation().clone().add(randomX, randomY, randomZ),  ArmorStand.class);
        as.setCustomName(ChatColor.translateAlternateColorCodes('&', "&c&l- " + damage + " " + Symbol.HEART.get()));
        as.setCustomNameVisible(true);
        as.setGravity(true);
        as.setInvisible(true);
        as.setInvulnerable(true);

        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), as::remove, 20L);
    }

    public String getItemOriginName(ItemStack itemStack) {
        if(!itemStack.hasItemMeta()) {
            return null;
        }
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        if(!data.has(getPlugin().getItemName(), PersistentDataType.STRING)) {
            return null;
        }
        return data.get(getPlugin().getItemName(), PersistentDataType.STRING);
    }

    public String getItemOriginId(ItemStack itemStack) {
        if(!itemStack.hasItemMeta()) {
            return null;
        }
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        if(!data.has(getPlugin().getItemId(), PersistentDataType.STRING)) {
            return null;
        }
        return data.get(getPlugin().getItemId(), PersistentDataType.STRING);
    }

    public void setItemOriginName(ItemStack stack, String name) {
        if(!stack.hasItemMeta()) {
            return;
        }
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(getPlugin().getItemName(), PersistentDataType.STRING, name);
        stack.setItemMeta(meta);
    }

    public void setItemOriginId(ItemStack stack, String name) {
        if(!stack.hasItemMeta()) {
            return;
        }
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(getPlugin().getItemId(), PersistentDataType.STRING, name);
        stack.setItemMeta(meta);
    }

    public void setItemOriginId(ItemStack stack) {
        if(!stack.hasItemMeta()) {
            return;
        }
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(getPlugin().getItemId(), PersistentDataType.STRING, UUID.randomUUID().toString());
        stack.setItemMeta(meta);
    }

    public void reloadItems(CommandSender sender) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), () -> {
            Bukkit.getOnlinePlayers().stream().filter(Avatar::hasSelection).filter(p -> !p.getInventory().isEmpty()).forEach(player -> {
                for(int i = 0; i < player.getInventory().getStorageContents().length; i++) {
                    ItemStack stack = player.getInventory().getStorageContents()[i];
                    if(stack == null) continue;
                    String name = getItemOriginName(stack);
                    if(name == null) continue;
                    ItemStack stack2 = getPlugin().getItemsFile().getItem(name, Avatar.getSelectedAvatar(player));
                    setItemOriginName(stack2, getItemOriginName(stack));
                    setItemOriginId(stack2, getItemOriginId(stack));
                    player.getInventory().setItem(i, stack2);
                }
                player.updateInventory();
            });
        });
        sender.sendMessage(ChatColor.GREEN + "All online players' items are now up to date.");
    }

    public void alarmForDupe(String permission, Player suspect) {
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission(permission))
                .filter(p -> admins.getOrDefault(p.getUniqueId(), 0L) <= System.currentTimeMillis())
                .forEach(player -> {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2&l(!) &7Player: &f" + suspect.getDisplayName() + " &7have duplicated items."));
                    admins.put(player.getUniqueId(), System.currentTimeMillis() + 5000L);
                });
    }

    public boolean isArmor(ItemStack stack) {
        if(stack == null) {
            return false;
        }
        if(stack.getType().name().endsWith("BOOTS")) {
            return true;
        }
        if(stack.getType().name().endsWith("CHESTPLATE")) {
            return true;
        }
        if(stack.getType().name().endsWith("LEGGINGS")) {
            return true;
        }
        return stack.getType().name().endsWith("HELMET");
    }

    public void checkIfDupe(Player player) {
        List<String> current = new ArrayList<>();
        if(player.getInventory().isEmpty()) return;
        for(ItemStack stack : player.getInventory().getContents()) {
            if(stack == null) continue;
            String id = getItemOriginId(stack);
            if(id == null) continue;
            if(id.equals("admin")) continue;
            if(current.contains(id)) {
                alarmForDupe("welven.admin", player);
                continue;
            }
            current.add(id);
        }
        current.clear();
    }

    public String translate(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
            matcher = HEX_PATTERN.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public void storeLootData(ItemStack itemStack, int[] dat) {
        if(!itemStack.hasItemMeta()) {
            return;
        }
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(getPlugin().getItemId(), PersistentDataType.INTEGER_ARRAY, dat);
    }

    public int[] lootData(ItemStack itemStack) {
        if(!itemStack.hasItemMeta()) {
            return null;
        }
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        if(!data.has(getPlugin().getItemId(), PersistentDataType.STRING)) {
            return null;
        }
        return data.get(getPlugin().getItemId(), PersistentDataType.INTEGER_ARRAY);
    }

    public ItemCache rollItem(Rarity rarity) {
        if(rarity.get() < getRandom().nextInt(101)) {
            return null;
        }
        return ItemsFile.getCacheMap().values().stream().filter(cache -> cache.getRare() == rarity).findAny().orElse(null);
    }

    public Welven getPlugin() {
        return plugin;
    }
}
