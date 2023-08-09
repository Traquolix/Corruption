package fr.traquolix.quests.rewards;

import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.player.CPlayer;
import fr.traquolix.quests.QuestReward;

public class ItemReward implements QuestReward {
    private final AbstractItem item;

    public ItemReward(AbstractItem item) {
        this.item = item;
    }

    @Override
    public void applyToPlayer(CPlayer player) {
        player.addItemStack(item);
    }
}
