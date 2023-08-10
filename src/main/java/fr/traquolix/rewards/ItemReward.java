package fr.traquolix.rewards;

import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.player.CPlayer;
import fr.traquolix.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;

public class ItemReward implements Reward {
    RewardState rewardState = RewardState.UNCLAIMED;
    private final AbstractItem item;

    public ItemReward(AbstractItem item) {
        this.item = item;
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
        player.addItemStack(item);
    }

    @Override
    public TextComponent getText() {
        return Component.text("→ ", NamedTextColor.DARK_GRAY)
                .decoration(TextDecoration.ITALIC, false)
                .append(item.getName()).append(Component.text(" coins !"))
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.YELLOW);
    }

    @Override
    public ItemStack getRepresentation() {
        return item.buildItemStack();
    }
}
