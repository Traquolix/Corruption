package fr.traquolix.entity.npc.npc;

import fr.traquolix.entity.AbstractEntity;
import fr.traquolix.entity.npc.NPCEntity;
import fr.traquolix.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import fr.traquolix.quests.QuestRegistry;
import fr.traquolix.quests.tutorial.TutorialQuest;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.EntityType;

public class TutorialGuy extends AbstractEntity implements NPCEntity {
    TutorialQuest quest = (TutorialQuest) QuestRegistry.getInstance().getQuest(TutorialQuest.ID);
    public TutorialGuy(EntityType entityType) {
        super(entityType);
    }

    @Override
    public void initId() {
        id = "tutorial_guy";
    }

    @Override
    public void initIdentifier() {
        identifier = new Identifier(getGroup(), id);
    }

    @Override
    public void initName() {
        name = Component.text("Tutorial Guy");
    }

    @Override
    public void onInteract(CPlayer player) {
        if (player.getCurrentQuests().get(TutorialQuest.ID) == null && !player.getCompletedQuests().contains(TutorialQuest.ID)) {
            quest.start(player);
        } else if (!player.getCompletedQuests().contains(TutorialQuest.ID)) {
            quest.step(player);
        }
    }
}
