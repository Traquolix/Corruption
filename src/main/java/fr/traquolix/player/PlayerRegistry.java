package fr.traquolix.player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lombok.Getter;
import net.minestom.server.entity.Player;

/**
 * A registry class to associate Minestom's Player entities with custom player wrappers (CPlayer).
 */
public class PlayerRegistry {
    private static final PlayerRegistry INSTANCE = new PlayerRegistry();
    private final ConcurrentMap<UUID, CPlayer> cPlayerMap = new ConcurrentHashMap<>();

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
    public void registerPlayer(UUID playerUUID, CPlayer cPlayer) {
        cPlayerMap.put(playerUUID, cPlayer);
    }

    /**
     * Unregister a player and remove their associated custom player wrapper (CPlayer).
     *
     * @param player The Minestom Player entity to unregister.
     */
    public void unregisterPlayer(UUID playerUUID) {
        cPlayerMap.remove(playerUUID);
    }

    /**
     * Retrieve the custom player wrapper (CPlayer) associated with the given Minestom Player entity.
     *
     * @param player The Minestom Player entity to look up.
     * @return The custom player wrapper (CPlayer) associated with the player, or null if not found.
     */
    public CPlayer getCPlayer(UUID playerUUID) {
        return cPlayerMap.get(playerUUID);
    }
}
