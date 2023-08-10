package fr.traquolix.entity.npc;

import fr.traquolix.entity.AbstractEntity;
import fr.traquolix.gui.AbstractGui;
import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.entity.EntityType;

public abstract class NPCEntity extends AbstractEntity {

    protected AbstractGui gui;
    protected NPCEntity(EntityType type) {
        super(type);
    }

    public abstract void initGui();

    public abstract TextComponent getDefaultMessage();
    public abstract void onInteract(CPlayer player);
    public static String getGroup() {
        return "npc";
    }
}
