package org.mythofy.skyblock.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mythofy.skyblock.ExpadorSkyblock;
import org.mythofy.skyblock.Island;
import org.mythofy.skyblock.IslandStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class IslandManager {
    private static final int ISLAND_SPACING = 456;
    private final ExpadorSkyblock plugin;
    private final HashMap<UUID, Island> islands;
    private final IslandStorage islandStorage;

    public IslandManager(ExpadorSkyblock plugin) {
        this.plugin = plugin;
        this.islands = new HashMap<>();
        this.islandStorage = new IslandStorage(plugin);
        loadIslands();
    }

    public boolean isWithinBorders(Island island, Location location) {
        Location home = island.getHomeLocation();
        int borderSize = island.getBorderSize();
        int halfSize = borderSize / 2;

        return location.getX() >= home.getX() - halfSize && location.getX() <= home.getX() + halfSize &&
                location.getZ() >= home.getZ() - halfSize && location.getZ() <= home.getZ() + halfSize;
    }

    public boolean playerHasIsland(Player player) {
        return islands.containsKey(player.getUniqueId());
    }

    public Optional<Island> getIslandByOwner(UUID ownerUUID) {
        return Optional.ofNullable(islands.get(ownerUUID));
    }

    public boolean createIsland(UUID ownerUUID, Location homeLocation) {
        World islandWorld = Bukkit.getWorld("IslandWorld");

        // If IslandWorld is not found, create it
        if (islandWorld == null) {
            plugin.getLogger().info("IslandWorld not found. Creating it now.");
            plugin.getWorldManager().setupWorld();
            islandWorld = Bukkit.getWorld("IslandWorld");

            // If IslandWorld is still null after creation attempt, return false
            if (islandWorld == null) {
                plugin.getLogger().info("Failed to create IslandWorld. Unable to create island.");
                return false;
            }
        }

        Location islandHomeLocation = calculateNextIslandLocation(islandWorld);
        Island island = new Island(ownerUUID, islandHomeLocation, 1, islandWorld, 200);
        islands.put(ownerUUID, island);
        islandStorage.saveIsland(island);
        plugin.getLogger().info("Island created for owner UUID: " + ownerUUID + " at location: " + islandHomeLocation);

        islandHomeLocation.getBlock().setType(Material.GOLD_BLOCK);
        islandHomeLocation.clone().add(0, 1, 0).getBlock().setType(Material.DIAMOND_BLOCK);

        return true;
    }

    private Location calculateNextIslandLocation(World islandWorld) {
        int index = islands.size();
        int row = index / 4;
        int col = index % 4;

        int x = col * ISLAND_SPACING;
        int z = row * ISLAND_SPACING;

        return new Location(islandWorld, x, 64, z);
    }

    private void loadIslands() {
        Map<UUID, Island> loadedIslands = islandStorage.loadIslands();
        islands.putAll(loadedIslands);
        plugin.getLogger().info("Islands have been loaded.");
    }

    public Map<UUID, Island> getIslands() {
        return islands;
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

    public void setIslandHome(Player player, Location homeLocation) {
        if (playerHasIsland(player)) {
            Island island = getIslandOfPlayer(player);
            Location oldHome = island.getHomeLocation();

            // Remove the old gold block
            if (oldHome != null) {
                oldHome.getBlock().setType(Material.AIR);
            }

            // Set new home location and place the gold block
            homeLocation.getBlock().setType(Material.GOLD_BLOCK);
            island.setHomeLocation(homeLocation);
            islandStorage.saveIsland(island);
            player.sendMessage("Island home location set successfully.");
        } else {
            player.sendMessage("You do not have an island to set home for.");
        }
    }

    public boolean teleportToIslandHome(Player player) {
        Optional<Island> islandOptional = getIslandByOwner(player.getUniqueId());
        if (islandOptional.isPresent()) {
            Island island = islandOptional.get();
            Location homeLocation = island.getHomeLocation();
            player.teleport(homeLocation);
            return true;
        } else {
            plugin.getLogger().info("Island home teleport failed for player: " + player.getName());
            return false;
        }
    }

    public boolean deleteIsland(Player player) {
        if (!playerHasIsland(player)) {
            plugin.getLogger().info("Attempt to delete island for " + player.getName() + " denied. No island found.");
            return false;
        }

        islands.remove(player.getUniqueId());
        plugin.getLogger().info("Island for " + player.getName() + " deleted.");
        return true;
    }

    public boolean adminDeleteIsland(Player player, UUID targetPlayerId) {
        if (!islands.containsKey(targetPlayerId)) {
            plugin.getLogger().info("Admin attempt to delete island for " + player.getName() + " denied. No island found for target.");
            return false;
        }

        islands.remove(targetPlayerId);
        plugin.getLogger().info("Admin " + player.getName() + " deleted the island of " + plugin.getServer().getPlayer(targetPlayerId).getName());
        return true;
    }

    public void saveIslands() {
        islandStorage.saveIslands(islands);
        plugin.getLogger().info("Islands have been saved.");
    }
}
