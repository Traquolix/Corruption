package fr.traquolix.entity.npc;

import fr.traquolix.player.CPlayer;

public interface NPCEntity {
    public void onInteract(CPlayer player);

    public default String getGroup() {
        return "npc";
    }
}
