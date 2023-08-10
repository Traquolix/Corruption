package fr.traquolix.gui.rumorsGUI;

import fr.traquolix.content.requirements.Requirement;
import fr.traquolix.entity.AbstractEntity;
import fr.traquolix.gui.AbstractGui;
import fr.traquolix.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import fr.traquolix.quests.AbstractCircularQuestLine;
import fr.traquolix.quests.AbstractQuest;
import fr.traquolix.quests.QuestStep;
import fr.traquolix.quests.tutorial.TutorialCircularQuestLine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.condition.InventoryCondition;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class RumorGui extends AbstractGui {

    protected AbstractCircularQuestLine quest;

    protected AbstractEntity entity;
    public RumorGui(String name) {
        super(InventoryType.CHEST_6_ROW, name + " rumors");

        initEntity();
        fillInventoryWith(this.backGroundItem);
        addCloseItem(49);
        addNpcHeadAt(4);
    }


    public abstract void initEntity();

    public abstract void addNpcHeadAt(int slot);

    @Override
    public void refresh(CPlayer cPlayer) {
        inventory.getInventoryConditions().clear();
        ConcurrentLinkedQueue<AbstractQuest> quests = quest.getQuests();
        AtomicInteger slotCounters = new AtomicInteger(9);
        quests.forEach(abstractQuest -> {
            int currentSlot = slotCounters.get(); // Capture the current slot value
            if (!cPlayer.getCurrentQuests().containsKey(abstractQuest.getId())
                    && !cPlayer.getCompletedQuests().contains(abstractQuest.getId())) {
                Component text = abstractQuest.getDescription();
                setItemStack(slotCounters.get(), ItemStack.of(Material.PAPER).withDisplayName(text).withTag(
                        Identifier.getGlobalTag(), String.valueOf(abstractQuest.getId())
                ));
                InventoryCondition condition = ((player, slot, clickType, inventoryConditionResult) -> {
                    if (currentSlot == slot
                            && (!cPlayer.getCurrentQuests().containsKey(abstractQuest.getId()))
                            && (!cPlayer.getCompletedQuests().contains(abstractQuest.getId()))
                            && inventory.getItemStack(slot) != backGroundItem
                            && inventory.getItemStack(slot).getTag(Identifier.getGlobalTag()).contains(String.valueOf(abstractQuest.getId()))) {
                        if (quest.start(cPlayer, abstractQuest.getId())) {
                            System.out.println("Starting quest " + abstractQuest.getId());
                        }
                        refresh(cPlayer);
                        inventory.update(cPlayer.getPlayer());
                    }
                    inventoryConditionResult.setCancel(true);
                });
                addInventoryCondition(condition);
            } else {
                AbstractQuest alreadyRunningQuest = cPlayer.getCurrentQuests().get(abstractQuest.getId());
                if (alreadyRunningQuest == null) {
                    return;
                }
                QuestStep currentStep = alreadyRunningQuest.getSteps().get(alreadyRunningQuest.getCurrentStep()-1);
                setItemStack(slotCounters.get(), ItemStack.of(Material.PAPER)
                        .withDisplayName(Component.text(alreadyRunningQuest.getName()))
                        .withTag(Identifier.getGlobalTag(), String.valueOf(abstractQuest.getId()))
                        .withLore(List.of(
                                Component.text("Current step: ").append(currentStep.getText()),
                                currentStep.getRequirements().stream().map(Requirement::getText).reduce(Component.empty(), TextComponent::append)
                        ))
                );
                addInventoryCondition(((player, slot, clickType, inventoryConditionResult) -> {
                    if (currentSlot == slot && (cPlayer.getCurrentQuests().containsKey(abstractQuest.getId()))
                            && inventory.getItemStack(slot).getTag(Identifier.getGlobalTag()).contains(String.valueOf(abstractQuest.getId()))) {
                        if (quest.step(cPlayer, alreadyRunningQuest)) {
                            System.out.println("Quest stepped");
                            refresh(cPlayer);
                            inventory.update(cPlayer.getPlayer());
                        }
                    }
                    inventoryConditionResult.setCancel(true);
                }));
            }
            slotCounters.getAndIncrement();
        });
        fill(slotCounters.get(), inventory.getSize(), backGroundItem);
        if (slotCounters.get() == 9) {
            addNpcHeadAt(22);
            fill(0, 8, backGroundItem);
        }
        addCloseItem(49);
    }


    public void fill(int start, int end, ItemStack itemStack) {
        for (int i = start; i < end; i++) {
            setItemStack(i, itemStack);
        }
    }

}
