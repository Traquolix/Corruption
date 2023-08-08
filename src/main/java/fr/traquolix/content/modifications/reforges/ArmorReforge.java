package fr.traquolix.content.modifications.reforges;

import fr.traquolix.stats.Stat;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code ArmorReforge} enum represents different reforges that can be applied to armor items.
 *
 * <p>Each armor reforge has a name and a map of {@link Stat} bonuses associated with it. The bonuses
 * represent the effects or enhancements that the reforge provides to the armor item when applied.</p>
 *
 * <p>Example: {@code PROTECTION} reforge provides bonuses of 1 health and 2 defense when applied to
 * an armor item.</p>
 */
@Getter
public enum ArmorReforge implements Reforge {

    /**
     * Represents the "Protection" reforge.
     * This reforge provides bonuses of 1 health and 2 defense when applied to an armor item.
     */
    PROTECTION("Protection", createMap(new Stat[] { Stat.HEALTH, Stat.DEFENSE }, new Integer[] { 1, 2 }));

    private final String name;
    private final Map<Stat, Integer> bonuses;

    /**
     * Constructor to set the name and bonuses for each armor reforge.
     *
     * @param name    The name of the armor reforge.
     * @param bonuses A map of {@link Stat} bonuses associated with the reforge.
     */
    ArmorReforge(String name, Map<Stat, Integer> bonuses) {
        this.name = name;
        this.bonuses = bonuses;
    }

    /**
     * Helper method to create a map from arrays of stats and bonuses.
     *
     * @param stats   An array of {@link Stat} representing the stats to be used as keys in the map.
     * @param bonuses An array of integers representing the bonuses to be associated with each stat in the map.
     * @return The map with the specified stats as keys and their corresponding bonuses as values.
     */
    private static Map<Stat, Integer> createMap(Stat[] stats, Integer[] bonuses) {
        Map<Stat, Integer> result = new HashMap<>();
        for (int i = 0; i < stats.length; i++) {
            result.put(stats[i], bonuses[i]);
        }
        return result;
    }
}

