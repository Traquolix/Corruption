package fr.traquolix.skills;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
        levelExperienceMap.put(0, 0.0);
        levelExperienceMap.put(1, 83.0);
        levelExperienceMap.put(2, 174.0);
        levelExperienceMap.put(3, 276.0);
        levelExperienceMap.put(4, 388.0);
        levelExperienceMap.put(5, 512.0);
        levelExperienceMap.put(6, 650.0);
        levelExperienceMap.put(7, 801.0);
        levelExperienceMap.put(8, 969.0);
        levelExperienceMap.put(9, 1154.0);
        levelExperienceMap.put(10, 1358.0);
        levelExperienceMap.put(11, 1584.0);
        levelExperienceMap.put(12, 1833.0);
        levelExperienceMap.put(13, 2107.0);
        levelExperienceMap.put(14, 2411.0);
        levelExperienceMap.put(15, 2746.0);
        levelExperienceMap.put(16, 3115.0);
        levelExperienceMap.put(17, 3523.0);
        levelExperienceMap.put(18, 3973.0);
        levelExperienceMap.put(19, 4470.0);
        levelExperienceMap.put(20, 5018.0);
        levelExperienceMap.put(21, 5624.0);
        levelExperienceMap.put(22, 6291.0);
        levelExperienceMap.put(23, 7028.0);
        levelExperienceMap.put(24, 7842.0);
        levelExperienceMap.put(25, 8822.0);
        levelExperienceMap.put(26, 9740.0);
        levelExperienceMap.put(27, 10754.0);
        levelExperienceMap.put(28, 11864.0);
        levelExperienceMap.put(29, 13084.0);
        levelExperienceMap.put(30, 14423.0);
        levelExperienceMap.put(31, 15889.0);
        levelExperienceMap.put(32, 17404.0);
        levelExperienceMap.put(33, 19078.0);
        levelExperienceMap.put(34, 20927.0);
        levelExperienceMap.put(35, 22968.0);
        levelExperienceMap.put(36, 25223.0);
        levelExperienceMap.put(37, 27690.0);
        levelExperienceMap.put(38, 30379.0);
        levelExperienceMap.put(39, 33300.0);
        levelExperienceMap.put(40, 36463.0);
        levelExperienceMap.put(41, 39880.0);
        levelExperienceMap.put(42, 43562.0);
        levelExperienceMap.put(43, 47521.0);
        levelExperienceMap.put(44, 51768.0);
        levelExperienceMap.put(45, 56316.0);
        levelExperienceMap.put(46, 61175.0);
        levelExperienceMap.put(47, 66358.0);
        levelExperienceMap.put(48, 71877.0);
        levelExperienceMap.put(49, 77744.0);
        levelExperienceMap.put(50, 83972.0);
        levelExperienceMap.put(51, 90673.0);
        levelExperienceMap.put(52, 97760.0);
        levelExperienceMap.put(53, 105532.0);
        levelExperienceMap.put(54, 113800.0);
        levelExperienceMap.put(55, 122575.0);
        levelExperienceMap.put(56, 131868.0);
        levelExperienceMap.put(57, 141689.0);
        levelExperienceMap.put(58, 152048.0);
        levelExperienceMap.put(59, 162955.0);
        levelExperienceMap.put(60, 174420.0);
        levelExperienceMap.put(61, 186453.0);
        levelExperienceMap.put(62, 199064.0);
        levelExperienceMap.put(63, 212263.0);
        levelExperienceMap.put(64, 226060.0);
        levelExperienceMap.put(65, 240465.0);
        levelExperienceMap.put(66, 255488.0);
        levelExperienceMap.put(67, 271139.0);
        levelExperienceMap.put(68, 287428.0);
        levelExperienceMap.put(69, 304365.0);
        levelExperienceMap.put(70, 321960.0);
        levelExperienceMap.put(71, 340223.0);
        levelExperienceMap.put(72, 359164.0);
        levelExperienceMap.put(73, 378793.0);
        levelExperienceMap.put(74, 399120.0);
        levelExperienceMap.put(75, 420155.0);
        levelExperienceMap.put(76, 441908.0);
        levelExperienceMap.put(77, 464389.0);
        levelExperienceMap.put(78, 487608.0);
        levelExperienceMap.put(79, 511575.0);
        levelExperienceMap.put(80, 536300.0);
        levelExperienceMap.put(81, 561793.0);
        levelExperienceMap.put(82, 588064.0);
        levelExperienceMap.put(83, 615123.0);
        levelExperienceMap.put(84, 643180.0);
        levelExperienceMap.put(85, 672245.0);
        levelExperienceMap.put(86, 702328.0);
        levelExperienceMap.put(87, 733439.0);
        levelExperienceMap.put(88, 765588.0);
        levelExperienceMap.put(89, 798785.0);
        levelExperienceMap.put(90, 833040.0);
        levelExperienceMap.put(91, 868363.0);
        levelExperienceMap.put(92, 904764.0);
        levelExperienceMap.put(93, 942253.0);
        levelExperienceMap.put(94, 980840.0);
        levelExperienceMap.put(95, 1020565.0);
        levelExperienceMap.put(96, 1061238.0);
        levelExperienceMap.put(97, 1103069.0);
        levelExperienceMap.put(98, 1146078.0);
        levelExperienceMap.put(99, 1190285.0);
        levelExperienceMap.put(100, 1235700.0);

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
