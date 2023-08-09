package fr.traquolix.content.items.types.misc;

import fr.traquolix.content.Rarity;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.ItemType;
import fr.traquolix.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;

public class CloudBlockItem extends AbstractItem {

    final public static Identifier identifier = new Identifier("item", "cloud");

    /**
     * Constructs an AbstractItem with default values and no pure description.
     */
    public CloudBlockItem() {
        super(identifier, Component.text("Cloud"),
                ItemType.ITEM,
                Material.WHITE_STAINED_GLASS,
                Rarity.UNCOMMON,
                "A literal cloud as a block.");
    }

    /**
     * Initializes the requirements for the item.
     */
    @Override
    public void initRequirements() {

    }

    /**
     * Initializes the stat bonuses for the item.
     */
    @Override
    public void initBonuses() {

    }

    /**
     * Uses the item by a player.
     *
     * @param cplayer The player using the item.
     * @param item    The item being used.
     */
    @Override
    public void use(CPlayer cplayer, AbstractItem item) {

    }
}
