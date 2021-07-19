package com.redspeaks.welven.lib.items;

import com.redspeaks.welven.lib.classes.AvatarClass;
import com.redspeaks.welven.lib.gear.Identification;
import com.redspeaks.welven.lib.loot.Rarity;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;

public class ItemCache {

    @Getter
    private final String name;
    @Getter
    private final String rarity, displayName;
    @Getter
    private final AvatarClass avatarClass;
    @Getter
    private final List<Identification> list;
    @Getter
    private final List<String> description;
    @Getter private final Material type;
    @Getter private final int durability;
    @Getter
    private final Rarity rare;
    public ItemCache(String name, String displayName, String rarity, List<Identification> list, List<String> description, Material type, int durability, AvatarClass avatarClass, Rarity rare) {
        this.name = name;
        this.list = list;
        this.displayName = displayName;
        this.rarity = rarity;
        this.description = description;
        this.type = type;
        this.durability = durability;
        this.avatarClass = avatarClass;
        this.rare = rare;
    }
}
