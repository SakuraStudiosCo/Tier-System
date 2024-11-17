package xyz.itemTeirSystem.models

import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

data class ItemTier(
    val name: String,
    val color: TextColor,
    val level: Int,
    val displayName: String = name,
    val glowing: Boolean = false,
    val decorations: Set<TextDecoration> = emptySet()
) {
    fun formatText(text: String): Component {
        return Component.text()
            .content(text)
            .color(color)
            .decorations(decorations.associateWith { TextDecoration.State.TRUE })
            .build()
    }
}