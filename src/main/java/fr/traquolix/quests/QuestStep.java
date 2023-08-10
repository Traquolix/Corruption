package fr.traquolix.quests;

import fr.traquolix.content.generalities.requirements.Requirement;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.List;
@Getter
public class QuestStep {
    List<Requirement> requirements;
    List<Component> text;

    public QuestStep(List<Requirement> requirements, List<Component> text) {
        this.requirements = requirements;
        this.text = text;
    }
}
