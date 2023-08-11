package fr.traquolix.time;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.scoreboard.Sidebar;

import static fr.traquolix.Main.logger;

@Getter
public class TimeManager {
    @Getter
    public static TimeManager instance;

    InstanceContainer instanceContainer;
    public TimeManager(InstanceContainer instanceContainer) {
        instance = this;
        this.instanceContainer = instanceContainer;
        logger.info("[Time] - TimeManager has been initialized !");
    }

    @Setter
    int currentTime = 1;

    public void stepTime() {
        logger.info("[Time] - Time has been advanced from " + (currentTime-1) + " to " + currentTime + " !");
        currentTime++;

        Sidebar scoreboard = new Sidebar(Component.text("Scoreboard"));
        scoreboard.createLine(new Sidebar.ScoreboardLine(
                "current_time_line",
                Component.text("Time : " + (TimeManager.getInstance().getCurrentTime()-1)),
                0));

        instanceContainer.getPlayers().forEach(scoreboard::addViewer);
    }
}
