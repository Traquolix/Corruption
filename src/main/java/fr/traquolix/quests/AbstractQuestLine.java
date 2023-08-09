package fr.traquolix.quests;

import fr.traquolix.content.requirements.Requirement;
import fr.traquolix.player.CPlayer;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static fr.traquolix.Main.logger;

@Getter
public abstract class AbstractQuestLine {
    ConcurrentLinkedQueue<AbstractQuest> quests = new ConcurrentLinkedQueue<>();
    protected Component questLineName;
    AbstractQuest currentQuest;
    AbstractQuest lastQuest;
    boolean flaggedMessage = false;
    protected AbstractQuestLine() {
        initQuests();
        initName();
    }

    public abstract void initQuests();
    public abstract void initName();

    public boolean step(CPlayer player) {

        if (currentQuest != null && currentQuest.isFinished()) {
            lastQuest = currentQuest;
            currentQuest = null;
        }
        if (currentQuest == null) {
            currentQuest = quests.poll();
        }
        if (currentQuest == null && quests.isEmpty() && !flaggedMessage) {
            flaggedMessage = true;
            logger.info("Player " + player.getUuid() + " completed " + questLineName + " quest line !");
            player.sendMessage(Component.text("You have completed ")
                    .append(questLineName)
                    .append(Component.text(" quest line !")));
            return true;
        }
        if (currentQuest == null) return false;
        if (player.getCurrentQuests().containsKey(currentQuest.getId())) {
            currentQuest = player.getCurrentQuests().get(currentQuest.getId());
            currentQuest.step(player);
        } else {
            currentQuest.start(player);
        }
        return true;
    }
}
