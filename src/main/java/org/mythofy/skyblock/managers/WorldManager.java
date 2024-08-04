package org.mythofy.skyblock.managers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.mythofy.skyblock.ExpadorSkyblock;

import java.util.Random;
import java.util.logging.Logger;

public class WorldManager {
    private final ExpadorSkyblock plugin;
    private final Logger logger;

    public WorldManager(ExpadorSkyblock plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        logger.info("WorldManager initialized.");
    }

    public void setupWorld() {
        String worldName = "IslandWorld";
        World islandWorld = Bukkit.getWorld(worldName);

        if (islandWorld == null) {
            logger.info("IslandWorld not found. Creating a new void world named IslandWorld.");
            WorldCreator worldCreator = new WorldCreator(worldName);
            worldCreator.environment(World.Environment.NORMAL);
            worldCreator.type(WorldType.FLAT);
            worldCreator.generator(new VoidWorldGenerator());

            islandWorld = worldCreator.createWorld();
            if (islandWorld != null) {
                islandWorld.setSpawnLocation(0, 64, 0);
                logger.info("IslandWorld created successfully as a void world.");
            } else {
                logger.severe("Failed to create IslandWorld.");
            }
        } else {
            logger.info("IslandWorld already exists.");
        }
    }

    private static class VoidWorldGenerator extends ChunkGenerator {
        @Override
        public ChunkGenerator.ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
            return createChunkData(world);
        }
    }

    public World createOrLoadWorld(String worldName) {
        logger.info("Creating or loading world: " + worldName);
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            WorldCreator creator = new WorldCreator(worldName);
            creator.environment(World.Environment.NORMAL);
            creator.type(WorldType.FLAT);
            creator.generateStructures(false);
            creator.generator(new VoidWorldGenerator());
            world = creator.createWorld();
            logger.info("World " + worldName + " created.");
        } else {
            logger.info("World " + worldName + " loaded.");
        }
        return world;
    }

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
