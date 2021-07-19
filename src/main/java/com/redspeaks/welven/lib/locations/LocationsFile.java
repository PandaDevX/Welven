package com.redspeaks.welven.lib.locations;

import com.redspeaks.welven.Welven;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LocationsFile {

    public static Location SPAWN, LOGIN;
    @Getter private final File file;
    @Getter
    private FileConfiguration config;
    public LocationsFile() {
        if(!Welven.getUtil().getPlugin().getDataFolder().exists()) {
            Welven.getUtil().getPlugin().getDataFolder().mkdir();
        }
        file = new File(Welven.getUtil().getPlugin().getDataFolder(), "locations.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);

        SPAWN = config.getLocation("spawn");
        LOGIN = config.getLocation("login");
    }

    public void setSpawn(Location loc) {
        config.set("spawn", loc);
        SPAWN = loc;
        save();
    }

    public void setLogin(Location loc) {
        config.set("login", loc);
        LOGIN =loc;
        save();
    }

    public void save() {
        try {
            config.save(file);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
        SPAWN = config.getLocation("spawn");
        LOGIN = config.getLocation("login");
    }
}
