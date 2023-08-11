package fr.traquolix.quests.dwarf;

import fr.traquolix.content.generalities.requirements.ResistColdRequirement;
import fr.traquolix.entity.EntityRegistry;
import fr.traquolix.entity.npc.npc.Dwarf;
import fr.traquolix.quests.AbstractQuest;
import fr.traquolix.quests.QuestStep;
import fr.traquolix.rewards.CoinReward;
import fr.traquolix.rewards.ExperienceReward;
import fr.traquolix.skills.Skill;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;

public class FirstColdResistanceItemQuest extends AbstractQuest {

    public static int ID = 0;

    public FirstColdResistanceItemQuest(int id) {
        super(id);
        FirstColdResistanceItemQuest.ID = id;
    }

    @Override
    public void initTime() {
        time = 0;
    }

    @Override
    public void initRepresentation() {
        representation = ItemStack.of(Material.SNOW)
                .withDisplayName(Component.text(name)
                        .decoration(TextDecoration.ITALIC, false)
                        .color(NamedTextColor.GOLD));
    }

    @Override
    public void initQuestRequirements() {
    }

    @Override
    public void initSteps() {

        addStep(new QuestStep(
                        List.of(
                                new ResistColdRequirement()
                        ),
                List.of(
                        Component.text("Good morning, youngster !", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text("My friends and I were hiking near the peaks when we noticed ",NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                                .append(Component.text("big ", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.OBFUSCATED, true)),
                        Component.text("shadow ", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.OBFUSCATED, true)
                                .append(Component.text("in the blizzard. We were too scared to go and see what it was,", NamedTextColor.GRAY).decoration(TextDecoration.OBFUSCATED, false).decoration(TextDecoration.ITALIC, false)),
                        Component.text("but I'm sure you can do it ! I'll give you what we found up there if you do !", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text("You will need to get some ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                                .append(Component.text("warm clothes ", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                                .append(Component.text("before going up there, though.", NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false).decoration(TextDecoration.ITALIC, false))
                )
                )
        );

        addStep(new QuestStep(
                List.of(

                ),
                List.of(
                        Component.text("You are now ready to get up the peaks. Take care, it is difficult", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text("to navigate without the right equipment.. Here, let me help you a bit. ",NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text("You can see this as a ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                                        .append(Component.text("token of gratitude", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                                        .append(Component.text(" for having listened to me.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)),
                        Component.text("As an hermit dwarf, I barely get company ! ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                )
                )
        );

    }

    @Override
    public void initRewards() {
        addReward(new CoinReward(500));
        addReward(new ExperienceReward(Skill.MINING, 1000));
    }

    @Override
    public void initName() {
        name = "First step beyond the snowline";
    }

    @Override
    public void initDescription() {
        description = List.of(
                Component.text("A weird story happened on the mountains peeks...", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
        );
    }

    @Override
    public void initQuestGiver() {
        questGiver = EntityRegistry.getInstance().getEntity(Dwarf.identifier);
    }
}
