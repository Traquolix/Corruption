package fr.traquolix.gui.rumorsGUI;

import fr.traquolix.gui.AbstractGui;
import fr.traquolix.identifiers.Identifier;
import net.minestom.server.inventory.InventoryType;

public class RumorGui extends AbstractGui {

    public static final Identifier identifier = new Identifier("gui", "rumors");
    public RumorGui() {
        super(InventoryType.CHEST_6_ROW, "Rumors");

        fillInventoryWith(this.backGroundItem);
        addCloseItem(49);
    }

    @Override
    public void initIdentifier() {
        super.identifier = identifier;
    }
}
