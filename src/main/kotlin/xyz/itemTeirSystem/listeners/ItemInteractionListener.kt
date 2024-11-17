package xyz.itemTeirSystem.listeners

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import xyz.itemTeirSystem.ItemTeirSystem

class ItemInteractionListener(private val plugin: ItemTeirSystem) : Listener {

    @EventHandler
    fun onItemInfo(event: PlayerSwapHandItemsEvent) {
        val player = event.player
        val mainHandItem = player.inventory.itemInMainHand

        // Cancel the swap animation
        event.isCancelled = true

        // Check if the item is a tiered item
        if (isTieredItem(mainHandItem)) {
            openItemInfo(player, mainHandItem)
        }
    }

    private fun isTieredItem(item: ItemStack): Boolean {
        if (item.type == Material.AIR) return false
        val meta = item.itemMeta ?: return false

        return meta.persistentDataContainer.has(
            NamespacedKey(plugin, "tier_name"),
            PersistentDataType.STRING
        )
    }

    private fun openItemInfo(player: Player, item: ItemStack) {
        val meta = item.itemMeta ?: return

        // Get tier and category info
        val tierName = meta.persistentDataContainer.get(
            NamespacedKey(plugin, "tier_name"),
            PersistentDataType.STRING
        ) ?: return

        val categoryName = meta.persistentDataContainer.get(
            NamespacedKey(plugin, "category_name"),
            PersistentDataType.STRING
        ) ?: return

        val tier = plugin.tierManager.getTier(tierName) ?: return
        val category = plugin.categoryManager.getCategory(categoryName) ?: return

        // Create inventory GUI
        val inventory = Bukkit.createInventory(
            null,
            27,
            Component.text("Item Information")
        )

        // Create info items
        val itemDisplay = ItemStack(item.type).apply {
            editMeta { displayMeta ->
                displayMeta.displayName(meta.displayName())
            }
        }

        val tierInfo = ItemStack(Material.NAME_TAG).apply {
            editMeta { displayMeta ->
                displayMeta.displayName(Component.text("Tier Information")
                    .color(tier.color))

                val lore = mutableListOf(
                    Component.text("Tier: ").color(TextColor.color(0x7F7F7F))
                        .append(Component.text(tier.displayName).color(tier.color)),
                    Component.text("Level: ").color(TextColor.color(0x7F7F7F))
                        .append(Component.text(tier.level.toString()).color(TextColor.color(0xFFFFFF))),
                    Component.empty(),
                    Component.text("Effects:").color(TextColor.color(0x7F7F7F))
                )

                if (tier.glowing) {
                    lore.add(Component.text("• Glowing").color(TextColor.color(0xFFFFFF)))
                }

                displayMeta.lore(lore)
            }
        }

        val categoryInfo = ItemStack(Material.BOOK).apply {
            editMeta { displayMeta ->
                displayMeta.displayName(Component.text("Category Information")
                    .color(category.color ?: TextColor.color(0xFFFFFF)))

                val lore = listOf(
                    Component.text("Category: ").color(TextColor.color(0x7F7F7F))
                        .append(Component.text(category.displayName).color(category.color ?: TextColor.color(0xFFFFFF))),
                    Component.empty(),
                    Component.text("Description:").color(TextColor.color(0x7F7F7F)),
                    Component.text(category.description).color(TextColor.color(0xFFFFFF))
                )

                displayMeta.lore(lore)
            }
        }

        // Place items in inventory
        inventory.setItem(4, itemDisplay)
        inventory.setItem(11, tierInfo)
        inventory.setItem(15, categoryInfo)

        // Fill empty slots with glass panes
        val emptySlot = ItemStack(Material.GRAY_STAINED_GLASS_PANE).apply {
            editMeta { displayMeta ->
                displayMeta.displayName(Component.empty())
            }
        }

        for (i in 0 until inventory.size) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, emptySlot)
            }
        }

        // Open inventory and play sound
        player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1f)
        player.openInventory(inventory)
    }
}