package com.gotospawn;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class GotoCommand implements TabExecutor {
    private final GotoSpawnPlugin plugin;
    private static final List<String> SUBCOMMANDS = List.of("spawn", "bed");

    public GotoCommand(GotoSpawnPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }

        if (args.length != 1) {
            return false; // shows usage from plugin.yml
        }

        return switch (args[0].toLowerCase()) {
            case "spawn" -> handleGotoSpawn(player);
            case "bed" -> handleGotoBed(player);
            default -> false;
        };
    }

    private boolean handleGotoSpawn(Player player) {
        Location bedLocation = player.getRespawnLocation();
        if (bedLocation == null) {
            player.sendMessage(Component.text("You don't have a bed or respawn point set.", NamedTextColor.RED));
            return true;
        }

        double max = plugin.getMaxDistance();
        if (player.getLocation().distanceSquared(bedLocation) > max * max) {
            player.sendMessage(Component.text(
                "You must be within " + (int) max + " blocks of your bed to use this.",
                    NamedTextColor.RED));
            return true;
        }

        Location spawnLocation = getOverworld(player.getServer()).getSpawnLocation().add(0.5, 0, 0.5);
        player.teleportAsync(spawnLocation);
        player.sendMessage(Component.text("Teleported to world spawn.", NamedTextColor.GREEN));
        return true;
    }

    private boolean handleGotoBed(Player player) {
        Location bedLocation = player.getRespawnLocation();
        if (bedLocation == null) {
            player.sendMessage(Component.text("You don't have a bed or respawn point set.", NamedTextColor.RED));
            return true;
        }

        Location spawnLocation = getOverworld(player.getServer()).getSpawnLocation();

        double max = plugin.getMaxDistance();
        if (player.getLocation().distanceSquared(spawnLocation) > max * max) {
            player.sendMessage(Component.text(
                "You must be within " + (int) max + " blocks of world spawn to use this.",
                    NamedTextColor.RED));
            return true;
        }

        Location destination = bedLocation.clone().add(0.5, 0, 0.5);
        player.teleportAsync(destination);
        player.sendMessage(Component.text("Teleported to your bed.", NamedTextColor.GREEN));
        return true;
    }

    private World getOverworld(Server server) {
        for (World world : server.getWorlds()) {
            if (world.getEnvironment() == World.Environment.NORMAL) {
                return world;
            }
        }
        // Fallback: first world is always the overworld on vanilla/Paper
        return server.getWorlds().getFirst();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            return SUBCOMMANDS.stream()
                    .filter(s -> s.startsWith(partial))
                    .toList();
        }
        return List.of();
    }
}
