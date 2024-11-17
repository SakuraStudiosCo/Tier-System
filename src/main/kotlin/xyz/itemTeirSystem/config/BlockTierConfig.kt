package xyz.itemTeirSystem.config

import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import xyz.itemTeirSystem.ItemTeirSystem
import java.io.File

class BlockTierConfig(private val plugin: ItemTeirSystem) {
    private val configFile: File = File(plugin.dataFolder, "block_tiers.yml")
    private val config: YamlConfiguration = YamlConfiguration()

    data class BlockTierData(val tier: String, val category: String)

    private var blockTiers: Map<Material, BlockTierData> = emptyMap()

    init {
        createDefaultConfig()
        loadConfig()
    }

    fun getBlockTierData(material: Material): BlockTierData? {
        return blockTiers[material]
    }

    private fun createDefaultConfig() {
        if (!configFile.exists()) {
            plugin.saveResource("block_tiers.yml", false)
        }
    }

    fun loadConfig() {
        config.load(configFile)

        val loadedTiers = mutableMapOf<Material, BlockTierData>()

        // Load each section from the config
        for (key in config.getKeys(false)) {
            val section = config.getConfigurationSection(key) ?: continue

            try {
                val material = Material.valueOf(key.uppercase())
                val tier = section.getString("tier") ?: continue
                val category = section.getString("category") ?: continue

                loadedTiers[material] = BlockTierData(tier, category)
            } catch (e: IllegalArgumentException) {
                plugin.logger.warning("Invalid material in config: $key")
            }
        }

        blockTiers = loadedTiers
    }

    fun reloadConfig() {
        loadConfig()
    }
}