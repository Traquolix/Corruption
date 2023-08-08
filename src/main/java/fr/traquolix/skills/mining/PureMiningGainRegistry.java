package fr.traquolix.skills.mining;

import lombok.Getter;

/**
 * Enum representing a registry of mining gains for specific items in the game.
 */
@Getter
public enum PureMiningGainRegistry {

    /**
     * Represents the mining gain for Gold Block.
     */
    GOLD_BLOCK("minecraft:gold_block", 10),
    DEEPSLATE("minecraft:deepslate", 0.5f),
    LAPIS_BLOCK("minecraft:lapis_block", 10),
    EMERALD_BLOCK("minecraft:emerald_block", 10),
    BLACKSTONE("minecraft:blackstone", 0.5f);

    private final String name;
    private final float xp;

    /**
     * Constructor for each PureMiningGainRegistry enum instance.
     *
     * @param name The name of the item obtained through mining.
     * @param xp   The amount of experience points gained when mining the item.
     */
    PureMiningGainRegistry(String name, float xp) {
        this.name = name;
        this.xp = xp;
    }
}
