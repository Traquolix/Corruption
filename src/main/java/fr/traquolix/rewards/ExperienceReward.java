package fr.traquolix.rewards;

import fr.traquolix.player.CPlayer;
import fr.traquolix.skills.Skill;
import fr.traquolix.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import org.w3c.dom.Text;

public class ExperienceReward implements Reward {
    RewardState rewardState = RewardState.UNCLAIMED;
    private final int xp;
    private final Skill skill;

    public ExperienceReward(Skill skill, int xp) {
        this.skill = skill;
        this.xp = xp;
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
        player.gainExperience(skill, xp);
    }

    @Override
    public TextComponent getText() {
        return Component.text("→ ", NamedTextColor.DARK_GRAY)
                .append(Component.text(xp, NamedTextColor.DARK_AQUA))
                .append(Component.text(" " + Utils.capitalizeFirstLetter(skill.name()), NamedTextColor.GRAY))
                .append(Component.text(" Experience")
                        .color(NamedTextColor.GRAY))
                .decoration(TextDecoration.ITALIC, false);

    }

    @Override
    public ItemStack getRepresentation() {
        return skill.getRepresentation();
    }
}
