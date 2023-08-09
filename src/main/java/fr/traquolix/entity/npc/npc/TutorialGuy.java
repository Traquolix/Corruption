package fr.traquolix.entity.npc.npc;

import fr.traquolix.entity.AbstractEntity;
import fr.traquolix.entity.npc.NPCEntity;
import fr.traquolix.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import fr.traquolix.quests.tutorial.TutorialQuestLine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.entity.EntityType;

public class TutorialGuy extends AbstractEntity implements NPCEntity {
    TutorialQuestLine quest = new TutorialQuestLine();
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
    public TextComponent getDefaultMessage() {
        return Component.text("Hello, I'm a NPC!");
    }

    @Override
    public void onInteract(CPlayer player) {
        if (!quest.step(player)) {
            player.sendMessage(getDefaultMessage());
        }
    }
}
