package fr.traquolix.GUI.rumorsGUI.rumors;

import fr.traquolix.entity.EntityRegistry;
import fr.traquolix.entity.npc.npc.Dwarf;
import fr.traquolix.GUI.rumorsGUI.RumorGUI;
import fr.traquolix.content.generalities.identifiers.Identifier;

public class DwarfRumors extends RumorGUI {
    public static final Identifier identifier = new Identifier("gui", "rumors");
    public DwarfRumors() {
        super("Dwarf");
    }

    @Override
    public void initEntity() {
        super.entity = EntityRegistry.getInstance().getEntity(Dwarf.identifier);
    }

    @Override
    public void initIdentifier() {
        super.identifier = identifier;
    }
}