package fr.traquolix.GUI.rumorsGUI;

import fr.traquolix.GUI.AbstractGUI;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.content.generalities.requirements.Requirement;
import fr.traquolix.entity.npc.NPCEntity;
import fr.traquolix.player.CPlayer;
import fr.traquolix.quests.AbstractQuest;
import fr.traquolix.quests.QuestStep;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryCondition;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.PlayerHeadMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestProgressionGUI extends AbstractGUI {
    Identifier identifier = new Identifier("gui", "specific_skill_progression");
    AbstractQuest abstractQuest;
    RumorGUI source;
    int step;
    public QuestProgressionGUI(AbstractQuest abstractQuest, int step, RumorGUI rumorGUI) {
        super(InventoryType.CHEST_6_ROW, abstractQuest.getName());
        this.step = step;
        this.abstractQuest = abstractQuest;
        this.source = rumorGUI;
    }

    @Override
    public void initIdentifier() {
        super.identifier = identifier;
    }

    @Override
    public void refresh(CPlayer cPlayer) {
        super.refresh(cPlayer);

        fillInventoryWith(backGroundItem);
        addNpcHeadAt(4);
        addCloseItem(49);
        addInventoryOpener(45,
                ItemStack.of(Material.ARROW)
                        .withDisplayName(Component.text("Go back to rumors")
                                .decoration(TextDecoration.ITALIC, false)),
                source);
        if (step > 0) {
            addInventoryOpener(48, ItemStack.of(Material.ARROW)
                            .withDisplayName(Component.text("Step left")
                                    .decoration(TextDecoration.ITALIC, false)),
                    new QuestProgressionGUI(abstractQuest, step - 1, source), ClickType.LEFT_CLICK);
            addInventoryCondition((player, slot, clickType, inventoryConditionResult) -> {
                if (slot == 48 && clickType == ClickType.RIGHT_CLICK) {
                    int decrement = Math.min(5, step); // If step is 4, it'll decrement by 4; if step is 6, it'll decrement by 5.
                    int newStep = step - decrement;
                    new QuestProgressionGUI(abstractQuest, newStep, source).open(cPlayer);
                }
            });
        }
        if (step < abstractQuest.getSteps().size() - 1) {
            addInventoryOpener(50, ItemStack.of(Material.ARROW)
                            .withDisplayName(Component.text("Step right")
                                    .decoration(TextDecoration.ITALIC, false)),
                    new QuestProgressionGUI(abstractQuest, step + 1, source), ClickType.LEFT_CLICK);
            addInventoryCondition((player, slot, clickType, inventoryConditionResult) -> {
                if (slot == 50 && clickType == ClickType.RIGHT_CLICK) {
                    int maxSize = abstractQuest.getSteps().size() - 1;
                    int increment = Math.min(5, maxSize - step);  // Will increment by as close to 5 as possible without going out of bounds
                    int newStep = step + increment;
                    new QuestProgressionGUI(abstractQuest, newStep, source).open(cPlayer);
                }
            });
        }

        int counter = 0;
        List<Integer> availableSlots = List.of(18, 19, 20, 21, 22, 23, 24, 25, 26);

        int currentStepIndex = abstractQuest.getCurrentStep();
        List<QuestStep> allSteps = abstractQuest.getSteps();

        int start = Math.max(0, step - 4);
        int end = Math.min(allSteps.size() - 1, step + 4);

        // Calculate offset based on how many steps you have from the start
        int offset = Math.max(0, 4 - (step - start));

        for (int i = start; i <= end; i++) {
            QuestStep questStep = allSteps.get(i);

            int slotIndex = counter + offset;  // Add the offset to position in availableSlots

            if (i == currentStepIndex) {

                if (cPlayer.getCurrentQuests().get(abstractQuest.getId()) == null) {
                    setItemStack(availableSlots.get(slotIndex), ItemStack.of(Material.YELLOW_STAINED_GLASS_PANE).withDisplayName(Component.text("Current step", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)).withLore(abstractQuest.getDescription()));
                } else {
                    List<Component> lore = new ArrayList<>();

                    lore.add(Component.empty());
                    lore.addAll(questStep.getText()); // Add each component from getText
                    if (!questStep.getRequirements().isEmpty()) {
                        lore.add(Component.empty());
                        lore.add(Component.text("You need :").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.LIGHT_PURPLE));

                        for (Requirement req : questStep.getRequirements()) {
                            lore.add(req.getText());
                        }
                    }

                    setItemStack(availableSlots.get(slotIndex), ItemStack.of(Material.YELLOW_STAINED_GLASS_PANE).withDisplayName(Component.text("Current step", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)).withLore(lore));
                }
            } else if (i < currentStepIndex) {
                List<Component> lore = new ArrayList<>();

                lore.add(Component.empty());
                lore.addAll(questStep.getText()); // Add each component from getText
                if (!questStep.getRequirements().isEmpty()) {
                    lore.add(Component.empty());
                    lore.add(Component.text("You needed :").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.LIGHT_PURPLE));

                    for (Requirement req : questStep.getRequirements()) {
                        lore.add(req.getText());
                    }
                }

                setItemStack(availableSlots.get(slotIndex), ItemStack.of(Material.GREEN_STAINED_GLASS_PANE).withDisplayName(Component.text("Completed", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)).withLore(lore));
            } else {
                setItemStack(availableSlots.get(slotIndex), ItemStack.of(Material.RED_STAINED_GLASS_PANE).withDisplayName(Component.text("Unkown", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)));
            }
            counter++;
        }

    }

    public void addNpcHeadAt(int slot) {
        if (abstractQuest.getQuestGiver() == null) {
            ItemStack itemStack = ItemStack.of(Material.NETHER_STAR).withDisplayName(Component.text("Mission").decoration(TextDecoration.ITALIC, false));
            setItemStack(slot, itemStack);
            return;
        }

        NPCEntity npcEntity = (NPCEntity) abstractQuest.getQuestGiver();

        PlayerHeadMeta.Builder metaBuilder = new PlayerHeadMeta.Builder();
        metaBuilder.skullOwner(UUID.randomUUID()).playerSkin(npcEntity.getSkin());
        ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD).withMeta(metaBuilder.build());

        itemStack = itemStack.withDisplayName(npcEntity.getName().decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        itemStack = itemStack.withLore(abstractQuest.getDescription());

        setItemStack(slot, itemStack);
    }
}
