package fr.traquolix.skills.farming;

import lombok.Getter;

/**
 * Enum representing a registry of farming gains for specific items in the game.
 */
@Getter
public enum PureFarmingGainRegistry {

    /**
     * Represents the farming gain for Carrot.
     */
    CARROT("Carrot", 10);

    private final String name;
    private final int xp;

    /**
     * Constructor for each PureFarmingGainRegistry enum instance.
     *
     * @param name The name of the item obtained through farming.
     * @param xp   The amount of experience points gained when farming the item.
     */
    PureFarmingGainRegistry(String name, int xp) {
        this.name = name;
        this.xp = xp;
    }
}
