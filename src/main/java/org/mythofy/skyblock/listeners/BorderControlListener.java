package org.mythofy.skyblock.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.mythofy.skyblock.managers.IslandManager;
import org.mythofy.skyblock.Island;

import java.util.UUID;
import java.util.Optional;

public class BorderControlListener implements Listener {
    private final IslandManager islandManager;

    public BorderControlListener(IslandManager islandManager) {
        this.islandManager = islandManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        Optional<Island> optionalIsland = islandManager.getIslandByOwner(playerUUID);

        if (optionalIsland.isPresent()) {
            Island island = optionalIsland.get();
            Location homeLocation = island.getHomeLocation();
            if (!isInsideBorder(event.getTo(), homeLocation)) {
                // Handle the player being outside their island border
                player.sendMessage("You are outside your island border!");
                event.setCancelled(true);
            }
        }
    }

    private boolean isInsideBorder(Location playerLocation, Location islandHome) {
        // Implement your logic to check if the player is inside the border based on islandHome
        // For example, a simple distance check can be used
        double borderRadius = 100.0; // Example border radius
        return playerLocation.distance(islandHome) <= borderRadius;
    }
}
