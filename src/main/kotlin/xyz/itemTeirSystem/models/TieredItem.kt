package xyz.itemTeirSystem.models

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.NamespacedKey
import xyz.itemTeirSystem.ItemTeirSystem

data class TieredItem(
    val material: Material,
    val tier: ItemTier,
    val category: ItemCategory,
    val name: String,
    private val plugin: ItemTeirSystem
) {
    companion object {
        private const val TIER_KEY = "tier_name"
        private const val CATEGORY_KEY = "category_name"

        fun fromItemStack(item: ItemStack, plugin: ItemTeirSystem): TieredItem? {
            val meta = item.itemMeta ?: return null

            // Get stored tier and category from persistent data
            val tierName = meta.persistentDataContainer.get(
                NamespacedKey(plugin, TIER_KEY),
                PersistentDataType.STRING
            ) ?: return null

            val categoryName = meta.persistentDataContainer.get(
                NamespacedKey(plugin, CATEGORY_KEY),
                PersistentDataType.STRING
            ) ?: return null

            val tier = plugin.tierManager.getTier(tierName) ?: return null
            val category = plugin.categoryManager.getCategory(categoryName) ?: return null

            // Use a standard name format instead of the display name
            val standardName = "${tier.name} ${item.type.name}"

            return TieredItem(item.type, tier, category, standardName, plugin)
        }
    }

    fun toItemStack(): ItemStack {
        return ItemStack(material).apply {
            editMeta { meta ->
                // Store tier and category data
                storePersistentData(meta)

                // Set display name
                meta.displayName(
                    tier.formatText(name)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true)
                )

                // Set consistent lore
                setConsistentLore(meta)
            }
        }
    }

    private fun storePersistentData(meta: ItemMeta) {
        val container = meta.persistentDataContainer

        // Store tier name
        container.set(
            NamespacedKey(plugin, TIER_KEY),
            PersistentDataType.STRING,
            tier.name
        )

        // Store category name
        container.set(
            NamespacedKey(plugin, CATEGORY_KEY),
            PersistentDataType.STRING,
            category.name
        )
    }

    private fun setConsistentLore(meta: ItemMeta) {
        val lore = listOf(
            // Tier name with no italic
            tier.formatText(tier.displayName)
                .decoration(TextDecoration.ITALIC, false),
            // Line through middle
            Component.text()
                .content("                         ")
                .color(TextColor.color(0x7F7F7F))
                .decoration(TextDecoration.STRIKETHROUGH, true)
                .decoration(TextDecoration.ITALIC, false)
                .build(),
            // Empty line for spacing
            Component.empty()
                .decoration(TextDecoration.ITALIC, false),
            // Category with custom coloring
            Component.text()
                .content("Category: ")
                .color(TextColor.color(0x7F7F7F))
                .decoration(TextDecoration.ITALIC, false)
                .append(
                    Component.text(category.displayName)
                        .color(TextColor.color(0xFFFFFF))
                        .decoration(TextDecoration.ITALIC, false)
                )
                .build(),
            // Empty line for spacing
            Component.empty()
                .decoration(TextDecoration.ITALIC, false),
            // Arrow with custom coloring
            Component.text()
                .content("→ See more: ")
                .color(TextColor.color(0x00AAFF))
                .decoration(TextDecoration.ITALIC, false)
                .append(
                    Component.text("F")
                        .color(TextColor.color(0x00AAFF))
                        .decoration(TextDecoration.ITALIC, false)
                )
                .build()
        )

        meta.lore(lore)
    }
}