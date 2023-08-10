package fr.traquolix.quests.tutorial;

import fr.traquolix.quests.AbstractCircularQuestLine;
import fr.traquolix.quests.QuestRegistry;
import net.kyori.adventure.text.Component;

public class TutorialCircularQuestLine extends AbstractCircularQuestLine {
    @Override
    public void initQuests() {
        getQuests().add(QuestRegistry.getInstance().getQuest(TutorialQuest.ID));
        getQuests().add(QuestRegistry.getInstance().getQuest(Tutorial2Quest.ID));
    }

    @Override
    public void initName() {
        questLineName = Component.text("Tutorial Quest Line");
    }
}
