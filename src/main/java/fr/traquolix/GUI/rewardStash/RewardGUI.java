package fr.traquolix.GUI.rewardStash;

import fr.traquolix.GUI.AbstractGUI;
import fr.traquolix.entity.AbstractEntity;
import fr.traquolix.entity.EntityRegistry;
import fr.traquolix.entity.npc.NPCEntity;
import fr.traquolix.player.Flag;
import fr.traquolix.quests.AbstractQuest;
import fr.traquolix.quests.QuestEntityRegistry;
import fr.traquolix.quests.QuestRegistry;
import fr.traquolix.rewards.Reward;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import fr.traquolix.rewards.RewardState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.EntityType;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Enchantment;
import net.minestom.server.item.ItemHideFlag;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class RewardGUI extends AbstractGUI {
    public static final Identifier identifier = new Identifier("gui", "reward");

    ConcurrentLinkedQueue<Reward> rewards;
    int id;
    AbstractQuest quest;

    public RewardGUI(int id, ConcurrentLinkedQueue<Reward> rewards, String source, AbstractQuest quest) {
        super(InventoryType.CHEST_6_ROW, source);
        this.id = id;
        this.rewards = rewards;
        this.quest = quest;
    }

    @Override
    public void initIdentifier() {
        super.identifier = identifier;
    }

    @Override
    public void refresh(CPlayer cPlayer) {

        inventory.getInventoryConditions().clear();
        inventory.clear();
        fillInventoryWith(backGroundItem);

        AtomicInteger slotCounter = new AtomicInteger(0);
        List<Integer> questSlotsAvailable = List.of(
                10, 12, 14, 16,
                28, 30, 32, 34
        );
        for (Reward reward: rewards) {
            if (!cPlayer.getConfigFlags().get(Flag.REWARD_STASH_SHOW_CLAIMED)) {
                if (reward.getRewardState() == RewardState.CLAIMED) {
                    continue;
                }
            }
            int currentSlot;
            try {
                currentSlot = questSlotsAvailable.get(slotCounter.get());
            } catch (ArrayIndexOutOfBoundsException exception) {
                return;
            }
            ItemStack itemStack = reward.getRepresentation();
            itemStack = itemStack.withDisplayName(Component.text("Click to claim", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
            setClaimer(cPlayer, currentSlot, itemStack, reward);
            slotCounter.getAndIncrement();
        }
        addInventoryCondition(((player, slot, clickType, inventoryConditionResult) -> {
            inventoryConditionResult.setCancel(true);
        }));
        addInventoryOpener(48, ItemStack.of(Material.ARROW).withDisplayName(Component.text("Previous page")), new RewardStashGUI());
        addCloseItem(50);
        addToggleFlagItem(ItemStack.of(Material.GREEN_CONCRETE), ItemStack.of(Material.RED_CONCRETE), 53, Flag.REWARD_STASH_SHOW_CLAIMED, cPlayer);
        addNpcHeadAt(4);
    }

    private void setClaimer(CPlayer cPlayer, int slot, ItemStack itemStack, Reward reward) {
        Component displayName = itemStack.getDisplayName();
        assert displayName != null;
        if (!itemStack.hasTag(Identifier.getGlobalTag())) {
            itemStack = itemStack.withLore(List.of(reward.getText()));
        }
        if (reward.getRewardState() == RewardState.CLAIMED) {
            itemStack = itemStack.withMeta(meta -> {
                meta.enchantment(Enchantment.MENDING, (short) 1);
                meta.hideFlag(ItemHideFlag.HIDE_ENCHANTS);
            }).withLore(List.of()).withDisplayName(Component.text("Claimed ", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false).append(reward.getText()));
        }
        ItemStack finalItemStack = itemStack;
        setItemStack(slot, itemStack);
        addInventoryCondition((p, slot1, clickType, inventoryConditionResult) -> {
            if (slot1 == slot
                    && inventory.getItemStack(slot) != backGroundItem
            && cPlayer.getCompletedQuests().contains(id)) {
                cPlayer.getPersonalRewardRegistry().claimReward(id, cPlayer, reward);
                setItemStack(slot, finalItemStack.withMeta(meta -> {
                    meta.enchantment(Enchantment.MENDING, (short) 1);
                    meta.hideFlag(ItemHideFlag.HIDE_ENCHANTS);
                }).withLore(List.of()).withDisplayName(Component.text("Claimed ", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false).append(reward.getText())));
                refresh(cPlayer);
            }
        });
    }

    public void addNpcHeadAt(int slot) {
        AbstractEntity entity = quest.getQuestGiver();
        ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD);
        itemStack = itemStack.withDisplayName(entity.getName().decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));

        setItemStack(slot, itemStack);
    }
}
