package fr.traquolix.entity.npc;

import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DialogueSelector {

    private ConcurrentLinkedQueue<Dialogue> dialogues;
    private int totalWeight;
    private Random random;

    public DialogueSelector() {
        dialogues = new ConcurrentLinkedQueue<>();
        random = new Random();
    }

    public void addDialogue(List<TextComponent> line, int weight) {
        dialogues.add(new Dialogue(line, weight));
        totalWeight += weight;
    }

    public List<TextComponent> getRandomDialogue() {
        int value = random.nextInt(totalWeight);
        int weightSum = 0;
        for (Dialogue dialogue : dialogues) {
            weightSum += dialogue.weight;
            if (weightSum >= value) {
                return dialogue.line;
            }
        }
        return null;  // This shouldn't happen if weights are set up correctly
    }
}
