package fr.traquolix.quests.rewards;

import fr.traquolix.player.CPlayer;
import fr.traquolix.quests.QuestReward;
import fr.traquolix.skills.Skill;

public class ExperienceReward implements QuestReward {
    private final int xp;
    private final Skill skill;

    public ExperienceReward(Skill skill, int xp) {
        this.skill = skill;
        this.xp = xp;
    }

    @Override
    public void applyToPlayer(CPlayer player) {
        player.gainExperience(skill, xp);
    }
}
