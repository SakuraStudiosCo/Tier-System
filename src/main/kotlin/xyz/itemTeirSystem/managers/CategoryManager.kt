package xyz.itemTeirSystem.managers

import net.kyori.adventure.text.format.TextColor
import xyz.itemTeirSystem.models.ItemCategory

class CategoryManager {
    private val categories = mutableMapOf<String, ItemCategory>()

    fun loadDefaultCategories() {
        registerCategory(
            ItemCategory(
                name = "FURNITURE",
                description = "Decorative items for building",
                color = TextColor.color(0xDEB887)
            )
        )
        registerCategory(
            ItemCategory(
                name = "WEAPON",
                description = "Combat items",
                color = TextColor.color(0xFF4444)
            )
        )
        registerCategory(
            ItemCategory(
                name = "ARMOR",
                description = "Protective items",
                color = TextColor.color(0x4444FF)
            )
        )
        registerCategory(
            ItemCategory(
                name = "RESOURCE",
                description = "Materials and resources",
                color = TextColor.color(0xFFD700) // Gold
            )
        )
        registerCategory(
            ItemCategory(
                name = "TOOL",
                description = "Utility items",
                color = TextColor.color(0x44FF44)
            )
        )
    }

    fun registerCategory(category: ItemCategory) {
        categories[category.name.uppercase()] = category
    }

    fun getCategory(name: String): ItemCategory? = categories[name.uppercase()]

    fun getAllCategories(): List<ItemCategory> = categories.values.toList()
}