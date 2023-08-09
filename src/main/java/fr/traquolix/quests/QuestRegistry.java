package fr.traquolix.quests;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class QuestRegistry {
    private static final QuestRegistry INSTANCE = new QuestRegistry();

    private final ConcurrentMap<Integer, AbstractQuest> itemMap = new ConcurrentHashMap<>();

    // Private constructor to prevent external instantiation (singleton pattern)
    private QuestRegistry() {
    }

    public static QuestRegistry getInstance() {
        return INSTANCE;
    }

    public void registerQuest(AbstractQuest quest) {
        itemMap.put(quest.getId(), quest);
    }
    public void unregisterQuest(Integer id) {
        itemMap.remove(id);
    }

    public AbstractQuest getQuest(Integer id) {
        AbstractQuest quest = itemMap.get(id);
        if (quest != null) {
            return quest.clone();
        }
        return null;
    }

    public int getSize() {
        return itemMap.size();
    }
}
