package fr.traquolix.quests.tutorial;

import fr.traquolix.content.requirements.ResistColdRequirement;
import fr.traquolix.quests.AbstractQuest;
import fr.traquolix.quests.QuestStep;
import fr.traquolix.quests.rewards.CoinReward;
import net.kyori.adventure.text.Component;

import java.util.List;

public class TutorialQuest extends AbstractQuest {

    public TutorialQuest(int id) {
        super(id);
    }

    @Override
    public void initSteps() {
        addStep(new QuestStep(
                        List.of(

                        ),
                        Component.text("Coucou, je suis un test !")));

        addStep(new QuestStep(
                        List.of(
                                new ResistColdRequirement()
                        ),
                        Component.text("Tu résistes au froid, bravo !")));

        addStep(new QuestStep(
                        List.of(

                        ),
                        Component.text("Voilà ta récompense !")));
    }

    @Override
    public void initRewards() {
        addReward(new CoinReward(100));
    }

    @Override
    public void initName() {
        name = Component.text("Tutorial Quest");
    }

    @Override
    public void initDescription() {
        description = Component.text("This is a tutorial quest");
    }
}
