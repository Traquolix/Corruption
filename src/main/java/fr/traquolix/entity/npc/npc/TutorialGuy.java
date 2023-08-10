package fr.traquolix.entity.npc.npc;

import fr.traquolix.entity.npc.NPCEntity;
import fr.traquolix.gui.rumorsGUI.rumors.TutorialGuyRumors;
import fr.traquolix.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.entity.EntityType;

public class TutorialGuy extends NPCEntity  {
    public static final Identifier identifier = new Identifier(getGroup(), "tutorial_guy");
    public TutorialGuy(EntityType entityType) {
        super(entityType);
        initGui();
    }

    @Override
    public void initGui() {
        gui = new TutorialGuyRumors();
    }

    @Override
    public void initId() {
        id = "tutorial_guy";
    }

    @Override
    public void initIdentifier() {
        super.identifier = identifier;
    }

    @Override
    public void initName() {
        name = Component.text("Tutorial Guy");
    }

    @Override
    public TextComponent getDefaultMessage() {
        return Component.text("Hello, I'm a NPC!");
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
