package fr.traquolix.content;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * The Rarity enum represents different rarities for items in the game.
 */
@Getter
public enum Rarity {

    /**
     * The "Common" rarity with white color.
     */
    COMMON("Common", NamedTextColor.WHITE),

    /**
     * The "Uncommon" rarity with green color.
     */
    UNCOMMON("Uncommon", NamedTextColor.GREEN),

    /**
     * The "Rare" rarity with blue color.
     */
    RARE("Rare", NamedTextColor.BLUE),

    /**
     * The "Epic" rarity with dark purple color.
     */
    EPIC("Epic", NamedTextColor.DARK_PURPLE),

    /**
     * The "Legendary" rarity with gold color.
     */
    LEGENDARY("Legendary", NamedTextColor.GOLD),

    /**
     * The "Mythic" rarity with light purple color.
     */
    MYTHIC("Mythic", NamedTextColor.LIGHT_PURPLE),

    /**
     * The "Supreme" rarity with red color.
     */
    SUPREME("Supreme", NamedTextColor.RED);

    /**
     * The name of the rarity.
     */
    private final String name;

    /**
     * The color associated with the rarity.
     */
    private final NamedTextColor color;

    /**
     * Constructor to set the name and color of the rarity.
     *
     * @param name  The name of the rarity.
     * @param color The color associated with the rarity.
     */
    Rarity(String name, NamedTextColor color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Get the next rarity in the sequence.
     *
     * @return The next rarity, or null if the rarity provided has no next rarity (not coded).
     */
    public Rarity getNextRarity() {
        return switch (this) {
            case COMMON -> UNCOMMON;
            case UNCOMMON -> RARE;
            case RARE -> EPIC;
            case EPIC -> LEGENDARY;
            case LEGENDARY -> MYTHIC;
            case MYTHIC -> SUPREME;
            case SUPREME -> SUPREME; // Supreme is the highest rarity, so it remains the same.
            // Default case, returns the first rarity (COMMON) if none of the above cases match.
        };
    }
}
