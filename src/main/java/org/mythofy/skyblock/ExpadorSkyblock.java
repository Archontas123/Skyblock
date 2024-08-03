package org.mythofy.skyblock;

import org.bukkit.plugin.java.JavaPlugin;
import org.mythofy.skyblock.commands.CommandManager;
import org.mythofy.skyblock.listeners.BorderControlListener;
import org.mythofy.skyblock.managers.IslandManager;
import org.mythofy.skyblock.managers.WorldManager;

/**
 * The main class for the ExpadorSkyblock plugin, handling the initialization and shutdown of the plugin.
 */
public class ExpadorSkyblock extends JavaPlugin {

    private static ExpadorSkyblock instance;

    private CommandManager commandManager;
    private IslandManager islandManager;
    private WorldManager worldManager;

    /**
     * Provides access to the plugin instance.
     *
     * @return the singleton instance of the plugin.
     */
    public static ExpadorSkyblock getInstance() {
        return instance;
    }

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        instance = this;
        initManagers();
        getLogger().info("ExpadorSkyblock has been enabled!");
        getServer().getPluginManager().registerEvents(new BorderControlListener(new IslandManager(this)), this);

    }

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        getLogger().info("ExpadorSkyblock has been disabled!");
    }

    /**
     * Initializes all the managers needed for the plugin.
     */
    private void initManagers() {
        this.commandManager = new CommandManager(this);
        this.islandManager = new IslandManager(this);
        this.worldManager = new WorldManager(this);

        // Register all commands through the command manager
        commandManager.registerCommands();

        // Initialize the world settings for skyblock
        String skyblockWorldName = "skyblock_world"; // Define your skyblock world name
        int spawnX = 0; // Define your default spawn X coordinate
        int spawnY = 100; // Define your default spawn Y coordinate
        int spawnZ = 0; // Define your default spawn Z coordinate

        worldManager.setupWorld(skyblockWorldName, spawnX, spawnY, spawnZ);

        getLogger().info("All managers have been initialized successfully.");
    }


    // Getters for managers to be used elsewhere in the plugin
    public CommandManager getCommandManager() {
        return commandManager;
    }

    public IslandManager getIslandManager() {
        return islandManager;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }
}
