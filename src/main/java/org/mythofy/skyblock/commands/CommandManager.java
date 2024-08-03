package org.mythofy.skyblock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.mythofy.skyblock.ExpadorSkyblock;
import org.mythofy.skyblock.managers.IslandManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all commands for the ExpadorSkyblock plugin, ensuring they are registered and processed correctly.
 */
public class CommandManager implements CommandExecutor, TabCompleter {

    private ExpadorSkyblock plugin;
    private IslandManager islandManager;
    private final IslandCommand islandCommand;


    /**
     * Constructor to create an instance of CommandManager.
     * @param plugin The main class instance of the ExpadorSkyblock plugin.
     */
    public CommandManager(ExpadorSkyblock plugin) {
        this.plugin = plugin;
        this.islandManager = islandManager;
        this.islandCommand = new IslandCommand(plugin);


    }

    /**
     * Registers all commands used in the ExpadorSkyblock plugin.
     */
    public void registerCommands() {
        // Example of registering a command
        plugin.getCommand("island").setExecutor(this);
        plugin.getCommand("island").setTabCompleter(this);
        plugin.getLogger().info("Commands have been registered.");
    }

    /**
     * Handles command execution.
     * @param sender The sender of the command.
     * @param command The command that was executed.
     * @param label The alias of the command which was used.
     * @param args Arguments passed to the command.
     * @return true if the command was processed successfully.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Log command usage
        plugin.getLogger().info("Processing command: " + label);

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§6[ExpadorSkyblock] §eUse /island help for a list of commands.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                return handleCreateIsland(player);
            case "home":
                return handleGoToIslandHome(player);
            default:
                player.sendMessage("§6[ExpadorSkyblock] §cUnknown command. Use /island help for help.");
                return true;
        }
    }
    public void handleCreateIslandCommand(Player player) {
        if (!islandManager.playerHasIsland(player)) {
            islandManager.createIsland(player.getUniqueId(), player.getLocation());
            player.sendMessage("Your island has been created successfully.");
        } else {
            player.sendMessage("You already have an island.");
        }
    }

    public void handleTeleportHomeCommand(Player player) {
        if (islandManager.playerHasIsland(player)) {
            islandManager.teleportToIslandHome(player);
            player.sendMessage("Teleported to your island home.");
        } else {
            player.sendMessage("You do not have an island to teleport to.");
        }
    }

    private boolean handleCreateIsland(Player player) {
        // Check if the player already has an island
        if (plugin.getIslandManager().playerHasIsland(player)) {
            player.sendMessage("§6[ExpadorSkyblock] §cYou already own an island.");
            return true;
        }

        // Create the island
        if (plugin.getIslandManager().createIsland(player.getUniqueId(), player.getLocation())) {
            player.sendMessage("§6[ExpadorSkyblock] §aIsland created successfully!");
        } else {
            player.sendMessage("§6[ExpadorSkyblock] §cFailed to create an island. Please try again later.");
        }
        return true;
    }


    private boolean handleGoToIslandHome(Player player) {
        // Check if the player has an island
        if (!plugin.getIslandManager().playerHasIsland(player)) {
            player.sendMessage("§6[ExpadorSkyblock] §cYou do not own an island.");
            return true;
        }

        // Teleport the player
        if (plugin.getIslandManager().teleportToIslandHome(player)) {
            player.sendMessage("§6[ExpadorSkyblock] §aTeleported to your island home.");
        } else {
            player.sendMessage("§6[ExpadorSkyblock] §cFailed to teleport. Please try again later.");
        }
        return true;
    }

    /**
     * Handles tab completion for commands.
     * @param sender The sender of the command.
     * @param command The command for which tab completion is being executed.
     * @param alias The alias of the command.
     * @param args The current arguments passed to the command.
     * @return A list of available completions.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("island")) {
            if (args.length == 1) {
                List<String> options = new ArrayList<>();
                options.add("create");
                options.add("home");
                // Add more command options as needed
                return options;
            }
        }
        return null;
    }
}
