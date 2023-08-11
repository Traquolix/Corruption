package fr.traquolix.entity.npc;


import net.kyori.adventure.text.TextComponent;

import java.util.List;

public class Dialogue {
    List<TextComponent> line;
    int weight;

    public Dialogue(List<TextComponent> line, int weight) {
        this.line = line;
        this.weight = weight;
    }
}
