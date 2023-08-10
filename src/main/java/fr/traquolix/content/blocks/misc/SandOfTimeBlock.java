package fr.traquolix.content.blocks.misc;

import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.content.blocks.AbstractBlock;
import fr.traquolix.player.CPlayer;
import net.minestom.server.instance.block.Block;

/**
 * The SandOfTimeBlock class represents a block of Sand of Time in the game world.
 */
public class SandOfTimeBlock extends AbstractBlock {

    /**
     * The unique identifier of the Sand of Time block.
     */
    public static final Identifier identifier = new Identifier("block", "sand_of_time");

    /**
     * Constructs a SandOfTimeBlock.
     */
    public SandOfTimeBlock() {
        super(identifier, Block.SAND);
    }

    /**
     * Initializes the requirements for breaking the Sand of Time block.
     * There are no specific requirements for breaking this block.
     */
    @Override
    public void initRequirements() {
    }

    /**
     * Called when the Sand of Time block is broken by a player.
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
