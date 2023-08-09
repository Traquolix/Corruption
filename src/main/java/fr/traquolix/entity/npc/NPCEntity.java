package fr.traquolix.entity.npc;

import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.TextComponent;

public interface NPCEntity {
    TextComponent getDefaultMessage();
    public void onInteract(CPlayer player);
    public default String getGroup() {
        return "npc";
    }
}
