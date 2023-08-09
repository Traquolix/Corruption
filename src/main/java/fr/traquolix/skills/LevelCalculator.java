package fr.traquolix.skills;

import lombok.Getter;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Singleton class to calculate the level based on experience points in the game.
 */
@Getter
public class LevelCalculator {
    private static volatile LevelCalculator instance;
    private final NavigableMap<Integer, Integer> levelExperienceMap;

    /**
     * Private constructor for the LevelCalculator class.
     * Initializes the level-experience mapping using a TreeMap.
     */
    private LevelCalculator() {
        levelExperienceMap = new TreeMap<>();
        levelExperienceMap.put(0, 0);      // Level 0 requires 0 experience
        levelExperienceMap.put(1, 50);     // Level 1 requires 50 experience
        levelExperienceMap.put(2, 150);    // Level 2 requires 150 experience
        levelExperienceMap.put(3, 300);    // Level 3 requires 300 experience
        levelExperienceMap.put(4, 500);    // Level 4 requires 500 experience
        // Add more levels and experience requirements as needed
    }

    /**
     * Singleton pattern: Get the instance of LevelCalculator.
     *
     * @return The singleton instance of LevelCalculator.
     */
    public static LevelCalculator getInstance() {
        if (instance == null) {
            synchronized (LevelCalculator.class) {
                if (instance == null) {
                    instance = new LevelCalculator();
                }
            }
        }
        return instance;
    }

    /**
     * Calculates the level based on the provided experience points.
     *
     * @param experience The experience points to calculate the level for.
     * @return The level corresponding to the provided experience points.
     */
    public int calculateLevel(int experience) {
        return levelExperienceMap.floorEntry(experience).getKey();
    }
}
