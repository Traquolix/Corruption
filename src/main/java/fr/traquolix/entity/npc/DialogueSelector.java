package fr.traquolix.entity.npc;

import fr.traquolix.time.DialogueTimeline;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import static fr.traquolix.Main.logger;

public class DialogueSelector {

    private DialogueTimeline dialogues;
    private int totalWeight;
    private Random random;
    private Dialogue defaultDialogue = new Dialogue(new ArrayList<>(List.of(Component.text("I have nothing to tell you right now.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))), 1);
    private Dialogue lastDialogue = null;
    public DialogueSelector() {
        dialogues = new DialogueTimeline();
        random = new Random();
    }

    public void addDialogue(int i, List<TextComponent> line, int weight) {
        dialogues.add(i, new Dialogue(line, weight));
        totalWeight += weight;
    }

    public List<TextComponent> getRandomDialogue(int i) {
        if (dialogues.getSize() == 1) {
            logger.warn("There is only one dialogue for this NPC, it should not. Make sure you have at least 2 dialogues if you add one to a time step / NPC.");
            return List.of(Component.text("..."));
        }
        Dialogue selectedDialogue = null;
        do {
            int value = random.nextInt(totalWeight);
            int weightSum = 0;
            if (dialogues.get(i) != null) {
                for (Dialogue dialogue : dialogues.get(i)) {
                    weightSum += dialogue.weight;
                    if (weightSum >= value) {
                        selectedDialogue = dialogue;
                        break;  // We found our dialogue, break out of the loop
                    }
                }
                if (selectedDialogue == null) {
                    selectedDialogue = defaultDialogue;
                }
            } else {
                selectedDialogue = defaultDialogue;
            }
        } while (selectedDialogue == lastDialogue); // Keep looping until a different dialogue is selected

        lastDialogue = selectedDialogue;
        return selectedDialogue.line;
    }

}
