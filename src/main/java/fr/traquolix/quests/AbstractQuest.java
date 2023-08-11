package fr.traquolix.quests;

import fr.traquolix.content.generalities.requirements.Requirement;
import fr.traquolix.entity.AbstractEntity;
import fr.traquolix.player.CPlayer;
import fr.traquolix.rewards.Reward;
import fr.traquolix.utils.Utils;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static fr.traquolix.Main.logger;

// TODO Make a quest per branching path ? (like a quest tree composed of micro quests)
// TODO To make multiple quests on the same NPC, you can just check with a requirement if the other have been completed.
@Getter
public abstract class AbstractQuest implements Cloneable {
    protected ItemStack representation;
    protected List<Component> description;
    protected String name;
    protected List<Requirement> questRequirements = new ArrayList<>();
    protected List<QuestStep> steps = new ArrayList<>();
    protected ConcurrentLinkedQueue<Reward> rewards = new ConcurrentLinkedQueue<>();
    protected int id;
    protected int currentStep = 1;
    protected AbstractEntity questGiver;
    protected int time;
    @Getter
    protected boolean finished = false;
    public abstract void initSteps();
    public abstract void initRewards();
    public abstract void initName();
    public abstract void initDescription();
    public abstract void initQuestGiver();

    protected AbstractQuest(int id) {
        initTime();
        initQuestRequirements();
        initSteps();
        initRewards();
        initName();
        initDescription();
        initRepresentation();
        initQuestGiver();
        this.id = id;

        QuestRegistry.getInstance().registerQuest(this);
        QuestEntityRegistry.getInstance().registerQuest(questGiver, time, this);
    }

    public abstract void initTime();

    public abstract void initRepresentation();

    public abstract void initQuestRequirements();


    public boolean start(CPlayer player) {
        boolean canStart = true;
        for (Requirement requirement : questRequirements) {
            if (!requirement.isMet(player)) {
                canStart = false;
            }
        }
        if (canStart) {
            player.sendMessage(
                    Component.text("Quest ")
                            .color(NamedTextColor.GREEN)
                            .decoration(TextDecoration.ITALIC, false)
                            .append(Component.text("[" + getName() + "]")
                                    .hoverEvent(Utils.concatenateComponents(description))
                                    .color(NamedTextColor.GOLD))
                            .append(Component.text(" started.")));
            logger.info("Quest " + name + " (" + id +") started by " + player.getUuid());
            player.addCurrentQuests(id, currentStep);
        } else {
            player.sendMessage(Component.text("The constellations have not yet charted this path for you.").color(NamedTextColor.LIGHT_PURPLE));
            player.sendMessage(Component.text("Quest Locked - To start, achieve the following :").color(NamedTextColor.LIGHT_PURPLE));
            questRequirements.forEach(requirement -> {
                if (!requirement.isMet(player)) {
                    player.sendMessage(requirement.getText());
                }
            });
        }

        return canStart;
    }
    public void finish(CPlayer player) {
        player.removeCurrentQuests(this);
        player.getPersonalRewardRegistry().setRewardsAccessible(id);
        player.getPersonalRewardRegistry().claimAllReward(id, player);
        player.addCompletedQuests(this);
    }

    public void addStep(QuestStep step) {
        getSteps().add(step);
    }

    public void addReward(Reward reward) {
        getRewards().add(reward);
    }

    public boolean step(CPlayer player) {
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

                player.sendMessage((Component.text("[" + getName() + "]")
                        .hoverEvent(Utils.concatenateComponents(getDescription()))
                        .color(NamedTextColor.GOLD)
                        .append(Component.text(" completed !")
                                .color(NamedTextColor.GREEN)))
                                .hoverEvent(
                                        (Component.text("Click")
                                                .decoration(TextDecoration.BOLD, true)
                                                .color(NamedTextColor.YELLOW))
                                                .append(
                                                        Component.text(" to open your reward stash !")
                                                                .color(NamedTextColor.YELLOW)))
                                .clickEvent(ClickEvent.runCommand("/rewardstash"))
                );
                finish(player);
            } else {
                this.currentStep++;
                player.getCurrentQuests().put(id, currentStep);
            }
            return true;
        } else {
            player.sendMessage(Component.text("The stars have not yet aligned for you to proceed. ").color(NamedTextColor.LIGHT_PURPLE));
            player.sendMessage(Component.text("To continue, you still need : ").color(NamedTextColor.LIGHT_PURPLE));
            getSteps().get(currentStep-1).getRequirements().forEach(requirement -> {
                if (!requirement.isMet(player)) {
                    player.sendMessage(requirement.getText());
                }
            });
            return false;
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

    public void setCurrentStep(int step) {
        this.currentStep = step;
    }
}
