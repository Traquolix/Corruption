package fr.traquolix.content.blocks;

import fr.traquolix.identifiers.Identifier;
import fr.traquolix.content.items.PureItem;
import fr.traquolix.content.requirements.Requirement;
import fr.traquolix.player.CPlayer;
import lombok.Getter;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.HashSet;
import java.util.Set;

/**
 * The AbstractBlock class represents a block in the game world that can be placed and broken by players.
 */
@Getter
public abstract class AbstractBlock {

    /**
     * The unique identifier of the block.
     */
    final Identifier identifier;

    /**
     * The block type.
     */
    final Block block;

    /**
     * The list of requirements to place or break the block.
     */
    @Getter
    final protected Set<Requirement> requirements = new HashSet<>();

    /**
     * Constructs an AbstractBlock with the specified identifier and block type.
     *
     * @param identifier The unique identifier of the block.
     * @param block      The block type.
     */
    protected AbstractBlock(Identifier identifier, Block block) {
        this.identifier = identifier;
        this.block = block;

        initRequirements();

        BlockRegistry.getInstance().registerBlock(this);
        //logger.info("[Registry] - " + identifier.toString());
    }

    /**
     * Initializes the requirements for placing or breaking the block.
     * Subclasses should override this method to add specific requirements if needed.
     */
    public abstract void initRequirements();

    /**
     * Called when the block is broken by a player.
     *
     * @param player The player who broke the block.
     */
    public void broke(CPlayer player) {
        player.getPlayer().getInventory().addItemStack(
                PureItem.getPureItem(block)
        );
    }

    public abstract void brokeNatural(CPlayer player);

    /**
     * Places the block at the specified position in the given instance.
     *
     * @param instance The instance where the block will be placed.
     * @param pos      The position where the block will be placed.
     */
    public void placeBlock(Instance instance, Point pos) {
        instance.setBlock(pos, block.withTag(Identifier.getGlobalTag(), identifier.toString()));
    }

    /**
     * Gets the block type with its unique identifier tag.
     *
     * @return The block type with the identifier tag.
     */
    public Block getBlock() {
        return block
                .withTag(Identifier.getGlobalTag(), identifier.toString())
                .withTag(Identifier.getNaturalTag(), false);
    }

    public Block getNaturalBlock() {
        return block
                .withTag(Identifier.getGlobalTag(), identifier.toString())
                .withTag(Identifier.getNaturalTag(), true);
    }

    public void onPlace(Player player, Point blockPosition) {
        player.getInstance().setBlock(blockPosition, this.getBlock());
    }
}
