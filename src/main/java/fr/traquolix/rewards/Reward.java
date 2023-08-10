package fr.traquolix.rewards;

import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.item.ItemStack;

public interface Reward {
    RewardState getRewardState();
    void setRewardState(RewardState state);
    void applyToPlayer(CPlayer player);
    TextComponent getText();
    ItemStack getRepresentation();
}
