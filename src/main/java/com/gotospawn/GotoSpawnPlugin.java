package com.gotospawn;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class GotoSpawnPlugin extends JavaPlugin {

    private static final double DEFAULT_MAX_DISTANCE = 16.0;
    private static final double MIN_ALLOWED_DISTANCE = 1.0;
    private static final double MAX_ALLOWED_DISTANCE = 48.0;
    private static final String CONFIG_FILENAME = "goto.conf"; // YAML format

    private double maxDistance = DEFAULT_MAX_DISTANCE;

    @Override
    public void onEnable() {
        // Ensure data folder exists and load configuration
        loadGotoConfig();

        // Register commands
        GotoCommand gotoCommand = new GotoCommand(this);
        getCommand("goto").setExecutor(gotoCommand);
        getCommand("goto").setTabCompleter(gotoCommand);
        getCommand("gotoreload").setExecutor(new GotoReloadCommand(this));

        getLogger().info("GotoSpawn plugin enabled. max-distance=" + maxDistance);
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    void loadGotoConfig() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            getLogger().warning("Could not create plugin data folder: " + dataFolder.getAbsolutePath());
        }

        File configFile = new File(dataFolder, CONFIG_FILENAME);

        FileConfiguration cfg = new YamlConfiguration();
        if (!configFile.exists()) {
            // Create a default config file
            cfg.set("max-distance", DEFAULT_MAX_DISTANCE);
            try {
                cfg.save(configFile);
            } catch (IOException e) {
                getLogger().warning("Failed to save default config: " + e.getMessage());
            }
            maxDistance = DEFAULT_MAX_DISTANCE;
            return;
        }

        try {
            cfg.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            getLogger().warning("Failed to load config; using defaults. Error: " + e.getMessage());
            maxDistance = DEFAULT_MAX_DISTANCE;
            return;
        }

        double value = cfg.getDouble("max-distance", DEFAULT_MAX_DISTANCE);
        if (Double.isNaN(value)) {
            getLogger().warning("Config 'max-distance' is not a number; falling back to default " + DEFAULT_MAX_DISTANCE);
            value = DEFAULT_MAX_DISTANCE;
        }

        if (value < MIN_ALLOWED_DISTANCE) {
            getLogger().warning("Config 'max-distance' (" + value + ") below minimum; clamping to " + MIN_ALLOWED_DISTANCE);
            value = MIN_ALLOWED_DISTANCE;
        } else if (value > MAX_ALLOWED_DISTANCE) {
            getLogger().warning("Config 'max-distance' (" + value + ") above maximum; clamping to " + MAX_ALLOWED_DISTANCE);
            value = MAX_ALLOWED_DISTANCE;
        }

        maxDistance = value;
    }

    void reloadGotoConfig() {
        loadGotoConfig();
        getLogger().info("Reloaded config. max-distance=" + maxDistance);
    }
}
