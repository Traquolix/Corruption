package fr.traquolix.content.items.types.misc;

import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemType;
import fr.traquolix.content.Rarity;
import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;

/**
 * The BloodstoneItem class represents an item called "Bloodstone," which is a material that feels a bit mushy and is believed to be the blood of the gods.
 */
public class BloodstoneItem extends AbstractItem {

    /**
     * The unique identifier for the BloodstoneItem.
     */
   final public static Identifier identifier = new Identifier("item", "bloodstone");

    /**
     * Constructs a new BloodstoneItem with its properties and lore.
     */
    public BloodstoneItem() {
        super(identifier,
                Component.text("Bloodstone"),
                ItemType.ITEM,
                Material.RED_DYE,
                Rarity.UNCOMMON,
                "A material that feels a bit mushy. It is said that it is the blood of the gods.");
    }

    /**
     * Initializes the requirements for using the BloodstoneItem. In this case, it has no requirements, so the method is left empty.
     */
    @Override
    public void initRequirements() {

    }

    /**
     * Initializes the bonus stats of the BloodstoneItem. Since it is a material item and doesn't provide any bonuses, the method is left empty.
     */
    @Override
    public void initBonuses() {

    }

    /**
     * Executes the use of the BloodstoneItem. Since it is a material item and doesn't have any specific use, the method is left empty.
     *
     * @param cplayer The player who is using the BloodstoneItem.
     * @param item    The BloodstoneItem being used.
     */
    @Override
    public void use(CPlayer cplayer, AbstractItem item) {

    }
}
