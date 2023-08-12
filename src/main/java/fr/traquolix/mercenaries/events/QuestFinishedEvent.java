package fr.traquolix.mercenaries.events;

import fr.traquolix.entity.AbstractEntity;
import fr.traquolix.mercenaries.AbstractMercenary;
import fr.traquolix.quests.AbstractQuest;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.EntityEvent;
import org.jetbrains.annotations.NotNull;

public class QuestFinishedEvent implements EnvironmentalEvent, CancellableEvent, EntityEvent {

    private boolean cancelled;
    public AbstractEntity entity;
    public AbstractQuest quest;
    public AbstractMercenary mercenary;
    QuestFinishedEvent(AbstractEntity entity, AbstractQuest quest, AbstractMercenary mercenary) {
        this.entity = entity;
        this.quest = quest;
        this.mercenary = mercenary;
    }
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull Entity getEntity() {
        return entity.getEntity();
    }
}
