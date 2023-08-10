package fr.traquolix.rewards;

import fr.traquolix.player.CPlayer;
import fr.traquolix.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class AccessReward implements Reward {
    //private Access accessType; // Assuming AccessType is an enum or class defining various access types

    RewardState rewardState = RewardState.UNCLAIMED;
    public AccessReward() {
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
        //player.grantAccess(accessType);
    }

    @Override
    public TextComponent getText() {
        return Component.text("→ Access to something")
                .decoration(TextDecoration.ITALIC, false)
                .color(NamedTextColor.YELLOW);
    }

    @Override
    public ItemStack getRepresentation() {
        return ItemStack.of(Material.TRIPWIRE_HOOK);
    }
}
