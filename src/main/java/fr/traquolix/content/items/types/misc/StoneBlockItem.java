package fr.traquolix.content.items.types.misc;

import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemType;
import fr.traquolix.content.Rarity;
import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;

/**
 * The StoneBlockItem class represents a simple item representing a stone block.
 */
public class StoneBlockItem extends AbstractItem {

    /**
     * The unique identifier for the StoneBlockItem.
     */
    public static final Identifier identifier = new Identifier("item", "stone_block");

    /**
     * Constructs a new StoneBlockItem with its properties and lore.
     */
    public StoneBlockItem() {
        super(identifier,
                Component.text("Stone Block"),
                ItemType.ITEM,
                Material.STONE,
                Rarity.COMMON);
    }

    /**
     * Initializes the requirements for using the StoneBlockItem. In this case, it has no requirements, so the method is left empty.
     */
    @Override
    public void initRequirements() {

    }

    /**
     * Initializes the bonus stats of the StoneBlockItem. Since it is a simple block item, it doesn't provide any bonuses, so the method is left empty.
     */
    @Override
    public void initBonuses() {

    }

    /**
     * Executes the use of the StoneBlockItem. Since it is a simple block item, it doesn't have any specific use, so the method is left empty.
     *
     * @param cplayer The player who is using the StoneBlockItem.
     * @param item    The StoneBlockItem being used.
     */
    @Override
    public void use(CPlayer cplayer, AbstractItem item) {

    }
}
