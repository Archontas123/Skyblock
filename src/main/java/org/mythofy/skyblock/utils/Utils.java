package org.mythofy.skyblock.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.World;

public class Utils {

    /**
     * Colors a message using Minecraft color codes.
     *
     * @param message The message to colorize.
     * @return The colorized message.
     */
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Sends a colored message to a player.
     *
     * @param player  The player to send the message to.
     * @param message The message to send.
     */
    public static void sendMessage(Player player, String message) {
        player.sendMessage(colorize(message));
    }

    /**
     * Formats a Location object to a readable string.
     *
     * @param location The location to format.
     * @return The formatted location string.
     */
    public static String formatLocation(Location location) {
        return "World: " + location.getWorld().getName() + ", X: " + location.getBlockX() + ", Y: " + location.getBlockY() + ", Z: " + location.getBlockZ();
    }

    /**
     * Checks if a player is in a specific world.
     *
     * @param player The player to check.
     * @param world  The world to check against.
     * @return True if the player is in the specified world, false otherwise.
     */
    public static boolean isInWorld(Player player, World world) {
        return player.getWorld().equals(world);
    }
}
