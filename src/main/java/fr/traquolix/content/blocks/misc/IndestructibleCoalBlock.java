package fr.traquolix.content.blocks.misc;

import fr.traquolix.identifiers.Identifier;
import fr.traquolix.content.blocks.AbstractBlock;
import fr.traquolix.player.CPlayer;
import net.minestom.server.instance.block.Block;

public class IndestructibleCoalBlock extends AbstractBlock {

    /**
     * The unique identifier of the Indestructible coal block.
     */
    public static Identifier identifier = new Identifier("block", "indestructible_coal_block");

    /**
     * Constructs a IndestructibleCoalBlock.
     */
    public IndestructibleCoalBlock() {
        super(identifier, Block.COAL_BLOCK);
    }

    /**
     * Initializes the requirements for breaking the Indestructible coal block.
     * There are no specific requirements for breaking this block.
     */
    @Override
    public void initRequirements() {
    }

    /**
     * Called when the indestructible_coal_block is broken by a player.
     * Currently, no action is performed when the block is broken.
     *
     * @param player The player who broke the block.
     */
    @Override
    public void broke(CPlayer player) {

    }

    @Override
    public void brokeNatural(CPlayer player) {
    }
}
