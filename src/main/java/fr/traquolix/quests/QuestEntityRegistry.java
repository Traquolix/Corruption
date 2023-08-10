package fr.traquolix.quests;

import fr.traquolix.entity.AbstractEntity;
import fr.traquolix.entity.npc.NPCEntity;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
@Getter
public class QuestEntityRegistry {

    @Getter
    private static final QuestEntityRegistry instance = new QuestEntityRegistry();

    ConcurrentMap<AbstractEntity, ConcurrentLinkedQueue<AbstractQuest>> questMap = new ConcurrentHashMap<>();

    public QuestEntityRegistry() {

    }

    public void registerEntity(NPCEntity npcEntity) {
        questMap.put(npcEntity, new ConcurrentLinkedQueue<>());
    }

    public void registerQuest(AbstractEntity questGiver, AbstractQuest abstractQuest) {
        questMap.get(questGiver).add(abstractQuest);
    }

    public AbstractEntity getEntityResponsibleOf(AbstractQuest quest) {
        for (AbstractEntity entity : questMap.keySet()) {
            if (questMap.get(entity).contains(quest)) {
                return entity;
            }
        }
        return null;
    }

    public ConcurrentLinkedQueue<AbstractQuest> getQuests(AbstractEntity entity) {
        return questMap.get(entity);
    }
}
