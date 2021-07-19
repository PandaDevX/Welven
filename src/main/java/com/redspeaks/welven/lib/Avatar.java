package com.redspeaks.welven.lib;

import com.redspeaks.welven.Welven;
import com.redspeaks.welven.lib.classes.AvatarClass;
import com.redspeaks.welven.lib.gear.Identification;
import com.redspeaks.welven.lib.gear.IdentificationType;
import com.redspeaks.welven.lib.gui.ItemBuilder;
import com.redspeaks.welven.lib.util.Percentage;
import com.redspeaks.welven.lib.util.Symbol;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Avatar {

    public static final HashMap<UUID, List<Avatar>> avatarMap = new HashMap<>();
    public static final HashMap<UUID, Integer> selectedAvatar = new HashMap<>();

    public static boolean create(Player player, String type) {
        if(avatarMap.size() == 9) {
            return false;
        }
        List<Avatar> avatars = getAvatars(player);
        avatars.add(new Avatar(player, Welven.getUtil().getAvatarClass(type)));
        avatarMap.put(player.getUniqueId(), avatars);
        return true;
    }

    public static int getSelection(Player player) {
        return selectedAvatar.getOrDefault(player.getUniqueId(), 0);
    }

    public static boolean hasAvatarAt(Player player, int index) {
        if(index > 8) {
            return false;
        }
        if(index < 0) {
            return false;
        }
        return getAvatars(player).size() > index;
    }

    public static Avatar getSelectedAvatar(Player player) {
        return getAvatar(player, getSelection(player));
    }

    public static boolean hasSelection(Player player) {
        return selectedAvatar.containsKey(player.getUniqueId());
    }

    public static void select(Player player, int index) {
        selectedAvatar.put(player.getUniqueId(), index);
        getSelectedAvatar(player).setMana(0);
    }

    public static Avatar getAvatar(Player player, int index) {
        return getAvatars(player).get(index);
    }

    public static List<Avatar> getAvatars(Player player) {
        if(avatarMap.containsKey(player.getUniqueId())) {
            return avatarMap.get(player.getUniqueId());
        }
        List<Avatar> list = new ArrayList<>();
        avatarMap.put(player.getUniqueId(),list);
        return list;
    }

    @Getter
    private final Player player;
    @Getter
    private final AvatarClass avatarClass;
    @Getter @Setter private int regen, mana, maxHealth, maxMana, rawRegen;
    @Getter private int health;
    @Getter @Setter
    private long hurtInterval, lifestealInterval;
    @Getter @Setter private int attack_damage, attack_speed, attack_range, knockback, level, lifesteal;
    @Getter @Setter private int magic_defense;
    @Getter @Setter private List<Identification> identifications;
    @Getter @Setter private int physical_defense;
    @Getter @Setter private ItemStack[] lastInventory;
    @Getter @Setter private Location lastLocation;
    public Avatar(Player player, AvatarClass avatarClass) {
        this.player = player;
        this.avatarClass = avatarClass;
        this.health = 20;
        this.regen = 1;
        this.mana = 0;
        this.maxHealth = 20;
        this.maxMana = 20;
        this.hurtInterval = -1;
        this.attack_damage = 0;
        this.attack_speed = -1;
        this.attack_range = 0;
        this.knockback = 0;
        this.lifesteal = 0;
        this.magic_defense = 0;
        this.level = 0;
        this.physical_defense = 0;
        this.lifestealInterval = 0;
        this.identifications = new ArrayList<>();
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(1024);
    }

    public void setHealth(int health) {
        if(health <= 0) {
            this.health = getMaxHealth();
            respawn();
            return;
        }
        if(health >= getMaxHealth()) {
            this.health = getMaxHealth();
            return;
        }
        this.health = health;
    }

    public void respawn() {
        new BukkitRunnable() {
            @Override
            public void run() {
                getPlayer().spigot().respawn();
                getPlayer().setHealth(getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                getPlayer().teleport(Welven.getUtil().getPlugin().getLocationsFile().SPAWN);
                setHurtInterval(5000 + System.currentTimeMillis());
            }
        }.runTask(Welven.getUtil().getPlugin());
    }

    public ItemStack getItem() {
        return ItemBuilder.build(getAvatarClass().toItem().getItemMeta().getDisplayName(), getAvatarClass().toItem().getType(),
                "&eCharacter Info:", "&e- &7Class: &f" + getAvatarClass().getName(),
                "&e- &7Level: &f" + getLevel(), "&e- &7XP Progress: &f" + getProgress() + "%",
                "&e- &7Quest Completed: &f" + questCompleted() + "/235", "&e- &7Abilities Upgraded: &f" + abilitiesUpgraded() + "/24", "",
                "&aStatistics:", "&a- &7Total Playtime: &f" + getSecondsPlayed() + " Seconds", "&a- &7Mob Killed: &f345", "&a- &7Chest Looted: &f34");
    }

    public int questCompleted(){
        return 0;
    }

    public int getProgress() {
        return 0;
    }

    public int abilitiesUpgraded() {
        return 0;
    }

    public int getSecondsPlayed() {
        return getPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE)/ 20;
    }


    public void regenHealth() {
        int initialValue = getRegen() == 1 ? getHealth() + 1 :
                getHealth() + (1 + Percentage.of(getMaxHealth(), 1) + getRawRegen()) + Percentage.of(getMaxHealth(), getRegen());
        if(initialValue > getMaxHealth()) {
            initialValue = getMaxHealth();
        }
        setHealth(initialValue);
        updateHealth();
    }

    public void regenMana() {
        setMana(getMana() + 1);
    }

    public void updateArmor() {
        if(getPlayer().hasCooldown(getPlayer().getEquipment().getItemInMainHand().getType())) {
            runSync(() -> getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING));
            return;
        }
        if(getAttack_speed() < 0) {
            runSync(() -> getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING));
            return;
        }
        runSync(() -> getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,  1000000, getAttack_speed(), false, false, false)));
    }

    public void runSync(SyncMethod method) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Welven.getUtil().getPlugin(), method::process);
    }

    public void updateHealth() {
        int value = Percentage.of((int)getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(), Percentage.get(getHealth(), getMaxHealth()));
        if(value > 0) {
            getPlayer().setHealth(value);
        }
    }
    public void updateIdentification() {
        int maxHealth = 20, healthRegen = 1, rawRegen = 0, lifesteal = 0;
        int attack_damage = 0, attack_speed = -1, attack_range = 0, knockback = 0;
        int magic_defense = 0, physical_defense = 0;
        if(!getIdentifications().isEmpty()) {
            for(Identification identification : getIdentifications()) {
                if(identification.getType() == IdentificationType.HEALTH || identification.getType() == IdentificationType.BASE_HEALTH) {
                    maxHealth += identification.getValue();
                }
                if(identification.getType() == IdentificationType.HEALTH_REGEN) {
                    healthRegen += identification.getValue();
                }
                if(identification.getType() == IdentificationType.RAW_HEALTH_REGEN) {
                    rawRegen += identification.getValue();
                }
                if(identification.getType() == IdentificationType.ATTACK_DAMAGE) {
                    attack_damage += identification.getValue();
                }
                if(identification.getType() == IdentificationType.ATTACK_SPEED) {
                    attack_speed += (identification.getValue() - 1);
                }
                if(identification.getType() == IdentificationType.ATTACK_RANGE) {
                    attack_range += identification.getValue();
                }
                if(identification.getType() == IdentificationType.KNOCKBACK) {
                    knockback += identification.getValue();
                }
                if(identification.getType() == IdentificationType.LIFE_STEAL) {
                    lifesteal += identification.getValue();
                }
                if(identification.getType() == IdentificationType.PHYSICAL_DEFENSE) {
                    physical_defense += identification.getValue();
                }
                if(identification.getType() == IdentificationType.MAGIC_RESISTANCE) {
                    magic_defense += identification.getValue();
                }
            }
        }
        setMaxHealth(maxHealth);
        if(getHealth() > getMaxHealth()) {
            setHealth(getMaxHealth());
        }
        setRegen(healthRegen);
        setRawRegen(rawRegen);
        setAttack_damage(attack_damage);
        setAttack_speed(attack_speed);
        setAttack_range(attack_range);
        setKnockback(knockback);
        setLifesteal(lifesteal);
        setPhysical_defense(physical_defense);
        setMagic_defense(magic_defense);
        getIdentifications().clear();
        getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                ChatColor.translateAlternateColorCodes('&', "&c&l" + Symbol.HEART.get() + "  &c" + getHealth() + "/" + getMaxHealth() + "                   &b&l" + Symbol.STAR_16.get() + " &b" + getMana() + "/" + getMaxMana())
        ));
    }




}

interface SyncMethod {
    void process();
}
