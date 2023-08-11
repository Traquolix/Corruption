package fr.traquolix.quests;

import fr.traquolix.entity.AbstractEntity;
import fr.traquolix.entity.npc.NPCEntity;
import fr.traquolix.time.NPCTimeline;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
@Getter
@Getter
public class QuestEntityRegistry {

    private static final QuestEntityRegistry instance = new QuestEntityRegistry();

    ConcurrentMap<AbstractEntity, NPCTimeline> questMap = new ConcurrentHashMap<>();

    public QuestEntityRegistry() {

    }

    public void stepTime(int currentTime) {
        for (NPCTimeline timeline : questMap.values()) {
            timeline.stepTime(currentTime);
        }
    }

    public void registerEntity(NPCEntity npcEntity) {
        questMap.put(npcEntity, new NPCTimeline());
    }

    public void registerQuest(AbstractEntity questGiver, int time, AbstractQuest quest) {
        questMap.get(questGiver).add(time, quest);
    }

    public AbstractEntity getEntityResponsibleOf(AbstractQuest quest) {
        for (AbstractEntity entity : questMap.keySet()) {
            if (questMap.get(entity).contains(quest)) {
                return entity;
            }
        }
        return null;
    }

    public NPCTimeline getQuests(AbstractEntity entity) {
        return questMap.get(entity);
    }
}
