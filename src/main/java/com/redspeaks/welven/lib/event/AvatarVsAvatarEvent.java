package com.redspeaks.welven.lib.event;

import com.redspeaks.welven.lib.Avatar;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AvatarVsAvatarEvent extends Event {

    public static HandlerList HANDLER_LIST = new HandlerList();
    @Getter
    private final Avatar damager, victim;
    @Getter @Setter
    private int damage;
    public AvatarVsAvatarEvent(Avatar avatar1, Avatar avatar2) {
        this.damager = avatar1;
        this.victim = avatar2;
        this.damage = 0;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
