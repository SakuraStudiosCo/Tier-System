package xyz.itemTeirSystem


import org.bukkit.plugin.java.JavaPlugin
import xyz.itemTeirSystem.config.BlockTierConfig
import xyz.itemTeirSystem.managers.CategoryManager
import xyz.itemTeirSystem.managers.ItemManager
import xyz.itemTeirSystem.managers.TierManager
import xyz.itemTeirSystem.listeners.ItemEventListener
import xyz.itemTeirSystem.listeners.ItemGuiListener
import xyz.itemTeirSystem.listeners.ItemInteractionListener


class ItemTeirSystem : JavaPlugin() {
    lateinit var blockTierConfig: BlockTierConfig
        private set
    lateinit var tierManager: TierManager
        private set
    lateinit var categoryManager: CategoryManager
        private set
    lateinit var itemManager: ItemManager
        private set

    override fun onEnable() {

        // Create plugin folder if it doesn't exist
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }

        // Initialize managers
        initializeManagers()

        // Register events
        registerEvents()

        // Register commands
        registerCommands()

        // Load default data
        loadDefaultData()

        logger.info("ItemTeirSystem has been enabled!")
    }

    override fun onDisable() {
        logger.info("ItemTeirSystem has been disabled!")
    }

    private fun initializeManagers() {
        blockTierConfig = BlockTierConfig(this)
        tierManager = TierManager()
        categoryManager = CategoryManager()
        itemManager = ItemManager(this)
    }

    private fun registerEvents() {
        server.pluginManager.registerEvents(ItemEventListener(this), this)
        server.pluginManager.registerEvents(ItemInteractionListener(this), this)
        server.pluginManager.registerEvents(ItemGuiListener(this), this)
    }

    private fun registerCommands() {
    }

    private fun loadDefaultData() {
        tierManager.loadDefaultTiers()
        categoryManager.loadDefaultCategories()
    }


    fun reloadAllConfigs() {
        blockTierConfig.loadConfig()
        // Add other config reloads here as needed
    }
}