package xyz.itemTeirSystem.listeners

import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import xyz.itemTeirSystem.ItemTeirSystem

class ItemGuiListener(private val plugin: ItemTeirSystem) : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val title = event.view.title()

        // Check if this is our item info GUI
        if (title is Component && title == Component.text("Item Information")) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        val title = event.view.title()

        // Check if this is our item info GUI
        if (title is Component && title == Component.text("Item Information")) {
            event.isCancelled = true
        }
    }
}