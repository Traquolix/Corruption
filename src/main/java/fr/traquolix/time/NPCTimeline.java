package fr.traquolix.time;

import fr.traquolix.quests.AbstractQuest;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class NPCTimeline implements TimeEvent {
    ConcurrentMap<Integer, ConcurrentLinkedQueue<AbstractQuest>> timeline = new ConcurrentHashMap<>();
    @Getter
    int currentTime = 0;

    @Override
    public void stepTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public void add(int i, AbstractQuest quest) {
        if (timeline.containsKey(i)) {
            timeline.get(i).add(quest);
        } else {
            timeline.put(i, new ConcurrentLinkedQueue<>());
            timeline.get(i).add(quest);
        }
    }

    public boolean contains(AbstractQuest quest) {
        for (ConcurrentLinkedQueue<AbstractQuest> quests : timeline.values()) {
            if (quests.contains(quest))
                return true;
        }
        return false;
    }

    public void remove(AbstractQuest quest) {
        for (ConcurrentLinkedQueue<AbstractQuest> quests : timeline.values()) {
            quests.remove(quest);
        }
    }

    public void remove(int i, AbstractQuest quest) {
        if (timeline.containsKey(i))
            timeline.get(i).remove(quest);
    }

    public void remove(int i) {
        timeline.remove(i);
    }

    public void clear() {
        timeline.clear();
    }

    public ConcurrentLinkedQueue<AbstractQuest> getAllQuests(int currentTime) {
        return timeline.get(currentTime);
    }
}
