package xyz.itemTeirSystem.managers

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import xyz.itemTeirSystem.ItemTeirSystem
import xyz.itemTeirSystem.models.TieredItem

class ItemManager(private val plugin: ItemTeirSystem) {

    fun createTieredItem(material: Material, tierName: String, categoryName: String, name: String): TieredItem {
        val tier = plugin.tierManager.getTier(tierName)
            ?: throw IllegalArgumentException("Invalid tier: $tierName")
        val category = plugin.categoryManager.getCategory(categoryName)
            ?: throw IllegalArgumentException("Invalid category: $categoryName")

        return TieredItem(material, tier, category, name, plugin)
    }

    fun isTieredItem(item: ItemStack): Boolean {
        return TieredItem.fromItemStack(item, plugin) != null
    }

    fun getTieredItem(item: ItemStack): TieredItem? {
        return TieredItem.fromItemStack(item, plugin)
    }
}