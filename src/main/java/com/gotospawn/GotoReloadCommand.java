package com.gotospawn;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GotoReloadCommand implements CommandExecutor {

    private final GotoSpawnPlugin plugin;

    public GotoReloadCommand(GotoSpawnPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Permission is enforced via plugin.yml (permission: goto.admin)
        plugin.reloadGotoConfig();
        double dist = plugin.getMaxDistance();
        sender.sendMessage(Component.text("GotoSpawn config reloaded. max-distance=" + dist, NamedTextColor.GREEN));
        return true;
    }
}
