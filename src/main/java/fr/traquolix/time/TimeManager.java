package fr.traquolix.time;

import fr.traquolix.player.PlayerRegistry;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.InstanceContainer;

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

        for (Player player:instanceContainer.getPlayers()) {
            PlayerRegistry.getInstance().getCPlayer(player.getUuid()).getSidebar().updateLineContent(
                    "current_time_line",
                    Component.text("Time : " + (TimeManager.getInstance().getCurrentTime()-1))
            );

        }
    }
}
