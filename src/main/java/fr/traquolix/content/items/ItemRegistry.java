package fr.traquolix.content.items;

import fr.traquolix.content.generalities.identifiers.Identifier;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The ItemRegistry class is a singleton class responsible for maintaining a mapping of item identifiers to
 * their corresponding AbstractItem objects.
 */
public class ItemRegistry {
    private static final ItemRegistry INSTANCE = new ItemRegistry();

    // A map that associates item identifiers with their corresponding AbstractItem objects
    private final ConcurrentMap<Identifier, AbstractItem> itemMap = new ConcurrentHashMap<>();

    // Private constructor to prevent external instantiation (singleton pattern)
    private ItemRegistry() {
    }

    /**
     * Returns the single instance of the ItemRegistry.
     *
     * @return The ItemRegistry instance.
     */
    public static ItemRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * Registers an AbstractItem in the item registry.
     *
     * @param item The AbstractItem to register.
     */
    public void registerItem(AbstractItem item) {
        itemMap.put(item.getIdentifier(), item);
    }

    /**
     * Unregisters an item from the item registry based on its identifier.
     *
     * @param item The identifier of the item to unregister.
     */
    public void unregisterItem(Identifier item) {
        itemMap.remove(item);
    }

    /**
     * Retrieves an AbstractItem from the item registry based on its identifier.
     *
     * @param item The identifier of the item to retrieve.
     * @return The AbstractItem associated with the given identifier, or null if not found.
     */
    public AbstractItem getItem(Identifier item) {
        return itemMap.get(item);
    }

    public int getSize() {
        return itemMap.size();
    }
}
