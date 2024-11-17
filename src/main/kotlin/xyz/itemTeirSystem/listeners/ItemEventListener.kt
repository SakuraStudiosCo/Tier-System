package xyz.itemTeirSystem.listeners

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import xyz.itemTeirSystem.ItemTeirSystem
import xyz.itemTeirSystem.models.ItemTier

class ItemEventListener(private val plugin: ItemTeirSystem) : Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val block = event.block

        // Skip if player is in creative mode
        if (player.gameMode == GameMode.CREATIVE) return

        // Check if the block has a defined tier
        val blockTierData = plugin.blockTierConfig.getBlockTierData(block.type) ?: return

        // Get the tier
        val tier = plugin.tierManager.getTier(blockTierData.tier) ?: return

        // Cancel the default block drops
        event.isDropItems = false

        // Create and drop the tiered item
        val tieredItem = createTieredItem(block.type, tier, blockTierData.category)

        // Drop the item in the world
        block.world.dropItemNaturally(block.location, tieredItem.toItemStack())

        // Play effects for rare+ items
        if (tier.level >= 3) {
            playTierEffects(player, tier)
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onItemPickup(event: EntityPickupItemEvent) {
        val item = event.item.itemStack
        val entity = event.entity

        if (entity !is Player) return

        // Check if item is a tiered item
        val tierInfo = extractTierInfo(item) ?: return

        // Cancel vanilla pickup sound
        event.isCancelled = true

        // Re-implement pickup with custom effects
        if (addItemToInventory(entity, item)) {
            event.item.remove()
            playPickupEffects(entity, tierInfo)
        }
    }

    private fun createTieredItem(material: Material, tier: ItemTier, category: String): xyz.itemTeirSystem.models.TieredItem {
        // Create a standardized base name for stacking purposes
        val baseName = "${tier.name} ${material.name}"

        // Generate a fancy display name (but this won't affect stacking)
        val displayName = generateDisplayName(material, tier)

        return plugin.itemManager.createTieredItem(
            material = material,
            tierName = tier.name,
            categoryName = category,
            name = displayName // We use the fancy name for display only
        )
    }

    private fun generateDisplayName(material: Material, tier: ItemTier): String {
        val prefix = when(tier.name) {
            "COMMON" -> "Common"
            "UNCOMMON" -> "Uncommon"
            "RARE" -> "Rare"
            "EPIC" -> "Epic"
            "LEGENDARY" -> "Legendary"
            else -> "Generic"
        }

        val baseName = material.name.lowercase()
            .replace("_", " ")
            .split(" ")
            .joinToString(" ") { word ->
                word.replaceFirstChar { it.uppercase() }
            }

        return "$prefix $baseName"
    }

    private fun extractTierInfo(item: ItemStack): ItemTier? {
        val meta = item.itemMeta ?: return null

        // Use persistent data container instead of lore
        val tierName = meta.persistentDataContainer.get(
            NamespacedKey(plugin, "tier_name"),
            PersistentDataType.STRING
        ) ?: return null

        return plugin.tierManager.getTier(tierName)
    }

    private fun addItemToInventory(player: Player, item: ItemStack): Boolean {
        // Check if inventory has space
        if (player.inventory.firstEmpty() == -1) {
            player.sendMessage(
                Component.text("Inventory full!")
                    .color(TextColor.color(0xFF5555))
            )
            return false
        }

        player.inventory.addItem(item)
        return true
    }

    private fun playTierEffects(player: Player, tier: ItemTier) {
        val location = player.location

        when (tier.level) {
            3 -> { // Rare
                player.playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
            }
            4 -> { // Epic
                player.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.5f)
            }
            5 -> { // Legendary
                player.playSound(location, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f)
            }
        }
    }

    private fun playPickupEffects(player: Player, tier: ItemTier) {
        val location = player.location

        // Play base pickup sound
        player.playSound(location, Sound.ENTITY_ITEM_PICKUP, 0.5f, 1f)

        // Play additional effects based on tier
        when (tier.level) {
            3 -> { // Rare
                player.playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1.2f)
            }
            4 -> { // Epic
                player.playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.7f, 1.5f)
            }
            5 -> { // Legendary
                player.playSound(location, Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1.5f)
            }
        }
    }
}