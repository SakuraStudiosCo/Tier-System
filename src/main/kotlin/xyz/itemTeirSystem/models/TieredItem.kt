package xyz.itemTeirSystem.models

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

data class TieredItem(
    val material: Material,
    val tier: ItemTier,
    val category: ItemCategory,
    val name: String
) {
    fun toItemStack(): ItemStack {
        return ItemStack(material).apply {
            editMeta { meta ->
                // Name with no italic, but bold
                meta.displayName(
                    tier.formatText(name)
                        .decoration(TextDecoration.ITALIC, false)
                        .decoration(TextDecoration.BOLD, true)
                )

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
                    Component.text()
                        .content("")
                        .decoration(TextDecoration.ITALIC, false)
                        .build(),
                    // Category with custom coloring
                    Component.text()
                        .content("Category: ")
                        .color(TextColor.color(0x7F7F7F)) // Gray color for label
                        .decoration(TextDecoration.ITALIC, false)
                        .append(
                            Component.text(category.displayName)
                                .color(TextColor.color(0xFFFFFF)) // White color for category name
                                .decoration(TextDecoration.ITALIC, false)
                        )
                        .build(),
                    // Empty line for spacing
                    Component.text()
                        .content("")
                        .decoration(TextDecoration.ITALIC, false)
                        .build(),
                    // Arrow with custom coloring
                    Component.text()
                        .content("→ See more: ")
                        .color(TextColor.color(0x00AAFF)) // Light blue for "See more:"
                        .decoration(TextDecoration.ITALIC, false)
                        .append(
                            Component.text("F")
                                .color(TextColor.color(0x00AAFF)) // Matching light blue for "F"
                                .decoration(TextDecoration.ITALIC, false)
                        )
                        .build()
                )

                meta.lore(lore)
            }
        }
    }
}