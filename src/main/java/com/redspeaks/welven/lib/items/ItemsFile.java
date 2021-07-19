package com.redspeaks.welven.lib.items;

import com.redspeaks.welven.Welven;
import com.redspeaks.welven.lib.Avatar;
import com.redspeaks.welven.lib.gear.Identification;
import com.redspeaks.welven.lib.gear.IdentificationType;
import com.redspeaks.welven.lib.loot.Rarity;
import com.redspeaks.welven.lib.util.ItemCreator;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ItemsFile {


    @Getter
    private final static HashMap<String, ItemCache> cacheMap = new HashMap<>();
    @Getter private final File file;
    @Getter @Setter
    private FileConfiguration config;
    public ItemsFile() {
        if(!Welven.getUtil().getPlugin().getDataFolder().exists()) {
            Welven.getUtil().getPlugin().getDataFolder().mkdir();
        }
        this.file = new File(Welven.getUtil().getPlugin().getDataFolder(), "items.yml");
        if(!file.exists()) {
            Welven.getUtil().getPlugin().saveResource("items.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);

        for(String key : config.getConfigurationSection("Items").getKeys(false)) {
            String displayName = config.getString("Items." + key + ".display-name");
            String rarity = config.getString("Items." + key + ".rarity");
            List<Identification> identifications = config.getStringList("Items." + key + ".ids")
                    .stream().map(s -> new Identification(IdentificationType.fromName(s.split(":")[0]), Integer.parseInt(s.split(":")[1])))
                    .collect(Collectors.toList());
            List<String> description = config.getStringList("Items." + key + ".lore");
            cacheMap.put(key.toLowerCase(), new ItemCache(key.toLowerCase(), displayName, rarity, identifications, description,
                    Material.getMaterial(config.getString("Items." + key + ".type")),
                    config.getInt("Items." + key + ".durability"), Welven.getUtil().getAvatarClass(config.getString("Items." + key + ".class")),
                    Rarity.byName(config.getString("Items." + key + ".rarity"))));
        }
    }

    public ItemStack getItem(String item, Avatar avatar) {
        ItemCache cache = cacheMap.getOrDefault(item.toLowerCase(), null);
        if(cache == null) {
            return null;
        }
        return ItemCreator.create(avatar, cache);
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);

        for(String key : config.getConfigurationSection("Items").getKeys(false)) {
            String displayName = config.getString("Items." + key + ".display-name");
            String rarity = config.getString("Items." + key + ".rarity");
            List<Identification> identifications = config.getStringList("Items." + key + ".ids")
                    .stream().map(s -> new Identification(IdentificationType.fromName(s.split(":")[0]), Integer.parseInt(s.split(":")[1])))
                    .collect(Collectors.toList());
            List<String> description = config.getStringList("Items." + key + ".lore");
            cacheMap.put(key.toLowerCase(), new ItemCache(key.toLowerCase(), displayName, rarity, identifications, description,
                    Material.getMaterial(config.getString("Items." + key + ".type")),
                    config.getInt("Items." + key + ".durability"), Welven.getUtil().getAvatarClass(config.getString("Items." + key + ".class")), Rarity.byName(config.getString("Items." + key + ".rarity"))));
        }
    }
}
