package fr.traquolix.gui.rumorsGUI;

import fr.traquolix.entity.AbstractEntity;
import fr.traquolix.gui.AbstractGui;
import net.minestom.server.inventory.InventoryType;

public abstract class RumorGui extends AbstractGui {

    protected AbstractEntity entity;
    public RumorGui(String name) {
        super(InventoryType.CHEST_6_ROW, name + " rumors");

        initEntity();
        fillInventoryWith(this.backGroundItem);
        addCloseItem(49);
        addNpcHeadAt(4);
    }


    public abstract void initEntity();

    public abstract void addNpcHeadAt(int slot);

}
