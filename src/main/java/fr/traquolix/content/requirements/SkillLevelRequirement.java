package fr.traquolix.content.requirements;

import fr.traquolix.player.CPlayer;
import fr.traquolix.skills.Skill;
import fr.traquolix.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * The {@code SkillLevelRequirement} class represents a requirement based on a certain skill level.
 */
public class SkillLevelRequirement implements Requirement {

    /**
     * The Skill for which the requirement is based on.
     */
    private final Skill skill;

    /**
     * The minimum required level of the specified Skill.
     */
    private final int level;

    /**
     * Constructs a new {@code SkillLevelRequirement} with the specified Skill and required level.
     *
     * @param skill The Skill for which the requirement is based on.
     * @param level The minimum required level of the specified Skill.
     */
    public SkillLevelRequirement(Skill skill, int level) {
        this.skill = skill;
        this.level = level;
    }

    /**
     * Checks if the Skill level requirement is met for the given player.
     *
     * @param player The player for whom the Skill level requirement is being checked.
     * @return {@code true} if the requirement is met, {@code false} otherwise.
     */
    @Override
    public boolean isMet(CPlayer player) {
        // If the player is null, consider the requirement met (for non-player entities).
        if (player == null) return true;
        return player.getLevel(skill) >= level;
    }

    /**
     * Gets the formatted text representation of the Skill level requirement.
     *
     * @return The text representation as a {@link Component}.
     */
    @Override
    public TextComponent getText() {
        return Component.text(Utils.capitalizeFirstLetter("- " + Utils.capitalizeFirstLetter(skill.getIdentifier().getId())))
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.BLUE)
                .append(Component.text(" Lv"))
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.BLUE)
                .append(Component.text(level))
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.BLUE);
    }
}
