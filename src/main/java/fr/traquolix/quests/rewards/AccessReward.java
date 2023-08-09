package fr.traquolix.quests.rewards;

import fr.traquolix.player.CPlayer;
import fr.traquolix.quests.QuestReward;
import net.kyori.adventure.text.Component;

public class AccessReward implements QuestReward {
    //private Access accessType; // Assuming AccessType is an enum or class defining various access types

    public AccessReward() {
    }

    @Override
    public void applyToPlayer(CPlayer player) {
        //player.grantAccess(accessType);
        player.sendMessage(Component.text("You received access to something!"));
    }
}
