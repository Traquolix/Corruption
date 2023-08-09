package fr.traquolix.player;

import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemRegistry;
import fr.traquolix.content.items.ItemType;
import fr.traquolix.content.items.types.misc.AirItem;
import lombok.Getter;

import java.util.List;

@Getter
public class Equipment {
    AbstractItem helmet = ItemRegistry.getInstance().getItem(AirItem.identifier);
    AbstractItem chestplate = ItemRegistry.getInstance().getItem(AirItem.identifier);
    AbstractItem leggings = ItemRegistry.getInstance().getItem(AirItem.identifier);
    AbstractItem boots = ItemRegistry.getInstance().getItem(AirItem.identifier);
    AbstractItem itemInMainHand = ItemRegistry.getInstance().getItem(AirItem.identifier);
    AbstractItem itemInOffHand = ItemRegistry.getInstance().getItem(AirItem.identifier);

    public void set(int slot, AbstractItem item) {
        switch (slot) {
            case 41 -> helmet = item;
            case 42 -> chestplate = item;
            case 43 -> leggings = item;
            case 44 -> boots = item;
        }
    }

    public void setItemInMainHand(AbstractItem item) {
        itemInMainHand = item;
    }

    public void setItemInOffHand(AbstractItem item) {
        itemInOffHand = item;
    }

    public List<AbstractItem> getAllArmor() {
        return List.of(helmet, chestplate, leggings, boots);
    }

    public List<AbstractItem> getAllItems() {
        return List.of(helmet, chestplate, leggings, boots, itemInMainHand, itemInOffHand);
    }
}
