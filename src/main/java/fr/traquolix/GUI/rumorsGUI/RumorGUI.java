package fr.traquolix.GUI.rumorsGUI;

import fr.traquolix.GUI.AbstractGUI;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.content.generalities.requirements.Requirement;
import fr.traquolix.entity.AbstractEntity;
import fr.traquolix.entity.npc.NPCEntity;
import fr.traquolix.player.CPlayer;
import fr.traquolix.quests.AbstractQuest;
import fr.traquolix.quests.QuestEntityRegistry;
import fr.traquolix.quests.QuestRegistry;
import fr.traquolix.quests.QuestStep;
import fr.traquolix.rewards.Reward;
import fr.traquolix.time.NPCTimeline;
import fr.traquolix.time.TimeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryCondition;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.metadata.PlayerHeadMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class RumorGUI extends AbstractGUI {
// TODO Pagination des quêtes disponibles si il y en a plus de 8. N'afficher que les quêtes qui sont faisables et disponibles, et dont on complête les requirements pour les lancer.
    //  TODO Tuto avec des petites mécaniques une par une (Portal) La clock est donnée à ce moment là, et upgradée par la science et le craft du joueur.
    protected AbstractEntity entity;
    protected NPCTimeline ques;
    public RumorGUI(String name) {
        super(InventoryType.CHEST_6_ROW, name + " rumors");

        initEntity();
        fillInventoryWith(this.backGroundItem);
        addCloseItem(49);
        addNpcHeadAt(4);

         ques = QuestEntityRegistry.getInstance().getQuests(entity);
    }


    public abstract void initEntity();

    public void addNpcHeadAt(int slot) {

        NPCEntity npcEntity = (NPCEntity) entity;

        PlayerHeadMeta.Builder metaBuilder = new PlayerHeadMeta.Builder();
        metaBuilder.skullOwner(UUID.randomUUID()).playerSkin(npcEntity.getSkin());
        ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD).withMeta(metaBuilder.build());

        itemStack = itemStack.withDisplayName(entity.getName().decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, true));
        itemStack = itemStack.withLore(npcEntity.getDefaultDialogues().getRandomDialogue(TimeManager.getInstance().getCurrentTime()-1));

        addInventoryOpener(slot, itemStack, this);
    }

    @Override
    public void refresh(CPlayer cPlayer) {
        super.refresh(cPlayer);

        AtomicInteger slotCounters = new AtomicInteger(0);
        List<Integer> questSlotsAvailable = List.of(19, 21, 23, 25, 37, 39, 41, 43);

        addNpcHeadAt(4);

        ConcurrentLinkedQueue<AbstractQuest> quests = ques.getAllQuests(TimeManager.getInstance().getCurrentTime()-1);
        if (quests != null) {
            quests.forEach(abstractQuest -> {
                if (cPlayer.getCompletedQuests().contains(abstractQuest.getId())) {
                    return;
                }
                int currentSlot;
                try {
                    currentSlot = questSlotsAvailable.get(slotCounters.get()); // Capture the current slot value
                } catch (IndexOutOfBoundsException e) {
                    return;
                }
                if (!cPlayer.getCurrentQuests().containsKey(abstractQuest.getId())
                        && !cPlayer.getCompletedQuests().contains(abstractQuest.getId())) {
                    setItemStack(questSlotsAvailable.get(slotCounters.get()), ItemStack.of(Material.BOOK)
                            .withDisplayName(Component.text(abstractQuest.getName(), NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
                            .withLore(abstractQuest.getDescription()).withTag(
                                    Identifier.getGlobalTag(), String.valueOf(abstractQuest.getId())
                            ));
                    InventoryCondition condition = ((player, slot, clickType, inventoryConditionResult) -> {
                        if (currentSlot == slot && clickType == ClickType.RIGHT_CLICK) {
                            new QuestProgressionGUI(abstractQuest, abstractQuest.getCurrentStep(), this).open(cPlayer);
                        } else if (currentSlot == slot
                                && (!cPlayer.getCurrentQuests().containsKey(abstractQuest.getId()))
                                && (!cPlayer.getCompletedQuests().contains(abstractQuest.getId()))
                                && inventory.getItemStack(slot) != backGroundItem
                                && inventory.getItemStack(slot).getTag(Identifier.getGlobalTag()).contains(String.valueOf(abstractQuest.getId()))) {
                            if (abstractQuest.start(cPlayer)) {
                                refresh(cPlayer);
                                inventory.update(cPlayer.getPlayer());
                            }
                        }
                    });
                    addInventoryCondition(condition);
                } else {
                    AbstractQuest alreadyRunningQuest = QuestRegistry.getInstance().getQuest(abstractQuest.getId());
                    if (alreadyRunningQuest == null) return;
                    alreadyRunningQuest.setCurrentStep(cPlayer.getCurrentQuests().get(abstractQuest.getId()));

                    if (alreadyRunningQuest.getCurrentStep() == alreadyRunningQuest.getSteps().size()) {
                        if (slotCounters.get() == 0) {
                            addNpcHeadAt(22);
                            fill(0, 8, backGroundItem);
                        }
                        addCloseItem(49);
                        return;
                    }


                    QuestStep currentStep = alreadyRunningQuest.getSteps().get(alreadyRunningQuest.getCurrentStep());

                    List<Component> lore = new ArrayList<>();

                    lore.add(Component.empty());
                    lore.addAll(currentStep.getText()); // Add each component from getText
                    if (!currentStep.getRequirements().isEmpty()) {
                        lore.add(Component.empty());
                        lore.add(Component.text("You need :").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.LIGHT_PURPLE));

                        for (Requirement req : currentStep.getRequirements()) {
                            lore.add(req.getText());
                        }
                    }

                    if (alreadyRunningQuest.getCurrentStep() == alreadyRunningQuest.getSteps().size()-1) {
                        lore.add(Component.empty());
                        lore.add(Component.text("Rewards :").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GREEN));

                        for (Reward rew : alreadyRunningQuest.getRewards()) {
                            lore.add(rew.getText());
                        }
                    }


                    setItemStack(questSlotsAvailable.get(slotCounters.get()), ItemStack.of(Material.WRITABLE_BOOK)
                            .withDisplayName(Component.text(alreadyRunningQuest.getName()).color(NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false))
                            .withTag(Identifier.getGlobalTag(), String.valueOf(alreadyRunningQuest.getId()))
                            .withLore(lore)
                    );
                    addInventoryCondition(((player, slot, clickType, inventoryConditionResult) -> {
                        if (currentSlot == slot && clickType == ClickType.RIGHT_CLICK) {
                            new QuestProgressionGUI(abstractQuest, abstractQuest.getCurrentStep(), this).open(cPlayer);
                        } else if (currentSlot == slot && (cPlayer.getCurrentQuests().containsKey(alreadyRunningQuest.getId()))
                                && inventory.getItemStack(slot).getTag(Identifier.getGlobalTag()).contains(String.valueOf(alreadyRunningQuest.getId()))) {
                            if (alreadyRunningQuest.step(cPlayer)) {
                                refresh(cPlayer);
                                inventory.update(cPlayer.getPlayer());
                            }
                        }
                    }));
                }
                slotCounters.getAndIncrement();
            });
        }
        addCloseItem(49);
        // TODO Ajouter une visualisation de la timeline, des quêtes qu'on a réussi à terminer à un moment T etc.
        //  Une sorte d'almanach de connaissances, qui différence une quête entre
        //  l'avoir déjà fait une fois dans sa vie (Aka on a le knowledge pour afficher quand est ce que ça va arriver)
        //  , l'avoir faite pendant la boucle actuelle (Marquée comme complétée, et évidemment considérée comme découverte),
        //  et l'avoir découverte mais ne pas l'avoir faite pendant cette boucle
        //  et enfin ne pas l'avoir découverte. '4 états' différents.
        //addInventoryOpener(53, ItemStack.of(Material.CLOCK), new QuestCompletionTimelineGUI(entity));
    }

}
