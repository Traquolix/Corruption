package fr.traquolix.skills;

import lombok.Getter;

/**
 * Abstract base class representing a generic skill in the game.
 */
@Getter
public abstract class AbstractSkill {
    int level = 1;
    double experience = 0;

    /**
     * Constructor for the AbstractSkill class.
     */
    public AbstractSkill() {}

    /**
     * Gain experience and update the level if needed.
     *
     * @param experience The amount of experience to gain.
     * @return true if the level has been updated, false otherwise.
     */
    public boolean gainExperience(double experience) {
        int oldLevel = level;
        this.experience += experience;

        // Get the maximum level from the LevelCalculator
        int maxLevel = LevelCalculator.getInstance().getLevelExperienceMap().lastKey();

        // Update the skill's level if necessary based on the gained experience
        while (level < maxLevel && this.experience >= LevelCalculator.getInstance().getLevelExperienceMap().ceilingEntry(level + 1).getValue()) {
            level++;
            // Perform any additional actions when the level is updated, if needed
        }

        // Return true if the level has been updated
        return level > oldLevel;
    }
}
