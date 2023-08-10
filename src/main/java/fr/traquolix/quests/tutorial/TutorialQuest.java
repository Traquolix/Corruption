package fr.traquolix.quests.tutorial;

import fr.traquolix.content.requirements.QuestRequirement;
import fr.traquolix.content.requirements.ResistColdRequirement;
import fr.traquolix.quests.AbstractQuest;
import fr.traquolix.quests.QuestStep;
import fr.traquolix.quests.rewards.CoinReward;
import net.kyori.adventure.text.Component;

import java.util.List;

public class TutorialQuest extends AbstractQuest {

    public static int ID = 0;

    public TutorialQuest(int id) {
        super(id);
        TutorialQuest.ID = id;
    }

    @Override
    public void initQuestRequirements() {

    }

    @Override
    public void initSteps() {

        addStep(new QuestStep(
                List.of(

                ),
                Component.text("On a tout un tas de problèmes, help !")));

        addStep(new QuestStep(
                        List.of(
                                new ResistColdRequirement()
                        ),
                        Component.text("On a besoin de gens résistants au froid !")));
    }

    @Override
    public void initRewards() {
        addReward(new CoinReward(100));
    }

    @Override
    public void initName() {
        name = "Tutorial Quest";
    }

    @Override
    public void initDescription() {
        description = Component.text("This is a tutorial quest");
    }
}
