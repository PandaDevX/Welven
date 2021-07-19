package com.redspeaks.welven.lib.loot;

import com.redspeaks.welven.Welven;
import com.redspeaks.welven.lib.items.ItemCache;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum Rarity {
    COMMON(80, "Common"),
    UNCOMMON(50, "Uncommon"),
    RARE(30, "Rare"),
    EPIC(10, "Epic"),
    LEGENDARY(1, "Legendary");

    private final int percentage;
    @Getter
    private final String name;
    Rarity(int percentage, String name) {
        this.percentage = percentage;
        this.name = name;
    }

    public int get() {
        return percentage;
    }

    public static Rarity byName(String name) {
        return Arrays.stream(Rarity.values()).filter(r -> r.getName().equalsIgnoreCase(name)).findAny().orElse(Rarity.COMMON);
    }

    public static List<ItemCache> roll() {
        return Arrays.stream(Rarity.values()).map(r -> Welven.getUtil().rollItem(r)).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
