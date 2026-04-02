package com.gotospawn;

import org.bukkit.plugin.java.JavaPlugin;

public class GotoSpawnPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("goto").setExecutor(new GotoCommand());
        getLogger().info("GotoSpawn plugin enabled.");
    }
}
