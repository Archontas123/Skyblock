package org.mythofy.skyblock.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mythofy.skyblock.ConfigHandler;
import org.mythofy.skyblock.ExpadorSkyblock;
import org.mythofy.skyblock.Island;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class IslandManager {
    private final ExpadorSkyblock plugin;
    private final HashMap<UUID, Island> playerIslands;
    private HashMap<UUID, Island> islands;



    public IslandManager(ExpadorSkyblock plugin) {
        this.plugin = plugin;
        this.playerIslands = new HashMap<>();
        islands = new HashMap<>();

    }
    public boolean isWithinBorders(Island island, Location location) {
        Location home = island.getHomeLocation();
        int borderSize = island.getBorderSize();
        int halfSize = borderSize / 2;

        return location.getX() >= home.getX() - halfSize && location.getX() <= home.getX() + halfSize &&
                location.getZ() >= home.getZ() - halfSize && location.getZ() <= home.getZ() + halfSize;
    }

    public boolean playerHasIsland(Player player) {
        boolean hasIsland = playerIslands.containsKey(player.getUniqueId());
        plugin.getLogger().info("Checking if player " + player.getName() + " has an island: " + hasIsland);
        return hasIsland;
    }
    public Optional<Island> getIslandByOwner(UUID ownerUUID) {
        return Optional.ofNullable(islands.get(ownerUUID));
    }
    public boolean createIsland(UUID ownerUUID, Location homeLocation) {
        // Assuming a default level of 1 and the default world as the world's location provided
        int defaultLevel = 1;
        World defaultWorld = homeLocation.getWorld();

        // Ensure defaultWorld is not null
        if (defaultWorld == null) {
            defaultWorld = plugin.getServer().getWorlds().get(0);
        }

        Island island = new Island(ownerUUID, homeLocation, defaultLevel, defaultWorld, 200);
        islands.put(ownerUUID, island);
        plugin.getLogger().info("Island created for owner UUID: " + ownerUUID);
        return true;
    }


    private Location calculateIslandLocation() {
        // Dummy implementation, replace with actual location calculation logic
        return new Location(plugin.getServer().getWorld("world"), 0, 64, 0); // Example location
    }
    public boolean visitIsland(Player visitor, String targetPlayerName) {
        Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);
        if (targetPlayer != null && playerHasIsland(targetPlayer)) {
            Island targetIsland = getIslandOfPlayer(targetPlayer);
            if (targetIsland.isPublic() || visitor.hasPermission("expadorskyblock.admin.visit")) {
                visitor.teleport(targetIsland.getHomeLocation());
                return true;
            }
        }
        return false;
    }
    public Island getIslandOfPlayer(Player player) {
        UUID playerId = player.getUniqueId();
        return islands.get(playerId);
    }

    public boolean setPrivacy(Player player, boolean isPublic) {
        if (playerHasIsland(player)) {
            Island island = getIslandOfPlayer(player);
            island.setPublic(isPublic);
            return true;
        }
        return false;
    }

    private void setWorldBorderForIsland(Player player, Island island) {
        player.setWorldBorder(player.getWorld().getWorldBorder()); // Example setup, customize accordingly
        plugin.getLogger().info("World border set for " + player.getName() + "'s island.");
    }

    public boolean teleportToIslandHome(Player player) {
        if (!playerHasIsland(player)) {
            plugin.getLogger().info("Attempt to teleport " + player.getName() + " to island home denied. No island found.");
            return false;
        }

        Island island = playerIslands.get(player.getUniqueId());
        player.teleport(island.getHomeLocation());
        plugin.getLogger().info("Player " + player.getName() + " teleported to island home.");
        return true;
    }

    public boolean deleteIsland(Player player) {
        if (!playerHasIsland(player)) {
            plugin.getLogger().info("Attempt to delete island for " + player.getName() + " denied. No island found.");
            return false;
        }

        playerIslands.remove(player.getUniqueId());
        plugin.getLogger().info("Island for " + player.getName() + " deleted.");
        return true;
    }

    public boolean adminDeleteIsland(Player player, UUID targetPlayerId) {
        if (!playerIslands.containsKey(targetPlayerId)) {
            plugin.getLogger().info("Admin attempt to delete island for " + player.getName() + " denied. No island found for target.");
            return false;
        }

        playerIslands.remove(targetPlayerId);
        plugin.getLogger().info("Admin " + player.getName() + " deleted the island of " + plugin.getServer().getPlayer(targetPlayerId).getName());
        return true;
    }
}
