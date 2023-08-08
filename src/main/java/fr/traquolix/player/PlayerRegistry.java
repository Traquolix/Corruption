package fr.traquolix.player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.minestom.server.entity.Player;

/**
 * A registry class to associate Minestom's Player entities with custom player wrappers (CPlayer).
 */
public class PlayerRegistry {
    private static final PlayerRegistry INSTANCE = new PlayerRegistry();
    private final ConcurrentMap<Player, CPlayer> cPlayerMap = new ConcurrentHashMap<>();

    /**
     * Private constructor to prevent instantiation from outside.
     */
    private PlayerRegistry() {
    }

    /**
     * Get the singleton instance of PlayerRegistry.
     *
     * @return The singleton instance of PlayerRegistry.
     */
    public static PlayerRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * Register a player and associate them with a custom player wrapper (CPlayer).
     *
     * @param player  The Minestom Player entity to register.
     * @param cPlayer The custom player wrapper (CPlayer) to associate with the player.
     */
    public void registerPlayer(Player player, CPlayer cPlayer) {
        cPlayerMap.put(player, cPlayer);
    }

    /**
     * Unregister a player and remove their associated custom player wrapper (CPlayer).
     *
     * @param player The Minestom Player entity to unregister.
     */
    public void unregisterPlayer(Player player) {
        cPlayerMap.remove(player);
    }

    /**
     * Retrieve the custom player wrapper (CPlayer) associated with the given Minestom Player entity.
     *
     * @param player The Minestom Player entity to look up.
     * @return The custom player wrapper (CPlayer) associated with the player, or null if not found.
     */
    public CPlayer getCPlayer(Player player) {
        return cPlayerMap.get(player);
    }
}
