# ItemTierSystem

A powerful Minecraft plugin that adds an RPG-style tier system to blocks and items, featuring customizable categories, tiers, and special effects.

## Features

- **5 Tier Levels**: Common, Uncommon, Rare, Epic, and Legendary items with distinct visual styles
- **Custom Categories**: Organize items into categories like Furniture, Weapons, Armor, Resources, and Tools
- **Visual Enhancements**:
  - Color-coded item names and descriptions
  - Custom formatting (bold, italic) based on tier
  - Glowing effects for Rare+ items
  - Professional-looking item lore with category information
  - Visual dividers in item descriptions
  
- **Sound Effects**:
  - Unique sounds when breaking blocks of different tiers
  - Special pickup sounds based on item rarity
  - Enhanced audio feedback for rare item discoveries

- **Category System**:
  - Color-coded categories for easy identification
  - Customizable category descriptions
  - Flexible category assignment system

## Requirements

- Minecraft Server 1.21+
- Paper/Spigot server implementation

## Installation

1. Download the latest version of ItemTierSystem
2. Place the .jar file in your server's `plugins` folder
3. Restart your server
4. The plugin will generate default configuration files

## Configuration

### Block Tiers (`block_tiers.yml`)

Configure which blocks drop tiered items and their properties:

```yaml
DIAMOND_ORE:
  tier: RARE
  category: RESOURCE

ENDER_CHEST:
  tier: EPIC
  category: FURNITURE
```

### Default Tiers

- **COMMON**
  - White color
  - No special effects
  
- **UNCOMMON**
  - Light green color
  - Italic text
  
- **RARE**
  - Blue color
  - Glowing effect
  
- **EPIC**
  - Purple color
  - Bold text & glowing effect
  
- **LEGENDARY**
  - Gold color
  - Bold & italic text with glowing effect

### Default Categories

- **FURNITURE**: Decorative items (Burlywood color)
- **WEAPON**: Combat items (Red color)
- **ARMOR**: Protective items (Blue color)
- **RESOURCE**: Materials and resources (Gold color)
- **TOOL**: Utility items (Green color)

## Features In Detail

### Item Generation
- Items automatically generate with appropriate names based on their tier (e.g., "Simple Iron Ore", "Divine Diamond")
- Each item includes:
  - Custom-colored name
  - Tier indicator
  - Category information
  - Visual separator
  - Interactive prompt ("See more: F")

### Special Effects
- Higher-tier items produce special effects when:
  - Breaking blocks (level 3+ tiers)
  - Picking up items
  - Discovering rare items

## Developer API

The plugin provides a clean API for other plugins to integrate with:

```kotlin
// Create a tiered item
val tieredItem = itemManager.createTieredItem(
    material = Material.DIAMOND,
    tierName = "LEGENDARY",
    categoryName = "RESOURCE",
    name = "Divine Diamond"
)

// Register custom categories
categoryManager.registerCategory(
    ItemCategory(
        name = "CUSTOM",
        description = "Custom items",
        color = TextColor.color(0xFF00FF)
    )
)

// Register custom tiers
tierManager.registerTier(
    ItemTier(
        name = "MYTHIC",
        color = TextColor.color(0xFF00FF),
        level = 6,
        glowing = true
    )
)
```

## Planned Features

- Command system for managing tiers and categories
- GUI for easy configuration
- Stats tracking for tiered items
- Custom effects and particles
- Integration with economy plugins
- More customization options for sounds and effects

## Support

For issues, feature requests, or questions, please open an issue in the GitHub repository.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
