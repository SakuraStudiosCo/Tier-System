package xyz.itemTeirSystem.managers

import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import xyz.itemTeirSystem.models.ItemTier

class TierManager {
    private val tiers = mutableMapOf<String, ItemTier>()

    fun loadDefaultTiers() {
        registerTier(
            ItemTier(
                name = "COMMON",
                color = TextColor.color(0xFFFFFF),
                level = 1
            )
        )
        registerTier(
            ItemTier(
                name = "UNCOMMON",
                color = TextColor.color(0x55FF55),
                level = 2,
                decorations = setOf(TextDecoration.ITALIC)
            )
        )
        registerTier(
            ItemTier(
                name = "RARE",
                color = TextColor.color(0x5555FF),
                level = 3,
                glowing = true
            )
        )
        registerTier(
            ItemTier(
                name = "EPIC",
                color = TextColor.color(0xAA00AA),
                level = 4,
                glowing = true,
                decorations = setOf(TextDecoration.BOLD)
            )
        )
        registerTier(
            ItemTier(
                name = "LEGENDARY",
                color = TextColor.color(0xFFAA00),
                level = 5,
                glowing = true,
                decorations = setOf(TextDecoration.BOLD, TextDecoration.ITALIC)
            )
        )
    }

    fun registerTier(tier: ItemTier) {
        tiers[tier.name.uppercase()] = tier
    }

    fun getTier(name: String): ItemTier? = tiers[name.uppercase()]

    fun getAllTiers(): List<ItemTier> = tiers.values.toList()
}