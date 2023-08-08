package fr.traquolix.content.modifications.reforges;

import fr.traquolix.stats.Stat;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code WeaponReforge} enum represents different weapon reforges with associated bonuses.
 *
 * <p>Each weapon reforge has a name and a map of {@link Stat} bonuses associated with it.
 * The bonuses represent the effects or enhancements that the reforge provides to the weapon when applied.</p>
 *
 * <p>Example: {@code KEEN} reforge provides bonuses of 1 damage and 2 health when applied to a weapon.</p>
 */
@Getter
public enum WeaponReforge implements Reforge {

    /**
     * Represents the "Keen" reforge.
     * This reforge provides bonuses of 1 damage and 2 health when applied to a weapon.
     */
    KEEN("Keen", createMap(new Stat[]{Stat.DAMAGE, Stat.HEALTH}, new Integer[]{1, 2}));

    /**
     * The name of the reforge.
     */
    private final String name;

    /**
     * The map of stats and their associated bonuses for the reforge.
     */
    private final Map<Stat, Integer> bonuses;

    /**
     * Constructor to set the name and bonuses for each weapon reforge.
     *
     * @param name    The name of the reforge.
     * @param bonuses The map of stats and their associated bonuses for the reforge.
     */
    WeaponReforge(String name, Map<Stat, Integer> bonuses) {
        this.name = name;
        this.bonuses = bonuses;
    }

    /**
     * Helper method to create a map from arrays of stats and bonuses.
     *
     * @param stats   The array of stats for the reforge.
     * @param bonuses The array of bonuses corresponding to the stats.
     * @return The map of stats and their associated bonuses.
     */
    private static Map<Stat, Integer> createMap(Stat[] stats, Integer[] bonuses) {
        Map<Stat, Integer> result = new HashMap<>();
        for (int i = 0; i < stats.length; i++) {
            result.put(stats[i], bonuses[i]);
        }
        return result;
    }
}
