package fr.traquolix.time;

import fr.traquolix.entity.npc.Dialogue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class DialogueTimeline {

    ConcurrentMap<Integer, ConcurrentLinkedQueue<Dialogue>> timeline = new ConcurrentHashMap<>();

    public void add(int i, Dialogue dialogue) {
        if (timeline.containsKey(i)) {
            timeline.get(i).add(dialogue);
        } else {
            timeline.put(i, new ConcurrentLinkedQueue<>());
            timeline.get(i).add(dialogue);
        }
    }

    public boolean contains(Dialogue dialogue) {
        for (ConcurrentLinkedQueue<Dialogue> dialogues : timeline.values()) {
            if (dialogues.contains(dialogue))
                return true;
        }
        return false;
    }

    public void remove(Dialogue dialogue) {
        for (ConcurrentLinkedQueue<Dialogue> dialogues : timeline.values()) {
            dialogues.remove(dialogue);
        }
    }

    public void remove(int i, Dialogue dialogue) {
        if (timeline.containsKey(i))
            timeline.get(i).remove(dialogue);
    }

    public void remove(int i) {
        timeline.remove(i);
    }

    public ConcurrentLinkedQueue<Dialogue> get(int i) {
        return timeline.get(i);
    }
}
