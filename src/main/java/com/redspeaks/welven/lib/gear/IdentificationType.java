package com.redspeaks.welven.lib.gear;

import lombok.Getter;

import java.util.Arrays;

public enum IdentificationType {
    HEALTH("Health", 0), BASE_HEALTH("Base Health", 1), HEALTH_REGEN("Health Regen", 0), RAW_HEALTH_REGEN("Raw Health Regen", 0),
    ATTACK_DAMAGE("Attack Damage", 1), ATTACK_SPEED("Attack Speed", 1), ATTACK_RANGE("Attack Range", 1), KNOCKBACK("Knockback", 1),
    LIFE_STEAL("Life Steal", 0), PHYSICAL_DEFENSE("Physical Defense", 0), MAGIC_RESISTANCE("Magic Resistance", 0);

    @Getter
    private final int value;
    @Getter private final String name;
    IdentificationType(String name, int value) {
        this.value = value;
        this.name = name;
    }

    public boolean isRaw() {
        return getValue() == 1;
    }

    public static IdentificationType fromName(String name) {
        return Arrays.stream(IdentificationType.values()).filter(i -> i.getName().equalsIgnoreCase(name)).findAny().get();
    }
}
