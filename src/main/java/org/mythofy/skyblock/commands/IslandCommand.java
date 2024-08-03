package org.mythofy.skyblock.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mythofy.skyblock.ExpadorSkyblock;
import org.mythofy.skyblock.Island;
import org.mythofy.skyblock.managers.IslandManager;

import java.util.Optional;

public class IslandCommand implements CommandExecutor {

    private final ExpadorSkyblock plugin;
    private final IslandManager islandManager;


    public IslandCommand(ExpadorSkyblock plugin) {
        this.plugin = plugin;
        // Ensure that this command is registered in the main class
        plugin.getCommand("island").setExecutor(this);
        this.islandManager = plugin.getIslandManager();

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use island commands.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // If no arguments, show help message
            showHelp(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                handleCreate(player);
                break;
            case "home":
                handleHome(player);
                break;
            case "delete":
                handleDelete(player);
                break;
            case "visit":
                if (args.length > 1) {
                    handleVisit(player, args[1]);
                } else {
                    player.sendMessage("Please specify a player name to visit their island.");
                }
                break;
            case "setprivacy":
                if (args.length > 1) {
                    handleSetPrivacy(player, args[1]);
                } else {
                    player.sendMessage("Please specify the privacy level.");
                }
                break;
            default:
                player.sendMessage("Unknown command. Use /island for help.");
                break;
        }
        return true;
    }

    private void showHelp(Player player) {
        player.sendMessage("Island Commands:");
        player.sendMessage("/island create - Create your island");
        player.sendMessage("/island home - Teleport to your island home");
        player.sendMessage("/island delete - Delete your island");
        player.sendMessage("/island visit <player> - Visit another player's island");
        player.sendMessage("/island setprivacy <public/private> - Set your island's privacy");
    }

    private void handleCreate(Player player) {
        // Delegate to the CommandManager
        plugin.getCommandManager().handleCreateIslandCommand(player);
    }

    private void handleHome(Player player) {
        // Delegate to the CommandManager
        plugin.getCommandManager().handleTeleportHomeCommand(player);
    }

    private void handleDelete(Player player) {
        if (plugin.getIslandManager().deleteIsland(player)) {
            player.sendMessage("Your island has been successfully deleted.");
        } else {
            player.sendMessage("Failed to delete your island.");
        }
    }
    private void handleSetHomeCommand(Player player) {
        Optional<Island> optionalIsland = islandManager.getIslandByOwner(player.getUniqueId());
        if (optionalIsland.isPresent()) {
            Island island = optionalIsland.get();
            Location newHomeLocation = player.getLocation();
            island.setHomeLocation(newHomeLocation);
            player.sendMessage(ChatColor.GREEN + "Island home location set to your current position.");
        } else {
            player.sendMessage(ChatColor.RED + "You do not have an island.");
        }
    }

    private void handleVisit(Player player, String targetPlayerName) {
        if (plugin.getIslandManager().visitIsland(player, targetPlayerName)) {
            player.sendMessage("You are now visiting " + targetPlayerName + "'s island.");
        } else {
            player.sendMessage("Failed to visit " + targetPlayerName + "'s island. It may not exist or is set to private.");
        }
    }

    private void handleSetPrivacy(Player player, String privacy) {
        if (privacy.equalsIgnoreCase("public") || privacy.equalsIgnoreCase("private")) {
            if (plugin.getIslandManager().setPrivacy(player, privacy.equalsIgnoreCase("public"))) {
                player.sendMessage("Your island's privacy has been set to " + privacy + ".");
            } else {
                player.sendMessage("Failed to set island's privacy. Ensure you have an island.");
            }
        } else {
            player.sendMessage("Invalid privacy setting. Use 'public' or 'private'.");
        }
    }
}
