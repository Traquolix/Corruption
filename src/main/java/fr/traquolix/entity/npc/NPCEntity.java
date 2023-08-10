package fr.traquolix.entity.npc;

import fr.traquolix.entity.AbstractEntity;
import fr.traquolix.GUI.AbstractGUI;
import fr.traquolix.player.CPlayer;
import fr.traquolix.quests.QuestEntityRegistry;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.entity.EntityType;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public abstract class NPCEntity extends AbstractEntity {
// TODO Introduire le système de timeline (en fonction de quand on va le voir, il propose d'autres quêtes).
// TODO Créer un event qu'on peut appeler régulièrement pour "step" le temps.
    protected AbstractGUI gui;
    @Getter
    protected DialogueSelector defaultDialogues = new DialogueSelector(); // And their percentage of apparition.
    protected NPCEntity(EntityType type) {
        super(type);
        initDefaultDialogues();
        QuestEntityRegistry.getInstance().registerEntity(this);
    }

    public abstract void initDefaultDialogues();

    public abstract void initGui();
    public abstract void onInteract(CPlayer player);
    public static String getGroup() {
        return "npc";
    }
}
