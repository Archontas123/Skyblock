package org.mythofy.skyblock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class IslandStorage {
    private final ExpadorSkyblock plugin;
    private final Logger logger;
    private FileConfiguration islandsConfig;
    private File islandsFile;

    public IslandStorage(ExpadorSkyblock plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        createIslandsFile();
    }

    private void createIslandsFile() {
        islandsFile = new File(plugin.getDataFolder(), "islands.yml");
        if (!islandsFile.exists()) {
            islandsFile.getParentFile().mkdirs();
            plugin.saveResource("islands.yml", false);
        }
        islandsConfig = YamlConfiguration.loadConfiguration(islandsFile);
    }

    public void saveIslands(Map<UUID, Island> islands) {
        for (Map.Entry<UUID, Island> entry : islands.entrySet()) {
            UUID ownerUUID = entry.getKey();
            Island island = entry.getValue();

            String path = "islands." + ownerUUID.toString();
            islandsConfig.set(path + ".homeLocation.world", island.getHomeLocation().getWorld().getName());
            islandsConfig.set(path + ".homeLocation.x", island.getHomeLocation().getX());
            islandsConfig.set(path + ".homeLocation.y", island.getHomeLocation().getY());
            islandsConfig.set(path + ".homeLocation.z", island.getHomeLocation().getZ());
            islandsConfig.set(path + ".homeLocation.yaw", island.getHomeLocation().getYaw());
            islandsConfig.set(path + ".homeLocation.pitch", island.getHomeLocation().getPitch());
            islandsConfig.set(path + ".level", island.getLevel());
            islandsConfig.set(path + ".isPublic", island.isPublic());
        }
        saveIslandsConfig();
        logger.info("Islands have been saved.");
    }

    public Map<UUID, Island> loadIslands() {
        Map<UUID, Island> islands = new HashMap<>();
        if (islandsConfig.contains("islands")) {
            for (String key : islandsConfig.getConfigurationSection("islands").getKeys(false)) {
                UUID ownerUUID = UUID.fromString(key);

                String worldName = islandsConfig.getString("islands." + key + ".homeLocation.world");
                World world = Bukkit.getWorld(worldName);
                double x = islandsConfig.getDouble("islands." + key + ".homeLocation.x");
                double y = islandsConfig.getDouble("islands." + key + ".homeLocation.y");
                double z = islandsConfig.getDouble("islands." + key + ".homeLocation.z");
                float yaw = (float) islandsConfig.getDouble("islands." + key + ".homeLocation.yaw");
                float pitch = (float) islandsConfig.getDouble("islands." + key + ".homeLocation.pitch");

                Location homeLocation = new Location(world, x, y, z, yaw, pitch);
                int level = islandsConfig.getInt("islands." + key + ".level");
                boolean isPublic = islandsConfig.getBoolean("islands." + key + ".isPublic");
                int size = islandsConfig.getInt("islands." + key + ".size", 200); // Default size to 200 if not specified

                Island island = new Island(ownerUUID, homeLocation, level, world, size);
                island.setPublic(isPublic);
                islands.put(ownerUUID, island);
            }
        }
        logger.info("Islands have been loaded.");
        return islands;
    }


    private void saveIslandsConfig() {
        try {
            islandsConfig.save(islandsFile);
        } catch (IOException e) {
            logger.severe("Could not save islands config: " + e.getMessage());
        }
    }
}
