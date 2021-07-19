package com.redspeaks.welven;

import com.redspeaks.welven.command.ClassCommand;
import com.redspeaks.welven.command.GiveCommand;
import com.redspeaks.welven.command.WelvenCommand;
import com.redspeaks.welven.lib.gui.GUI;
import com.redspeaks.welven.lib.gui.types.ClassSelection;
import com.redspeaks.welven.lib.gui.types.CreateGUI;
import com.redspeaks.welven.lib.gui.types.DeleteGUI;
import com.redspeaks.welven.lib.items.ItemsFile;
import com.redspeaks.welven.lib.locations.LocationsFile;
import com.redspeaks.welven.lib.runnables.Checker;
import com.redspeaks.welven.lib.runnables.Regeneration;
import com.redspeaks.welven.lib.util.WelvenUtil;
import com.redspeaks.welven.listener.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Welven extends JavaPlugin implements Listener {

    private static WelvenUtil instance = null;
    @Getter
    private ItemsFile itemsFile = null;
    @Getter private LocationsFile locationsFile = null;
    @Getter private GUI createGui, deleteGui, classSelection;
    @Getter private NamespacedKey itemName, itemId, loot;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = new WelvenUtil(this);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Checker(), 0, 1);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Regeneration(), 0, 20);

        Bukkit.getServer().getPluginManager().registerEvents(new EntityDamage(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityDeath(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityPickup(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerLoot(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Join(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Quit(), this);

        itemsFile = new ItemsFile();
        locationsFile = new LocationsFile();
        this.createGui = new CreateGUI();
        this.classSelection = new ClassSelection();
        this.deleteGui = new DeleteGUI();

        this.loot = new NamespacedKey(this, "welven_loot");
        this.itemName = new NamespacedKey(this, "welven_itemname");
        this.itemId = new NamespacedKey(this, "welven_itemid");

        createGui.onLoad();
        classSelection.onLoad();
        deleteGui.onLoad();


        getCommand("welven").setExecutor(new WelvenCommand());
        getCommand("giveitem").setExecutor(new GiveCommand());
        getCommand("class").setExecutor(new ClassCommand());
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static WelvenUtil getUtil() {
        return instance;
    }
}
