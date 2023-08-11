package fr.traquolix.time;

import fr.traquolix.quests.QuestEntityRegistry;
import lombok.Getter;
import lombok.Setter;

@Getter
@Getter
public class TimeManager {
    public static TimeManager instance = new TimeManager();

    @Setter
    int currentTime = 0;

    public void stepTime() {
        currentTime++;
        QuestEntityRegistry.getInstance().stepTime(currentTime);
    }

}
