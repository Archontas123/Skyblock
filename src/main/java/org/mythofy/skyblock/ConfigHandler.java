package org.mythofy.skyblock;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigHandler {

    private final ExpadorSkyblock plugin;
    private FileConfiguration config;
    private File configFile;
    private final Logger logger;
    private static int defaultBorderSize;


    /**
     * Constructor for ConfigHandler.
     *
     * @param plugin The main plugin instance.
     */
    public ConfigHandler(ExpadorSkyblock plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        createConfig();
        loadConfig();
    }

    /**
     * Creates the configuration file if it doesn't exist.
     */
    private void createConfig() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            logger.info("Creating default config file...");
            configFile.getParentFile().mkdirs();
            plugin.saveResource("config.yml", false);
        }
    }

    /**
     * Loads the configuration from the file.
     */
    public void loadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        logger.info("Config file loaded successfully.");
        defaultBorderSize = config.getInt("defaultBorderSize", 200);

    }

    /**
     * Saves the current configuration to the file.
     */
    public void saveConfig() {
        try {
            config.save(configFile);
            logger.info("Config file saved successfully.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not save config file", e);
        }
    }

    /**
     * Gets the configuration object.
     *
     * @return The FileConfiguration object.
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Reloads the configuration from the file.
     */
    public void reloadConfig() {
        loadConfig();
        logger.info("Config file reloaded successfully.");
    }

    // Add getters for specific configuration values as needed.

    /**
     * Gets the default island level from the configuration.
     *
     * @return The default island level.
     */
    public int getDefaultIslandLevel() {
        return config.getInt("defaultIslandLevel", 1);
    }
    public static int getDefaultBorderSize() {
        return defaultBorderSize;
    }
    /**
     * Gets the maximum island level from the configuration.
     *
     * @return The maximum island level.
     */
    public int getMaxIslandLevel() {
        return config.getInt("maxIslandLevel", 10);
    }

    /**
     * Gets the initial island world name from the configuration.
     *
     * @return The initial island world name.
     */
    public String getInitialIslandWorldName() {
        return config.getString("initialIslandWorldName", "world");
    }
}
