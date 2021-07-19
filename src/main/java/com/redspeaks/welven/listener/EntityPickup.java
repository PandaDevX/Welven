package com.redspeaks.welven.listener;

import com.redspeaks.welven.Welven;
import com.redspeaks.welven.lib.Avatar;
import com.redspeaks.welven.lib.util.Symbol;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class EntityPickup implements Listener {

    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        ItemStack item = e.getItem().getItemStack();
        String id = Welven.getUtil().getItemOriginName(item);
        Avatar avatar = Avatar.getSelectedAvatar((Player) e.getEntity());
        if(id != null) {
            if(avatar.getAvatarClass().getName().equals(item.getItemMeta().getLore().get(2))) {
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.getLore();
                lore.set(2, Welven.getUtil().translate("&a" + Symbol.CHECK.get() + " &7Class: &f" + avatar.getAvatarClass().getName()));
                meta.setLore(lore);
                item.setItemMeta(meta);
                avatar.getPlayer().playSound(e.getItem().getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.2F, ((Welven.getUtil().getRandom().nextFloat() - Welven.getUtil().getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                avatar.getPlayer().getInventory().addItem(item);
                e.getItem().remove();
                e.setCancelled(true);
            }
        }
        avatar.getPlayer().updateInventory();
    }
}
