package fr.traquolix.content.generalities.requirements;

import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

/**
 * The {@code Requirement} interface represents a condition that needs to be met in order to fulfill certain criteria.
 */
public interface Requirement {

    /**
     * An identifier for the requirement. Currently set to null. It can be used to uniquely identify specific requirements
     * or attach metadata to them if needed.
     */
    Identifier identifier = null;

    /**
     * Checks if the requirement is met for the given player.
     *
     * @param player The player for whom the requirement is being checked.
     * @return {@code true} if the requirement is met, {@code false} otherwise.
     */
    boolean isMet(CPlayer player);

    /**
     * Gets the text description of the requirement.
     *
     * @return The text description as a {@link Component}.
     */
    TextComponent getText();
}
