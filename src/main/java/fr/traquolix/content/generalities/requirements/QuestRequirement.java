package fr.traquolix.content.generalities.requirements;

import fr.traquolix.player.CPlayer;
import fr.traquolix.quests.QuestRegistry;
import fr.traquolix.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

/**
 * The {@code QuestRequirement} class represents a requirement based on whether a player has completed a specific quest.json.
 */
public class QuestRequirement implements Requirement {

    /**
     * The unique identifier of the quest.json for which the requirement is based on.
     */
    private final int questId;

    /**
     * Constructs a new QuestRequirement with the specified quest.json identifier.
     *
     * @param questId The unique identifier of the quest.json for which the requirement is based on.
     */
    public QuestRequirement(int questId) {
        this.questId = questId;
    }

    /**
     * Checks if the quest.json requirement is met for the given player.
     *
     * @param player The player for whom the quest.json requirement is being checked.
     * @return {@code true} if the requirement is met, {@code false} otherwise.
     */
    @Override
    public boolean isMet(CPlayer player) {
        // Check if the player has completed the specified quest.json
        return player.hasCompletedQuest(questId);
    }

    /**
     * Gets the formatted text representation of the quest.json requirement.
     *
     * @return The text representation as a {@link Component}.
     */
    @Override
    public TextComponent getText() {
        return Component.text("→ ")
                .color(NamedTextColor.GOLD)
                .decoration(TextDecoration.ITALIC, false)

                .append(Component.text("[" + QuestRegistry.getInstance().getQuest(questId).getName() + "]")
                        .decoration(TextDecoration.ITALIC, false)
                        .color(NamedTextColor.GOLD)
                        .hoverEvent(Utils.concatenateComponents(QuestRegistry.getInstance().getQuest(questId).getDescription())))
                .append(Component.text(" completed")
                        .decoration(TextDecoration.ITALIC, false)
                        .color(NamedTextColor.GOLD));
    }
}
