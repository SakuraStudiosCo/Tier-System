package xyz.itemTeirSystem.models

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor

data class ItemCategory(
    val name: String,
    val description: String,
    val displayName: String = name,
    val color: TextColor? = null
) {
    fun formatName(): Component {
        return Component.text(displayName).let {
            if (color != null) it.color(color) else it
        }
    }
}