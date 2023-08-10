package fr.traquolix.rewards;

import fr.traquolix.player.CPlayer;
import fr.traquolix.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class CoinReward implements Reward {
    RewardState rewardState = RewardState.UNCLAIMED;
    private final int coins;

    public CoinReward(int coins) {
        this.coins = coins;
    }

    @Override
    public RewardState getRewardState() {
        return rewardState;
    }

    @Override
    public void setRewardState(RewardState state) {
        rewardState = state;
    }

    @Override
    public void applyToPlayer(CPlayer player) {
        //player.addCoins(coins);
    }

    @Override
    public TextComponent getText() {
        return Component.text("→ ", NamedTextColor.DARK_GRAY)
                .append(Component.text(coins, NamedTextColor.GOLD)).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(" coins").color(NamedTextColor.GRAY))
                .decoration(TextDecoration.ITALIC, false);
    }

    @Override
    public ItemStack getRepresentation() {
        if (coins <= 1000) {
            return ItemStack.of(Material.GOLD_NUGGET);
        } else if (coins <= 100000) {
            return ItemStack.of(Material.GOLD_INGOT);
        } else {
            return ItemStack.of(Material.GOLD_BLOCK);
        }
    }
}
