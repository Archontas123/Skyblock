package org.mythofy.skyblock.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.mythofy.skyblock.managers.IslandManager;
import org.mythofy.skyblock.Island;

import java.util.Optional;
import java.util.UUID;

public class BorderControlListener implements Listener {
    private final IslandManager islandManager;

    public BorderControlListener(IslandManager islandManager) {
        this.islandManager = islandManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        UUID playerUUID = player.getUniqueId();
        Optional<Island> islandOptional = islandManager.getIslandByOwner(playerUUID);

        if (islandOptional.isPresent()) {
            Island island = islandOptional.get();
            if (!islandManager.isWithinBorders(island, to)) {
                // Check if the player is within another island's border
                for (Island otherIsland : islandManager.getIslands().values()) {
                    if (!otherIsland.getOwnerUUID().equals(playerUUID) && islandManager.isWithinBorders(otherIsland, to)) {
                        return; // Allow movement within the visited island
                    }
                }

                // Prevent movement outside the island's border
                event.setCancelled(true);
                player.sendMessage("You cannot leave your island's border!");
            }
        }
    }
}
