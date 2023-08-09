package fr.traquolix.quests.tutorial;

import fr.traquolix.content.requirements.QuestRequirement;
import fr.traquolix.content.requirements.ResistColdRequirement;
import fr.traquolix.quests.AbstractQuest;
import fr.traquolix.quests.QuestStep;
import fr.traquolix.quests.rewards.CoinReward;
import net.kyori.adventure.text.Component;

import java.util.List;

public class Tutorial2Quest extends AbstractQuest {

    public static int ID = 1;

    public Tutorial2Quest(int id) {
        super(id);
        Tutorial2Quest.ID = id;
    }

    @Override
    public void initQuestRequirements() {
        questRequirements.add(new QuestRequirement(TutorialQuest.ID));
    }

    @Override
    public void initSteps() {
        addStep(new QuestStep(
                        List.of(

                        ),
                        Component.text("2 - Coucou, je suis un test !")));

        addStep(new QuestStep(
                        List.of(
                                new ResistColdRequirement()
                        ),
                        Component.text("2 - Tu résistes au froid, bravo !")));

        addStep(new QuestStep(
                        List.of(

                        ),
                        Component.text("2 - Voilà ta récompense !")));
    }

    @Override
    public void initRewards() {
        addReward(new CoinReward(100));
    }

    @Override
    public void initName() {
        name = "2 - Tutorial Quest";
    }

    @Override
    public void initDescription() {
        description = Component.text("2 - This is a tutorial quest");
    }
}
