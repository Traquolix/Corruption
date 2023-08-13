package fr.traquolix.quests.missions;

import fr.traquolix.content.blocks.BlockRegistry;
import fr.traquolix.content.blocks.misc.CloudBlock;
import fr.traquolix.content.generalities.requirements.LocationRequirement;
import fr.traquolix.content.generalities.requirements.ResistColdRequirement;
import fr.traquolix.content.generalities.requirements.BlockBrokenRequirement;
import fr.traquolix.locations.cave.generator.CaveGenerator;
import fr.traquolix.locations.cave.generator.structures.ObservatoryStructure;
import fr.traquolix.locations.cave.generator.structures.Structure;
import fr.traquolix.locations.cave.generator.structures.planets.PlanetStructure;
import fr.traquolix.player.CPlayer;
import fr.traquolix.quests.AbstractQuest;
import fr.traquolix.quests.QuestStep;
import fr.traquolix.rewards.AccessReward;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;

import java.util.List;

public class ExploreAPlanet extends AbstractQuest implements Mission {

    Task task;

    public ExploreAPlanet(int id) {
        super(id);
    }

    @Override
    public void initSteps() {
        ObservatoryStructure observatoryStructure = null;
        PlanetStructure planetStructure = null;
        for (Structure structure: CaveGenerator.getStructures()) {
            if (structure instanceof ObservatoryStructure) {
                observatoryStructure = (ObservatoryStructure) structure;
            }
            if (structure instanceof PlanetStructure) {
                planetStructure = (PlanetStructure) structure;
            }
        }


        assert planetStructure != null;
        assert observatoryStructure != null;
        super.steps = List.of(
                new QuestStep(
                        List.of(
                                new BlockBrokenRequirement(100, 0, BlockRegistry.getInstance().getBlock(CloudBlock.identifier))
                        ),
                        List.of(
                                Component.text("Be able to traverse the blizzard.")
                        )
                ),
                new QuestStep(
                        List.of(
                                new ResistColdRequirement()
                        ),
                        List.of(
                                Component.text("Be able to traverse the blizzard.")
                        )
                ),
                new QuestStep(
                        List.of(

                            new LocationRequirement("the Observatory", observatoryStructure.getPlacedStructure().sub(observatoryStructure.getHalfStructureSize(), 0, observatoryStructure.getHalfStructureSize()), observatoryStructure.getPlacedStructure().add(observatoryStructure.getHalfStructureSize(), observatoryStructure.getStructureHeight(), observatoryStructure.getHalfStructureSize()))
                        ),
                        List.of(
                                Component.text("Find the Observatory.")
                        )
                ),
                new QuestStep(
                        List.of(
                                new LocationRequirement("the Star" , planetStructure.getPlacedStructure().sub(20, 20, 20), planetStructure.getPlacedStructure().add(20, 20, 20))
                        ),
                        List.of(
                                Component.text("Access the planet.")
                        )
                )
        );
    }

    @Override
    public void initRewards() {
        super.rewards.add(new AccessReward("WOUW"));
    }

    @Override
    public void initName() {
        super.name = "Planet exploration";
    }

    @Override
    public void initDescription() {
        super.description = List.of(
                Component.text("You have to explore a planet.")
        );
    }

    @Override
    public void initQuestGiver() {
        super.questGiver = null;
    }

    @Override
    public void initTime() {
        super.time = 0;
    }

    @Override
    public void initRepresentation() {
        super.representation = ItemStack.of(Material.FIREWORK_STAR);
    }

    @Override
    public void initQuestRequirements() {

    }

    @Override
    public void startAutoActualizationOfTheMission(CPlayer cPlayer) {
        SchedulerManager scheduler = MinecraftServer.getSchedulerManager();
        task = scheduler.submitTask(() -> {
            if (currentStep != steps.size()) {
                step(cPlayer);
                cPlayer.getSidebar().updateLineContent("current_mission",
                        this.getCurrentQuestStep().getFirstRequirement().getText()
                        );
                return TaskSchedule.millis(500);
            } else {
                return TaskSchedule.stop(); // Stop the task when mana has been fully regenerated.
            }
        });
    }

    @Override
    public void stopSelfActualization(CPlayer cPlayer) {
        cPlayer.getSidebar().updateLineContent("current_mission",
                Component.text("Mission Completed !")
        );
        task.cancel();
    }
}
