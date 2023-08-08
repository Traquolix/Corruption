package fr.traquolix.content.items.types.misc;

import fr.traquolix.identifiers.Identifier;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemType;
import fr.traquolix.content.Rarity;
import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;

/**
 * The BloodstoneOreItem class represents an item called "Bloodstone Ore," which is a rare ore found in the depths of the world and is believed to be the blood of the gods.
 */
public class BloodstoneOreItem extends AbstractItem {

    /**
     * The unique identifier for the BloodstoneOreItem.
     */
    public static Identifier identifier = new Identifier("item", "bloodstone_ore");

    /**
     * Constructs a new BloodstoneOreItem with its properties and lore.
     */
    public BloodstoneOreItem() {
        super(identifier,
                Component.text("Bloodstone Ore"),
                ItemType.NONE,
                Material.RED_STAINED_GLASS,
                Rarity.UNCOMMON,
                "A rare ore that can be found in the depths of the world." +
                        "It is said that it is the blood of the gods.");
    }

    /**
     * Initializes the requirements for using the BloodstoneOreItem. In this case, it has no requirements, so the method is left empty.
     */
    @Override
    public void initRequirements() {

    }

    /**
     * Initializes the bonus stats of the BloodstoneOreItem. Since it is an ore item and doesn't provide any bonuses, the method is left empty.
     */
    @Override
    public void initBonuses() {

    }

    /**
     * Executes the use of the BloodstoneOreItem. Since it is an ore item and doesn't have any specific use, the method is left empty.
     *
     * @param cplayer The player who is using the BloodstoneOreItem.
     * @param item    The BloodstoneOreItem being used.
     */
    @Override
    public void use(CPlayer cplayer, AbstractItem item) {

    }
}
