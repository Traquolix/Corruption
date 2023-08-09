package fr.traquolix.quests.rewards;

import fr.traquolix.player.CPlayer;
import fr.traquolix.quests.QuestReward;
import net.kyori.adventure.text.Component;

public class CoinReward implements QuestReward {
    private final int coins;

    public CoinReward(int coins) {
        this.coins = coins;
    }

    @Override
    public void applyToPlayer(CPlayer player) {
        //player.addCoins(coins);
        player.sendMessage(Component.text("You received $" + coins + " !"));
    }
}
