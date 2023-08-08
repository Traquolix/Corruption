package fr.traquolix.content.blocks.misc;

import fr.traquolix.content.blocks.AbstractBlock;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.types.misc.CelestiteItem;
import fr.traquolix.content.items.types.misc.CloudBlockItem;
import fr.traquolix.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import net.minestom.server.instance.block.Block;

import java.util.Random;

public class CloudBlock extends AbstractBlock {

    //TODO Un nuage peut te faire passer à travers le sol. Ne PAS corriger, en faire une mécanique.

    public static Identifier identifier = new Identifier("block", "cloud");
    public AbstractItem drop = new CloudBlockItem();
    public AbstractItem rareDrop = new CelestiteItem();

    /**
     * Constructs an AbstractBlock with the specified identifier and block type.
     */
    public CloudBlock() {
        super(identifier, Block.WHITE_STAINED_GLASS);
    }

    /**
     * Initializes the requirements for placing or breaking the block.
     * Subclasses should override this method to add specific requirements if needed.
     */
    @Override
    public void initRequirements() {

    }

    @Override
    public void broke(CPlayer player) {
        player.getPlayer().getInventory().addItemStack(
                drop.buildItemStack()
        );
    }

    @Override
    public void brokeNatural(CPlayer player) {
        player.getPlayer().getInventory().addItemStack(
                drop.buildItemStack()
        );
        player.getPlayer().getInventory().addItemStack(
                rareDrop.buildItemStack()
        );
//        Random random = new Random();
//        if (random.nextInt(5) == 0) {
//
//        }
    }
}
