package com.redspeaks.welven.listener;

import com.redspeaks.welven.Welven;
import com.redspeaks.welven.lib.Avatar;
import com.redspeaks.welven.lib.event.AvatarVsAvatarEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

public class EntityDamage implements Listener {

    @EventHandler
    public void onPVP(EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player) {
            Player damager =(Player) e.getDamager();
            if(!Avatar.hasSelection(damager)) return;
            if(damager.hasCooldown(damager.getEquipment().getItemInMainHand().getType())) {
                e.setCancelled(true);
                return;
            }
            Avatar damageDealer = Avatar.getSelectedAvatar(damager);
            int attack_speed = damageDealer.getAttack_speed();
            damager.setCooldown(damager.getEquipment().getItemInMainHand().getType(), attack_speed > 0 ? attack_speed * 20 : 0);
            if(e.getEntity() instanceof Player) {
                e.setDamage(0);
                Player victim = (Player)e.getEntity();
                if(Avatar.hasSelection(damager) && Avatar.hasSelection(victim)) {
                    Avatar damageTaker = Avatar.getSelectedAvatar(victim);
                    AvatarVsAvatarEvent avatarVsAvatarEvent = new AvatarVsAvatarEvent(damageDealer, damageTaker);
                    Bukkit.getServer().getPluginManager().callEvent(avatarVsAvatarEvent);
                    damageTaker.setHealth(damageTaker.getHealth() - avatarVsAvatarEvent.getDamage());
                    Welven.getUtil().showDamage(e.getEntity(), avatarVsAvatarEvent.getDamage());
                }
            } else {
                if(damageDealer.getKnockback() > 0) {
                    e.getEntity().setVelocity(damager.getLocation().getDirection().setY(0.1).normalize().multiply(damageDealer.getKnockback()));
                }
                if(damageDealer.getAttack_damage() > 0) {
                    e.setDamage(damageDealer.getAttack_damage());
                }
                if(damageDealer.getLifesteal() > 0 && damageDealer.getLifestealInterval() <= System.currentTimeMillis()) {
                    e.setDamage(e.getDamage() + damageDealer.getLifesteal());
                    damageDealer.setHealth(damageDealer.getHealth() + damageDealer.getLifesteal());
                    damageDealer.setLifestealInterval(System.currentTimeMillis() + 4000);
                }
                Welven.getUtil().showDamage(e.getEntity(), (int)e.getDamage());
            }
        }
    }

    @EventHandler
    public void onDamaged(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) {
            return;
        }
        Player damaged = (Player) e.getEntity();
        if(!Avatar.hasSelection(damaged)) {
            return;
        }
        Avatar avatar = Avatar.getSelectedAvatar(damaged);
        avatar.setHealth(avatar.getHealth() - (int)e.getDamage());
        e.setDamage(0);
        avatar.setHurtInterval(5000 + System.currentTimeMillis());
    }

    @EventHandler
    public void onRegen(EntityRegainHealthEvent e) {
        if(e.getEntity() instanceof Player){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        e.setFoodLevel(100);
    }

    @EventHandler
    public void onAvatarFight(AvatarVsAvatarEvent e) {
        int damage = e.getDamager().getAttack_damage() - e.getVictim().getPhysical_defense();

        if(e.getDamager().getKnockback() > 0) {
            e.getVictim().getPlayer().setVelocity(e.getDamager().getPlayer().getLocation().getDirection().setY(0).normalize().multiply(e.getDamager().getKnockback()));
        }
        if(e.getDamager().getLifestealInterval() <= System.currentTimeMillis()) {
            damage += e.getDamager().getLifesteal();
        }

        e.setDamage(damage);
    }
}
