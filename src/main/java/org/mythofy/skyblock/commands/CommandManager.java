package org.mythofy.skyblock.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
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

    private final ExpadorSkyblock plugin;
    private final IslandManager islandManager;

    /**
     * Constructor to create an instance of CommandManager.
     * @param plugin The main class instance of the ExpadorSkyblock plugin.
     */
    public CommandManager(ExpadorSkyblock plugin) {
        this.plugin = plugin;
        this.islandManager = plugin.getIslandManager(); // Correctly set the islandManager
    }

    /**
     * Registers all commands used in the ExpadorSkyblock plugin.
     */
    public void registerCommands() {
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        String commandName = args.length > 0 ? args[0].toLowerCase() : "";

        switch (commandName) {
            case "create":
                handleCreateIslandCommand(player);
                break;
            case "home":
                handleGoToIslandHome(player);
                break;
            case "sethome":
                handleSetHomeCommand(player);
                break;
            case "visit":
                if (args.length > 1) {
                    handleVisitIslandCommand(player, args[1]);
                } else {
                    player.sendMessage("§6[ExpadorSkyblock] §cPlease specify a player to visit.");
                }
                break;
            case "help":
                handleHelpCommand(player);
                break;
            default:
                player.sendMessage("§6[ExpadorSkyblock] §cUnknown command. Use /island help for a list of commands.");
        }
        return true;
    }
    void handleTeleportHomeCommand(Player player) {
        if (islandManager.playerHasIsland(player)) {
            islandManager.teleportToIslandHome(player);
            player.sendMessage("Teleported to your island home.");
        } else {
            player.sendMessage("You do not have an island to teleport to.");
        }
    }
    void handleCreateIslandCommand(Player player) {
        if (!islandManager.playerHasIsland(player)) {
            Location homeLocation = player.getLocation(); // Set the player's current location as the home location
            if (islandManager.createIsland(player.getUniqueId(), homeLocation)) {
                player.sendMessage("Your island has been created successfully.");
            } else {
                player.sendMessage("Failed to create your island. Please contact an admin.");
            }
        } else {
            player.sendMessage("You already have an island.");
        }
    }

    private void handleSetHomeCommand(Player player) {
        islandManager.setIslandHome(player, player.getLocation());
    }

    private void handleVisitIslandCommand(Player player, String targetPlayerName) {
        if (islandManager.visitIsland(player, targetPlayerName)) {
            player.sendMessage("Teleported to " + targetPlayerName + "'s island.");
        } else {
            player.sendMessage("Failed to visit " + targetPlayerName + "'s island.");
        }
    }

    private void handleHelpCommand(Player player) {
        player.sendMessage(ChatColor.YELLOW + "Available Island Commands:");
        player.sendMessage(ChatColor.GREEN + "/island create" + ChatColor.WHITE + " - Create your island.");
        player.sendMessage(ChatColor.GREEN + "/island home" + ChatColor.WHITE + " - Teleport to your island home.");
        player.sendMessage(ChatColor.GREEN + "/island sethome" + ChatColor.WHITE + " - Set your island home location.");
        player.sendMessage(ChatColor.GREEN + "/island visit <player>" + ChatColor.WHITE + " - Visit another player's island.");
        player.sendMessage(ChatColor.GREEN + "/island help" + ChatColor.WHITE + " - Display this help message.");
    }

    private boolean handleGoToIslandHome(Player player) {
        if (!islandManager.playerHasIsland(player)) {
            player.sendMessage("§6[ExpadorSkyblock] §cYou do not own an island.");
            return true;
        }

        if (islandManager.teleportToIslandHome(player)) {
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
                options.add("sethome");
                options.add("visit");
                options.add("help");
                return options;
            }
        }
        return null;
    }
}
