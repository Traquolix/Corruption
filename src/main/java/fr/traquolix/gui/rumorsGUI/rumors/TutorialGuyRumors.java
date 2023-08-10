package fr.traquolix.gui.rumorsGUI.rumors;

import fr.traquolix.content.requirements.Requirement;
import fr.traquolix.entity.EntityRegistry;
import fr.traquolix.entity.npc.npc.TutorialGuy;
import fr.traquolix.gui.rumorsGUI.RumorGui;
import fr.traquolix.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import fr.traquolix.quests.AbstractQuest;
import fr.traquolix.quests.QuestStep;
import fr.traquolix.quests.tutorial.TutorialCircularQuestLine;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TutorialGuyRumors extends RumorGui {
    public static final Identifier identifier = new Identifier("gui", "rumors");
    TutorialCircularQuestLine quest = new TutorialCircularQuestLine();

    public TutorialGuyRumors() {
        super("Inventory guy");
    }

    @Override
    public void initEntity() {
        super.entity = EntityRegistry.getInstance().getEntity(TutorialGuy.identifier);
    }

    @Override
    public void addNpcHeadAt(int slot) {
        ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD);
        if (entity.getType() == EntityType.PLAYER) {
            itemStack = itemStack.withDisplayName(Component.text("Player"));
        }
        itemStack = itemStack.withDisplayName(entity.getName());
        setItemStack(slot, itemStack);
    }

    @Override
    public void initIdentifier() {
        super.identifier = identifier;
    }

    @Override
    public void refresh(CPlayer cPlayer) {
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
                addInventoryCondition(((player, slot, clickType, inventoryConditionResult) -> {
                    if (currentSlot == slot
                            && (!cPlayer.getCurrentQuests().containsKey(abstractQuest.getId()))
                            && (!cPlayer.getCompletedQuests().contains(abstractQuest.getId()))
                            && inventory.getItemStack(slot) != backGroundItem
                    && inventory.getItemStack(slot).getTag(Identifier.getGlobalTag()).contains(String.valueOf(abstractQuest.getId()))) {
                        System.out.println("Starting quest " + abstractQuest.getId());
                        quest.start(cPlayer, abstractQuest.getId());
                        refresh(cPlayer);
                        inventory.update(cPlayer.getPlayer());
                    }
                }));
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