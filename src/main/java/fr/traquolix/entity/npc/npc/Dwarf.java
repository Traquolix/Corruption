package fr.traquolix.entity.npc.npc;

import fr.traquolix.GUI.rumorsGUI.rumors.DwarfRumors;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.entity.npc.NPCEntity;
import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.EntityType;

import java.util.List;

public class Dwarf extends NPCEntity  {
    public static final Identifier identifier = new Identifier(getGroup(), "cave_dwarf");
    public Dwarf(EntityType entityType) {
        super(entityType);
        initGui();
    }

    @Override
    public void initDefaultDialogues() {
        defaultDialogues.addDialogue(
                List.of(
                        Component.text("Finally, some company. It's been a long time ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text("since all my companions left the mines.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                ),
                10);
        defaultDialogues.addDialogue(
                List.of(
                    Component.text("It's been 5 years since I have not seen", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                        Component.text("the sun. But that blizzard won't stop.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)

                ),
                10);
        defaultDialogues.addDialogue(
                List.of(
                Component.text("I miss you Aleana.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                ),
                2);
        defaultDialogues.addDialogue(
                List.of(
                Component.text("You human don't know what a Dwarf is capable of !", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                ),
                10);
        defaultDialogues.addDialogue(
                List.of(
                Component.text("Come back to me if you find anything interesting.", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                ),
                10);
    }

    @Override
    public void initGui() {
        gui = new DwarfRumors();
    }

    @Override
    public void initId() {
        id = "cave_dwarf";
    }

    @Override
    public void initIdentifier() {
        super.identifier = identifier;
    }

    @Override
    public void initName() {
        name = Component.text("Dwarf");
    }

    @Override
    public void onInteract(CPlayer player) {

        gui.open(player);
        /*
        if (!quest.step(player)) {
            player.sendMessage(getDefaultMessage());
        }

         */
    }
}
