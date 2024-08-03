package org.mythofy.skyblock.managers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.mythofy.skyblock.ExpadorSkyblock;

import java.util.logging.Logger;

public class WorldManager {

    private final ExpadorSkyblock plugin;
    private final Logger logger;

    /**
     * Constructor for WorldManager.
     *
     * @param plugin The main plugin instance.
     */
    public WorldManager(ExpadorSkyblock plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        logger.info("WorldManager initialized.");
    }

    /**
     * Creates or loads the Skyblock world.
     *
     * @param worldName The name of the world to create or load.
     * @return The created or loaded world.
     */
    public World createOrLoadWorld(String worldName) {
        logger.info("Creating or loading world: " + worldName);
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            WorldCreator creator = new WorldCreator(worldName);
            creator.environment(World.Environment.NORMAL);
            creator.type(WorldType.FLAT);
            creator.generateStructures(false);
            world = creator.createWorld();
            logger.info("World " + worldName + " created.");
        } else {
            logger.info("World " + worldName + " loaded.");
        }
        return world;
    }

    /**
     * Teleports a player to the specified world.
     *
     * @param player    The player to teleport.
     * @param worldName The name of the world to teleport to.
     */
    public void teleportPlayerToWorld(Player player, String worldName) {
        logger.info("Teleporting player " + player.getName() + " to world: " + worldName);
        World world = createOrLoadWorld(worldName);
        if (world != null) {
            player.teleport(world.getSpawnLocation());
            player.sendMessage("§aTeleported to world: §e" + worldName);
            logger.info("Player " + player.getName() + " teleported to world: " + worldName);
        } else {
            player.sendMessage("§cFailed to teleport to world: §e" + worldName);
            logger.warning("Failed to teleport player " + player.getName() + " to world: " + worldName);
        }
    }

    /**
     * Sets the spawn location of the specified world.
     *
     * @param worldName The name of the world.
     * @param x         The x-coordinate of the spawn location.
     * @param y         The y-coordinate of the spawn location.
     * @param z         The z-coordinate of the spawn location.
     */
    public void setWorldSpawn(String worldName, int x, int y, int z) {
        logger.info("Setting spawn location for world: " + worldName);
        World world = createOrLoadWorld(worldName);
        if (world != null) {
            world.setSpawnLocation(x, y, z);
            logger.info("Spawn location set for world " + worldName + " to [" + x + ", " + y + ", " + z + "].");
        } else {
            logger.warning("Failed to set spawn location for world: " + worldName);
        }
    }

    /**
     * Deletes the specified world.
     *
     * @param worldName The name of the world to delete.
     */
    public void deleteWorld(String worldName) {
        logger.info("Deleting world: " + worldName);
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            for (Player player : world.getPlayers()) {
                player.kickPlayer("§cWorld is being deleted. Please rejoin.");
            }
            Bukkit.unloadWorld(world, false);
            // Add code to delete world folder if necessary.
            logger.info("World " + worldName + " deleted.");
        } else {
            logger.warning("World " + worldName + " not found. Cannot delete.");
        }
    }

    /**
     * Sets up the specified world by creating/loading it and setting its spawn location.
     *
     * @param worldName The name of the world.
     * @param spawnX    The x-coordinate of the spawn location.
     * @param spawnY    The y-coordinate of the spawn location.
     * @param spawnZ    The z-coordinate of the spawn location.
     */
    public void setupWorld(String worldName, int spawnX, int spawnY, int spawnZ) {
        logger.info("Setting up world: " + worldName);
        World world = createOrLoadWorld(worldName);
        if (world != null) {
            setWorldSpawn(worldName, spawnX, spawnY, spawnZ);
            logger.info("World " + worldName + " setup complete with spawn location at [" + spawnX + ", " + spawnY + ", " + spawnZ + "].");
        } else {
            logger.warning("Failed to setup world: " + worldName);
        }
    }
}
