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

    private static final double MAX_DISTANCE = 16.0;
    private static final List<String> SUBCOMMANDS = List.of("spawn", "bed");

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

        if (player.getLocation().distanceSquared(bedLocation) > MAX_DISTANCE * MAX_DISTANCE) {
            player.sendMessage(Component.text(
                    "You must be within " + (int) MAX_DISTANCE + " blocks of your bed to use this.",
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

        if (player.getLocation().distanceSquared(spawnLocation) > MAX_DISTANCE * MAX_DISTANCE) {
            player.sendMessage(Component.text(
                    "You must be within " + (int) MAX_DISTANCE + " blocks of world spawn to use this.",
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
