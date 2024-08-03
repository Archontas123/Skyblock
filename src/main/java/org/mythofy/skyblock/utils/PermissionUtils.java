package org.mythofy.skyblock.utils;

import org.bukkit.entity.Player;
import org.mythofy.skyblock.ExpadorSkyblock;
import org.mythofy.skyblock.Island;
import org.mythofy.skyblock.managers.IslandManager;

public class PermissionUtils {

    private final ExpadorSkyblock plugin;

    /**
     * Constructor for PermissionUtils.
     *
     * @param plugin The main plugin instance.
     */
    public PermissionUtils(ExpadorSkyblock plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks if a player has permission to create an island.
     *
     * @param player The player to check.
     * @return True if the player has permission, false otherwise.
     */
    public boolean canCreateIsland(Player player) {
        return player.hasPermission("expadorskyblock.create");
    }

    /**
     * Checks if a player has permission to manage an island.
     *
     * @param player The player to check.
     * @param island The island to check against.
     * @return True if the player has permission, false otherwise.
     */
    public boolean canManageIsland(Player player, Island island) {
        return island.getOwner().equals(player.getUniqueId()) || player.hasPermission("expadorskyblock.manage");
    }

    /**
     * Checks if a player has permission to visit an island.
     *
     * @param player The player to check.
     * @param island The island to check against.
     * @return True if the player has permission, false otherwise.
     */
    public boolean canVisitIsland(Player player, Island island) {
        IslandManager islandManager = plugin.getIslandManager();
        return island.isPublic() || island.getOwner().equals(player.getUniqueId()) || player.hasPermission("expadorskyblock.visit");
    }

    /**
     * Checks if a player has permission to delete an island.
     *
     * @param player The player to check.
     * @param island The island to check against.
     * @return True if the player has permission, false otherwise.
     */
    public boolean canDeleteIsland(Player player, Island island) {
        return island.getOwner().equals(player.getUniqueId()) || player.hasPermission("expadorskyblock.delete");
    }
}
