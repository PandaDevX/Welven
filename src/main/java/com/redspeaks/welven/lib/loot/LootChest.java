package com.redspeaks.welven.lib.loot;

import com.redspeaks.welven.Welven;
import com.redspeaks.welven.lib.Avatar;
import com.redspeaks.welven.lib.items.ItemsFile;
import com.redspeaks.welven.lib.util.ItemCreator;
import com.redspeaks.welven.lib.util.Symbol;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class LootChest implements InventoryHolder {

    @Getter private final Random random = Welven.getUtil().getRandom();
    @Getter private final static Map<Location, LootChest> lootChests = new HashMap<>();

    public static void createLootChest(Location location, int contents, int minutes) {
        if(contents > 27) {
            contents = 27;
        }
        if(contents <= 0) {
            contents = 1;
        }
        LootChest chest = new LootChest(location, contents, minutes);
        lootChests.put(location, chest);
        chest.init();
    }

    public static LootChest getLootChest(Location location) {
        return lootChests.getOrDefault(location, null);
    }

    @Getter
    private final Inventory inventory;
    @Getter @Setter
    private long respawnTime;
    @Getter @Setter
    private Location location;
    private final int hash;
    @Getter private final int randomSize;
    @Getter private final int minutes;
    LootChest(Location location, int contents, int minutes) {
        randomSize = random.nextInt(contents);
        this.inventory = Bukkit.createInventory(this, 9 * 3);
        this.respawnTime = 0;
        this.location = location;
        this.hash = location.hashCode();
        this.minutes = minutes;
    }

    public void init() {
        for(int i = 0; i < getRandomSize(); i++) {
            Rarity.roll().stream()
                    .map(ItemCreator::create)
                    .forEach(item -> getInventory().setItem(Welven.getUtil().getRandom().nextInt(27), item));
        }
    }

    public void open(Avatar avatar) {
        for (int i = 0; i < getInventory().getContents().length; i++) {
            ItemStack item = getInventory().getContents()[i];
            if(item == null) continue;
            String id = Welven.getUtil().getItemOriginName(item);
            if(id != null) {
                if(avatar.getAvatarClass().getName().equals(item.getItemMeta().getLore().get(2))) {
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = meta.getLore();
                    lore.set(2, Welven.getUtil().translate("&a" + Symbol.CHECK.get() + " &7Class: &fWarrior"));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    getInventory().setItem(i, item);
                }
            }
        }
        avatar.getPlayer().playSound(avatar.getPlayer().getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
        avatar.getPlayer().openInventory(getInventory());
    }

    public void vanish() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Welven.getUtil().getPlugin(), () -> {
            if(getInventory().getViewers().isEmpty()) {
                System.out.println(getLocation().getX() + getLocation().getY() + getLocation().getZ());
                getLocation().getBlock().setType(Material.AIR);
            }
        }, 5L);
        setRespawnTime(System.currentTimeMillis() + (((60L * getMinutes()) * 1000L)));
    }

    public void respawnIfTime() {
        if(getRespawnTime() == 0) return;
        if(System.currentTimeMillis() >= getRespawnTime()) {
            getLocation().getWorld().getBlockAt(getLocation()).setType(Material.CHEST);
            setRespawnTime(0);
        }
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof LootChest)) {
            return false;
        }
        return getInventory().equals(o);
    }
}
