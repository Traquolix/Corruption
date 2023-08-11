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

public class DialogueSelector {

    private DialogueTimeline dialogues;
    private int totalWeight;
    private Random random;
    private Dialogue defaultDialogue = new Dialogue(new ArrayList<>(List.of(Component.text("I have nothing to tell you right now.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))), 1);

    public DialogueSelector() {
        dialogues = new DialogueTimeline();
        random = new Random();
    }

    public void addDialogue(int i, List<TextComponent> line, int weight) {
        dialogues.add(i, new Dialogue(line, weight));
        totalWeight += weight;
    }

    public List<TextComponent> getRandomDialogue(int i) {
        int value = random.nextInt(totalWeight);
        int weightSum = 0;
        if (dialogues.get(i) != null) {
            for (Dialogue dialogue : dialogues.get(i)) {
                weightSum += dialogue.weight;
                if (weightSum >= value) {
                    return dialogue.line;
                }
            }
        } else {
            return defaultDialogue.line;
        }
        return null;  // This shouldn't happen if weights are set up correctly
    }
}
