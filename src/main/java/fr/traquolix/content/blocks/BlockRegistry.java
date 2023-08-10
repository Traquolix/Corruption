package fr.traquolix.content.blocks;

import fr.traquolix.content.generalities.identifiers.Identifier;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The BlockRegistry class is responsible for managing registered blocks in the game.
 */
public class BlockRegistry {

    /**
     * The singleton instance of the BlockRegistry.
     */
    private static final BlockRegistry INSTANCE = new BlockRegistry();

    /**
     * A map that stores registered blocks with their identifiers as keys.
     */
    private final ConcurrentMap<Identifier, AbstractBlock> blockMap = new ConcurrentHashMap<>();

    /**
     * Private constructor to prevent external instantiation of the BlockRegistry.
     */
    private BlockRegistry() {
    }

    /**
     * Returns the singleton instance of the BlockRegistry.
     *
     * @return The instance of BlockRegistry.
     */
    public static BlockRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * Registers a block with the BlockRegistry.
     *
     * @param block The block to be registered.
     */
    public void registerBlock(AbstractBlock block) {
        blockMap.put(block.identifier, block);
    }

    /**
     * Unregisters a block from the BlockRegistry.
     *
     * @param block The identifier of the block to be unregistered.
     */
    public void unregisterBlock(Identifier block) {
        blockMap.remove(block);
    }

    /**
     * Retrieves a registered block from the BlockRegistry based on its identifier.
     *
     * @param block The identifier of the block to be retrieved.
     * @return The registered block if found, or null if not found.
     */
    public AbstractBlock getBlock(Identifier block) {
        return blockMap.get(block);
    }

    public int getSize() {
        return blockMap.size();
    }
}
