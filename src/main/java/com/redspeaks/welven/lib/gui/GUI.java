package com.redspeaks.welven.lib.gui;

import com.redspeaks.welven.Welven;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class GUI implements InventoryHolder, Listener {

    private final Inventory inventory;
    private final String name;
    public GUI(String name) {
        this(name, 6);
    }

    public GUI(String name, int row) {
        this.inventory = Bukkit.createInventory(this, (9*row), name);
        Welven.getUtil().getPlugin().getServer().getPluginManager().registerEvents(this, Welven.getUtil().getPlugin());
        this.name = name;
    }

    public abstract void onLoad();

    public abstract void open(Player player);

    public Inventory getInventory() {
        return inventory;
    }

    protected void onClick(Player player, InventoryClickEvent e) {}

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(!e.getView().getTitle().equals(name)) return;
        onClick((Player)e.getWhoClicked(), e);
    }
}
