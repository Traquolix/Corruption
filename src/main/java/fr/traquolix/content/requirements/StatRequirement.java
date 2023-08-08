package fr.traquolix.content.requirements;

import fr.traquolix.player.CPlayer;
import fr.traquolix.stats.Stat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * The {@code StatRequirement} class represents a requirement based on a player's stat value.
 */
public class StatRequirement implements Requirement {

    /**
     * The stat associated with the requirement.
     */
    private final Stat stat;

    /**
     * The required value for the stat.
     */
    private final int requiredValue;

    /**
     * Constructor to set the stat and required value for the requirement.
     *
     * @param stat          The stat associated with the requirement.
     * @param requiredValue The required value for the stat.
     */
    public StatRequirement(Stat stat, int requiredValue) {
        this.stat = stat;
        this.requiredValue = requiredValue;
    }

    /**
     * Check if the requirement is met for the given player.
     *
     * @param player The player to check the requirement for.
     * @return {@code true} if the requirement is met, {@code false} otherwise.
     */
    @Override
    public boolean isMet(CPlayer player) {
        return (player.getCurrentStatValue(stat) >= requiredValue);
    }

    /**
     * Get the text representation of the requirement.
     *
     * @return The component representing the requirement text.
     */
    @Override
    public Component getText() {
        return Component.text("- ")
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.BLUE)
                .append(Component.text(requiredValue)
                        .decoration(TextDecoration.ITALIC, false)
                        .color(stat.getColor()))
                .append(Component.space().decoration(TextDecoration.ITALIC, false))
                .append(Component.text(stat.getSymbol())
                        .color(stat.getColor()))
                .decoration(TextDecoration.ITALIC, false);
    }
}
