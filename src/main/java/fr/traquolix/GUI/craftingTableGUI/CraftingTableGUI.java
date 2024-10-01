package fr.traquolix.GUI.craftingTableGUI;

import fr.traquolix.GUI.AbstractGUI;
import fr.traquolix.content.generalities.identifiers.Identifier;
import net.minestom.server.inventory.InventoryType;

public class CraftingTableGUI extends AbstractGUI {

    public static final Identifier identifier = new Identifier("gui", "craft");

    protected CraftingTableGUI(InventoryType inventoryType, String name) {
        super(inventoryType, name);
    }

    @Override
    public void initIdentifier() {

    }
}
