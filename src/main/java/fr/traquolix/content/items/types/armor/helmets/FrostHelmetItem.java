package fr.traquolix.content.items.types.armor.helmets;

import fr.traquolix.content.Rarity;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemType;
import fr.traquolix.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import fr.traquolix.stats.Stat;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;

public class FrostHelmetItem extends AbstractItem {

    /**
     * The unique identifier for the BloodstoneItem.
     */
    final public static Identifier identifier = new Identifier("item", "frost_helmet");

    /**
     * Constructs a new BloodstoneItem with its properties and lore.
     */
    public FrostHelmetItem() {
        super(identifier,
                Component.text("Frost Helmet"),
                ItemType.HELMET,
                Material.LEATHER_HELMET,
                Rarity.RARE,
                "A cold resistant helmet.");
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
        bonuses.put(Stat.HEALTH, 50);
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

