package fr.traquolix.content.blocks.misc;

import fr.traquolix.content.blocks.AbstractBlock;
import fr.traquolix.content.items.AbstractItem;
import fr.traquolix.content.items.types.misc.CelestiteItem;
import fr.traquolix.content.items.types.misc.CloudBlockItem;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import net.minestom.server.instance.block.Block;

import java.util.Random;

public class CloudBlock extends AbstractBlock {

    final public static Identifier identifier = new Identifier("block", "cloud");
    final public AbstractItem drop = new CloudBlockItem();
    final public AbstractItem rareDrop = new CelestiteItem();

    /**
     * Constructs an AbstractBlock with the specified identifier and block type.
     */
    public CloudBlock() {
        super(identifier, Block.POWDER_SNOW);
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
        Random random = new Random();
        if (random.nextInt(5) == 0) {
            player.getPlayer().getInventory().addItemStack(
                    rareDrop.buildItemStack()
            );
        }
    }
}
