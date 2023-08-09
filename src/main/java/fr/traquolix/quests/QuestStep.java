package fr.traquolix.quests;

import fr.traquolix.content.requirements.Requirement;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.List;
@Getter
public class QuestStep {
    List<Requirement> requirements;
    TextComponent text;

    public QuestStep(List<Requirement> requirements, TextComponent text) {
        this.requirements = requirements;
        this.text = text;
    }
}
