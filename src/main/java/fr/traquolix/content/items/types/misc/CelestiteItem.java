package fr.traquolix.content.items.types.misc;

import fr.traquolix.identifiers.Identifier;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemType;
import fr.traquolix.content.Rarity;
import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;

/**
 * The CelestiteItem class represents an item called "Celestite," which is a material said to have fallen from the sky and is believed to be the tears of the gods.
 */
public class CelestiteItem extends AbstractItem {

    /**
     * The unique identifier for the CelestiteItem.
     */
    final public static Identifier identifier = new Identifier("item", "celestite");

    /**
     * Constructs a new CelestiteItem with its properties and lore.
     */
    public CelestiteItem() {
        super(identifier,
                Component.text("Celestite"),
                ItemType.NONE,
                Material.WHITE_DYE,
                Rarity.UNCOMMON,
                "Fallen from the sky." +
                        "This material is said to be the tears of the gods.");
    }

    /**
     * Initializes the requirements for using the CelestiteItem. In this case, it has no requirements, so the method is left empty.
     */
    @Override
    public void initRequirements() {

    }

    /**
     * Initializes the bonus stats of the CelestiteItem. Since it is a material item and doesn't provide any bonuses, the method is left empty.
     */
    @Override
    public void initBonuses() {

    }

    /**
     * Executes the use of the CelestiteItem. Since it is a material item and doesn't have any specific use, the method is left empty.
     *
     * @param cplayer The player who is using the CelestiteItem.
     * @param item    The CelestiteItem being used.
     */
    @Override
    public void use(CPlayer cplayer, AbstractItem item) {

    }
}
