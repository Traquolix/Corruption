package fr.traquolix.GUI.rewardStash;

import fr.traquolix.GUI.AbstractGUI;
import fr.traquolix.player.Flag;
import fr.traquolix.rewards.PersonalRewardRegistry;
import fr.traquolix.rewards.Reward;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import fr.traquolix.quests.AbstractQuest;
import fr.traquolix.quests.QuestRegistry;
import fr.traquolix.rewards.RewardState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
// TODO Cela peut éventuellement copier le système des collections sans le copier. Débloquer des crafts avec des achievements quoi.
public class RewardStashGUI extends AbstractGUI {
    public static final Identifier identifier = new Identifier("gui", "reward_stash");
    public RewardStashGUI() {
        super(InventoryType.CHEST_6_ROW, "Reward Stash");
    }

    @Override
    public void initIdentifier() {
        super.identifier = identifier;
    }

    @Override
    public void refresh(CPlayer cPlayer) {
        super.refresh(cPlayer);

        fill(10, 17, ItemStack.of(Material.AIR));
        fill(19, 26, ItemStack.of(Material.AIR));
        fill(28, 35, ItemStack.of(Material.AIR));
        fill(37, 44, ItemStack.of(Material.AIR));

        PersonalRewardRegistry rewardStash = cPlayer.getPersonalRewardRegistry();
        AtomicInteger slotCounter = new AtomicInteger(0);

        List<Integer> questSlotsAvailable = List.of(
                10, 11, 12, 13, 14, 15, 16,
                19, 20, 21, 22, 23, 24, 25,
                28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43
                );

        rewardStash.getRewardsRegistry().forEach((id, rewards) -> {
            int currentSlot;
            try {
                currentSlot = questSlotsAvailable.get(slotCounter.get());
            } catch (ArrayIndexOutOfBoundsException exception) {
                return;
            }
            AtomicBoolean areAllRewardsClaimed = new AtomicBoolean(true);

            for (Reward reward: rewards) {
                if (!cPlayer.getConfigFlags().get(Flag.REWARD_STASH_SHOW_CLAIMED)) {
                    if (reward.getRewardState() == RewardState.CLAIMED) {
                        continue;
                    }
                }
                if (reward.getRewardState() == RewardState.UNCLAIMED) {
                    areAllRewardsClaimed.set(false);
                }
            }
            AbstractQuest quest = QuestRegistry.getInstance().getQuest(id);
            if (cPlayer.getCurrentQuests().get(id) != null) {
                quest = QuestRegistry.getInstance().getQuest(id);
                quest.setCurrentStep(cPlayer.getCurrentQuests().get(id));
            }

            ItemStack item = quest.getRepresentation()
                    .withDisplayName(
                            Component.text(quest.getName())
                                    .color(NamedTextColor.GOLD)
                                    .decoration(TextDecoration.ITALIC, false))
                    .withLore(quest.getDescription()).withTag(Identifier.getGlobalTag(), String.valueOf(id));
            if (areAllRewardsClaimed.get()) {
                if (!cPlayer.hasFlag(Flag.REWARD_STASH_SHOW_CLAIMED)) return;
                item = item.withMeta(meta -> {
                    meta.enchantment(Enchantment.MENDING, (short) 1);
                    meta.hideFlag(ItemHideFlag.HIDE_ENCHANTS);
                }).withDisplayName(Component.text("[Claimed]", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)
                                .append(Component.text(" - ", NamedTextColor.DARK_GRAY).decoration(TextDecoration.ITALIC, false))
                        .append(Component.text(quest.getName(), NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false)));
            }
            setItemStack(currentSlot, item);
            AbstractQuest finalQuest = quest;
            addInventoryCondition((p, slot, clickType, inventoryConditionResult) -> {
                if (slot == currentSlot
                && inventory.getItemStack(slot) != backGroundItem
                && inventory.getItemStack(slot).getTag(Identifier.getGlobalTag()).contains(String.valueOf(id))) {
                    new RewardGUI(id, rewards, "Rewards", finalQuest).open(cPlayer);
                }
            });
            slotCounter.getAndIncrement();

        });
        addInventoryCondition(((player, slot, clickType, inventoryConditionResult) -> {
            inventoryConditionResult.setCancel(true);
        }));
        addCloseItem(49);
        addToggleFlagItem(ItemStack.of(Material.GREEN_CONCRETE).withDisplayName(Component.text("Show claimed rewards", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)),
                ItemStack.of(Material.RED_CONCRETE).withDisplayName(Component.text("Do not show claimed rewards", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)), 53, Flag.REWARD_STASH_SHOW_CLAIMED, cPlayer);
    }
}
