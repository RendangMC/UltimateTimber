package com.songoda.ultimatetimber;

import com.songoda.ultimatetimber.commands.CommandGiveAxe;
import com.songoda.ultimatetimber.commands.CommandReload;
import com.songoda.ultimatetimber.commands.CommandToggle;
import com.songoda.ultimatetimber.manager.ChoppingManager;
import com.songoda.ultimatetimber.manager.ConfigurationManager;
import com.songoda.ultimatetimber.manager.Manager;
import com.songoda.ultimatetimber.manager.PlacedBlockManager;
import com.songoda.ultimatetimber.manager.SaplingManager;
import com.songoda.ultimatetimber.manager.TreeAnimationManager;
import com.songoda.ultimatetimber.manager.TreeDefinitionManager;
import com.songoda.ultimatetimber.manager.TreeDetectionManager;
import com.songoda.ultimatetimber.manager.TreeFallManager;
import org.stegripe.songoda.configuration.Config;
import org.stegripe.songoda.SongodaPlugin;
import org.stegripe.songoda.commands.CommandManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UltimateTimber extends SongodaPlugin {

    private static UltimateTimber INSTANCE;
    private Set<Manager> managers;
    private ChoppingManager choppingManager;
    private ConfigurationManager configurationManager;
    private CommandManager commandManager;
    private PlacedBlockManager placedBlockManager;
    private SaplingManager saplingManager;
    private TreeAnimationManager treeAnimationManager;
    private TreeDefinitionManager treeDefinitionManager;
    private TreeDetectionManager treeDetectionManager;
    private TreeFallManager treeFallManager;

    public static UltimateTimber getInstance() {
        return INSTANCE;
    }

    @Override
    public void onPluginLoad() {
        INSTANCE = this;
    }

    @Override
    public void onPluginEnable() {
        // Load hooks
        // Setup plugin commands
        this.commandManager = new CommandManager(this);
        this.commandManager.addMainCommand("ut")
                .addSubCommands(
                        new CommandReload(this),
                        new CommandToggle(this),
                        new CommandGiveAxe(this)
                );

        // Register managers
        this.managers = new HashSet<>();
        this.choppingManager = this.registerManager(ChoppingManager.class);
        this.configurationManager = new ConfigurationManager(this);
        this.placedBlockManager = this.registerManager(PlacedBlockManager.class);
        this.saplingManager = this.registerManager(SaplingManager.class);
        this.treeAnimationManager = this.registerManager(TreeAnimationManager.class);
        this.treeDefinitionManager = this.registerManager(TreeDefinitionManager.class);
        this.treeDetectionManager = this.registerManager(TreeDetectionManager.class);
        this.treeFallManager = this.registerManager(TreeFallManager.class);

        this.reloadConfig();
    }

    @Override
    public void onPluginDisable() {
        this.disable();
    }

    @Override
    public void onDataLoad() {
    }

    @Override
    public void onConfigReload() {
        this.configurationManager.reload();
        this.managers.forEach(Manager::reload);
    }

    @Override
    public List<Config> getExtraConfig() {
        return null;
    }

    /**
     * Disables most of the plugin
     */
    public void disable() {
        this.configurationManager.disable();
        this.managers.forEach(Manager::disable);
    }

    /**
     * Registers a manager
     *
     * @param managerClass The class of the manager to create a new instance of
     * @param <T>          extends Manager
     * @return A new instance of the given manager class
     */
    private <T extends Manager> T registerManager(Class<T> managerClass) {
        try {
            T newManager = managerClass.getConstructor(UltimateTimber.class).newInstance(this);
            this.managers.add(newManager);
            return newManager;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the chopping manager
     *
     * @return The ChoppingManager instance
     */
    public ChoppingManager getChoppingManager() {
        return this.choppingManager;
    }

    /**
     * Gets the configuration manager
     *
     * @return The ConfigurationManager instance
     */
    public ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    /**
     * Gets the placed block manager
     *
     * @return The PlacedBlockManager instance
     */
    public PlacedBlockManager getPlacedBlockManager() {
        return this.placedBlockManager;
    }

    /**
     * Gets the sapling manager
     *
     * @return The SaplingManager instance
     */
    public SaplingManager getSaplingManager() {
        return this.saplingManager;
    }

    /**
     * Gets the tree animation manager
     *
     * @return The TreeAnimationManager instance
     */
    public TreeAnimationManager getTreeAnimationManager() {
        return this.treeAnimationManager;
    }

    /**
     * Gets the tree definition manager
     *
     * @return The TreeDefinitionManager instance
     */
    public TreeDefinitionManager getTreeDefinitionManager() {
        return this.treeDefinitionManager;
    }

    /**
     * Gets the tree detection manager
     *
     * @return The TreeDetectionManager instance
     */
    public TreeDetectionManager getTreeDetectionManager() {
        return this.treeDetectionManager;
    }

    /**
     * Gets the tree fall manager
     *
     * @return The TreeFallManager instance
     */
    public TreeFallManager getTreeFallManager() {
        return this.treeFallManager;
    }

}
