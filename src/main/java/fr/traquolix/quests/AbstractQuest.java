package fr.traquolix.quests;

import fr.traquolix.player.CPlayer;
import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public abstract class AbstractQuest {
    protected Component description;
    protected Component name;
    protected List<QuestStep> steps = new ArrayList<>();
    protected List<QuestReward> rewards = new ArrayList<>();
    protected int id;
    protected int currentStep = 0;
    public abstract void initSteps();
    public abstract void initRewards();
    public abstract void initName();
    public abstract void initDescription();

    protected AbstractQuest(int id) {
        initSteps();
        initRewards();
        initName();
        initDescription();
        this.id = id;

        QuestRegistry.getInstance().registerQuest(this);
    }


    public void start(CPlayer player) {
        player.addCurrentQuests(this);
    }
    public void finish(CPlayer player) {
        player.removeCurrentQuests(this);
        getRewards().forEach(reward -> reward.applyToPlayer(player));
        player.addCompletedQuests(this);
    }

    public void addStep(QuestStep step) {
        getSteps().add(step);
    }

    public void addReward(QuestReward reward) {
        getRewards().add(reward);
    }

    public boolean step(CPlayer player) {
        AtomicBoolean canStep = new AtomicBoolean(true);
        getSteps().get(currentStep).getRequirements().forEach(requirement -> {
            if (!requirement.isMet(player)) {
                canStep.set(false);
            }
        });

        if (canStep.get()) {
            if (currentStep == getSteps().size() - 1) {
                currentStep++;
                finish(player);
            } else {
                player.sendMessage(getSteps().get(currentStep).getText());
                this.currentStep++;
            }
        }

        return canStep.get();
    }
}
