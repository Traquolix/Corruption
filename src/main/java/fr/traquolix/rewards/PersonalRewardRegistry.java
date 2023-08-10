package fr.traquolix.rewards;

import fr.traquolix.player.CPlayer;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
@Getter
public class PersonalRewardRegistry {
    private final ConcurrentMap<Integer, ConcurrentLinkedQueue<Reward>> rewardsRegistry = new ConcurrentHashMap<>();

    public void registerReward(int rewardID, Reward reward) {
        rewardsRegistry
                .computeIfAbsent(rewardID, k -> new ConcurrentLinkedQueue<>())
                .add(reward);
    }

    public void registerAllRewards(int rewardID, ConcurrentLinkedQueue<Reward> newRewards) {
        rewardsRegistry
                .computeIfAbsent(rewardID, k -> new ConcurrentLinkedQueue<>())
                .addAll(newRewards);
    }

    public void claimAllReward(int rewardID, CPlayer player) {
        ConcurrentLinkedQueue<Reward> rewardQueue = rewardsRegistry.get(rewardID);
        if (rewardQueue != null) {
            for (Reward reward : rewardQueue) {
                if (reward.getRewardState() == RewardState.UNCLAIMED) {
                    reward.applyToPlayer(player);
                    reward.setRewardState(RewardState.CLAIMED);
                }
            }
        }
    }

    public void claimReward(int rewardID, CPlayer player, Reward reward) {
        ConcurrentLinkedQueue<Reward> rewardQueue = rewardsRegistry.get(rewardID);
        if (rewardQueue != null) {
            if (reward.getRewardState() == RewardState.UNCLAIMED) {
                reward.applyToPlayer(player);
                reward.setRewardState(RewardState.CLAIMED);
            }
        }
    }

    public void setRewardsAccessible(int id) {
        ConcurrentLinkedQueue<Reward> rewardQueue = rewardsRegistry.get(id);
        if (rewardQueue != null) {
            for (Reward reward : rewardQueue) {
                reward.setRewardState(RewardState.UNCLAIMED);
            }
        }
    }

    public boolean hasClaimedReward(Reward reward) {
        return reward.getRewardState() == RewardState.CLAIMED;
    }

    // ... more methods as needed
}
