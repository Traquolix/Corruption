package fr.traquolix.skills;

import lombok.Getter;

import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Singleton class to calculate the level based on experience points in the game.
 */
@Getter
public class LevelCalculator {
    private static volatile LevelCalculator instance = new LevelCalculator();
    private final NavigableMap<Integer, Double> levelExperienceMap;

    /**
     * Private constructor for the LevelCalculator class.
     * Initializes the level-experience mapping using a TreeMap.
     */
    private LevelCalculator() {

        levelExperienceMap = new TreeMap<>();
        levelExperienceMap.put(1, 0.0);
        levelExperienceMap.put(2, 125.0);
        levelExperienceMap.put(3, 200.0);
        levelExperienceMap.put(4, 300.0);
        levelExperienceMap.put(5, 500.0);
        levelExperienceMap.put(6, 750.0);
        levelExperienceMap.put(7, 1000.0);
        levelExperienceMap.put(8, 1500.0);
        levelExperienceMap.put(9, 2000.0);
        levelExperienceMap.put(10, 3500.0);
        levelExperienceMap.put(11, 5000.0);
        levelExperienceMap.put(12, 7500.0);
        levelExperienceMap.put(13, 10000.0);
        levelExperienceMap.put(14, 15000.0);
        levelExperienceMap.put(15, 20000.0);
        levelExperienceMap.put(16, 30000.0);
        levelExperienceMap.put(17, 50000.0);
        levelExperienceMap.put(18, 75000.0);
        levelExperienceMap.put(19, 100000.0);
        levelExperienceMap.put(20, 200000.0);
        levelExperienceMap.put(21, 300000.0);
        levelExperienceMap.put(22, 400000.0);
        levelExperienceMap.put(23, 500000.0);
        levelExperienceMap.put(24, 600000.0);
        levelExperienceMap.put(25, 700000.0);
        levelExperienceMap.put(26, 800000.0);
        levelExperienceMap.put(27, 900000.0);
        levelExperienceMap.put(28, 1000000.0);
        levelExperienceMap.put(29, 1100000.0);
        levelExperienceMap.put(30, 1200000.0);
        levelExperienceMap.put(31, 1300000.0);
        levelExperienceMap.put(32, 1400000.0);
        levelExperienceMap.put(33, 1500000.0);
        levelExperienceMap.put(34, 1600000.0);
        levelExperienceMap.put(35, 1700000.0);
        levelExperienceMap.put(36, 1800000.0);
        levelExperienceMap.put(37, 1900000.0);
        levelExperienceMap.put(38, 2000000.0);
        levelExperienceMap.put(39, 2100000.0);
        levelExperienceMap.put(40, 2200000.0);
        levelExperienceMap.put(41, 2300000.0);
        levelExperienceMap.put(42, 2400000.0);
        levelExperienceMap.put(43, 2500000.0);
        levelExperienceMap.put(44, 2600000.0);
        levelExperienceMap.put(45, 2750000.0);
        levelExperienceMap.put(46, 2900000.0);
        levelExperienceMap.put(47, 3100000.0);
        levelExperienceMap.put(48, 3400000.0);
        levelExperienceMap.put(49, 3700000.0);
        levelExperienceMap.put(50, 4000000.0);
        levelExperienceMap.put(51, 4300000.0);
        levelExperienceMap.put(52, 4600000.0);
        levelExperienceMap.put(53, 4900000.0);
        levelExperienceMap.put(54, 5200000.0);
        levelExperienceMap.put(55, 5500000.0);
        levelExperienceMap.put(56, 5800000.0);
        levelExperienceMap.put(57, 6100000.0);
        levelExperienceMap.put(58, 6400000.0);
        levelExperienceMap.put(59, 6700000.0);
        levelExperienceMap.put(60, 7000000.0);
        levelExperienceMap.put(61, 7300000.0);
        levelExperienceMap.put(62, 7600000.0);
        levelExperienceMap.put(63, 7900000.0);
        levelExperienceMap.put(64, 8200000.0);
        levelExperienceMap.put(65, 8500000.0);
        levelExperienceMap.put(66, 8800000.0);
        levelExperienceMap.put(67, 9100000.0);
        levelExperienceMap.put(68, 9400000.0);
        levelExperienceMap.put(69, 9700000.0);
        levelExperienceMap.put(70, 10000000.0);
        levelExperienceMap.put(71, 10400000.0);
        levelExperienceMap.put(72, 10800000.0);
        levelExperienceMap.put(73, 11200000.0);
        levelExperienceMap.put(74, 11600000.0);
        levelExperienceMap.put(75, 12000000.0);
        levelExperienceMap.put(76, 12400000.0);
        levelExperienceMap.put(77, 12800000.0);
        levelExperienceMap.put(78, 13200000.0);
        levelExperienceMap.put(79, 13600000.0);
        levelExperienceMap.put(80, 14000000.0);
        levelExperienceMap.put(81, 14500000.0);
        levelExperienceMap.put(82, 15000000.0);
        levelExperienceMap.put(83, 15500000.0);
        levelExperienceMap.put(84, 16000000.0);
        levelExperienceMap.put(85, 16500000.0);
        levelExperienceMap.put(86, 17000000.0);
        levelExperienceMap.put(87, 17500000.0);
        levelExperienceMap.put(88, 18000000.0);
        levelExperienceMap.put(89, 19000000.0);
        levelExperienceMap.put(90, 20000000.0);
        levelExperienceMap.put(91, 21000000.0);
        levelExperienceMap.put(92, 22000000.0);
        levelExperienceMap.put(93, 23000000.0);
        levelExperienceMap.put(94, 24000000.0);
        levelExperienceMap.put(95, 25000000.0);
        levelExperienceMap.put(96, 26000000.0);
        levelExperienceMap.put(97, 27000000.0);
        levelExperienceMap.put(98, 28000000.0);
        levelExperienceMap.put(99, 29000000.0);
        levelExperienceMap.put(100, 30000000.0);


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

    public double getRequiredExperienceUntilNextLevel(int level) {
        return levelExperienceMap.get(level + 1);
    }
}
