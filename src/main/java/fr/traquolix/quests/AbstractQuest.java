package fr.traquolix.quests;

import fr.traquolix.content.requirements.Requirement;
import fr.traquolix.player.CPlayer;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static fr.traquolix.Main.logger;

// TODO Make a quest per branching path ? (like a quest tree composed of micro quests)
// TODO To make multiple quests on the same NPC, you can just check with a requirement if the other have been completed.
@Getter
public abstract class AbstractQuest implements Cloneable {
    protected Component description;
    protected String name;
    protected List<Requirement> questRequirements = new ArrayList<>();
    protected List<QuestStep> steps = new ArrayList<>();
    protected List<QuestReward> rewards = new ArrayList<>();
    protected int id;
    protected int currentStep = 1;
    @Getter
    protected boolean finished = false;
    public abstract void initSteps();
    public abstract void initRewards();
    public abstract void initName();
    public abstract void initDescription();

    protected AbstractQuest(int id) {
        initQuestRequirements();
        initSteps();
        initRewards();
        initName();
        initDescription();
        this.id = id;

        QuestRegistry.getInstance().registerQuest(this);
    }

    public abstract void initQuestRequirements();


    public boolean start(CPlayer player) {
        boolean canStart = true;
        for (Requirement requirement : questRequirements) {
            if (!requirement.isMet(player)) {
                player.sendMessage(Component.text("You don't meet the requirements to start this quest"));
                canStart = false;
            }
        }
        if (canStart) {
            player.sendMessage(
                    Component.text("Quest ")
                            .append(Component.text(getName())).hoverEvent(getDescription())
                            .append(Component.text(" started")));
            player.addCurrentQuests(this);
            step(player);
        }

        return canStart;
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
                logger.info("Quest " + name + " (" + id +") completed by " + player.getUuid());
                finished = true;
                player.sendMessage(Component.text("Quest ")
                        .append(Component.text(getName())).hoverEvent(getDescription())
                        .append(Component.text(" finished")));
                finish(player);
            } else {
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

    public boolean canStartQuest(CPlayer player) {
        for (Requirement requirement : questRequirements) {
            if (!requirement.isMet(player)) {
                return false;
            }
        }
        return true;
    }
}
