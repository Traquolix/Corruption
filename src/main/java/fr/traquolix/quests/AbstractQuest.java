package fr.traquolix.quests;

import fr.traquolix.player.CPlayer;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static fr.traquolix.Main.logger;

@Getter
public abstract class AbstractQuest implements Cloneable {
    protected Component description;
    protected Component name;
    protected List<QuestStep> steps = new ArrayList<>();
    protected List<QuestReward> rewards = new ArrayList<>();
    protected int id;
    protected int currentStep = 1;
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
        player.sendMessage(
                Component.text("Quest ")
                        .append(getName()).hoverEvent(getDescription())
                        .append(Component.text(" started")));
        player.addCurrentQuests(this);
        step(player);
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

    public void step(CPlayer player) {
        AtomicBoolean canStep = new AtomicBoolean(true);
        getSteps().get(currentStep-1).getRequirements().forEach(requirement -> {
            if (!requirement.isMet(player)) {
                canStep.set(false);
            }
        });

        if (canStep.get()) {

            logger.info("Step " + (currentStep) + " of quest " + name + " (" + id +") completed by " + player.getUuid());
            if (currentStep == getSteps().size()) {
                player.sendMessage(getSteps().get(currentStep-1).getText());
                finish(player);
            } else {
                player.sendMessage(getSteps().get(currentStep-1).getText()
                        .clickEvent(ClickEvent.runCommand("/step " + id))
                        .hoverEvent(Component.text("Click for a step forward")));
                this.currentStep++;
            }
        } else {
            player.sendMessage(Component.text("You cannot proceed."));
            player.sendMessage(Component.text("This step requires : "));
            getSteps().get(currentStep-1).getRequirements().forEach(requirement -> {
                if (!requirement.isMet(player)) {
                    player.sendMessage(requirement.getText());
                }
            });
        }
    }

    // Making the clone shallow allow us to modify a quest even if a player is currently doing it (or has finished with it)
    @Override
    public AbstractQuest clone() {
        try {
            return (AbstractQuest) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
