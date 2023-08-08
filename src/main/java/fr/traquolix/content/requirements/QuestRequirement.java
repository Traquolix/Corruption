package fr.traquolix.content.requirements;

import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * The {@code QuestRequirement} class represents a requirement based on whether a player has completed a specific quest.
 */
public class QuestRequirement implements Requirement {

    /**
     * The unique identifier of the quest for which the requirement is based on.
     */
    private final String questId;

    /**
     * Constructs a new QuestRequirement with the specified quest identifier.
     *
     * @param questId The unique identifier of the quest for which the requirement is based on.
     */
    public QuestRequirement(String questId) {
        this.questId = questId;
    }

    /**
     * Checks if the quest requirement is met for the given player.
     *
     * @param player The player for whom the quest requirement is being checked.
     * @return {@code true} if the requirement is met, {@code false} otherwise.
     */
    @Override
    public boolean isMet(CPlayer player) {
        // Check if the player has completed the specified quest
        return player.hasCompletedQuest(questId);
    }

    /**
     * Gets the formatted text representation of the quest requirement.
     *
     * @return The text representation as a {@link Component}.
     */
    @Override
    public Component getText() {
        return Component.text("- Quest " + questId + " completed")
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.BLUE);
    }
}
