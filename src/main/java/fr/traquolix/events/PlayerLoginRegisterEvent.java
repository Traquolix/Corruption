package fr.traquolix.events;

import fr.traquolix.player.CPlayer;
import fr.traquolix.player.PlayerRegistry;
import fr.traquolix.stats.Stat;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;

/**
 * A class that handles player login and registration events.
 * It listens for PlayerLoginEvent and creates a new CPlayer for the player when they log in.
 */
public class PlayerLoginRegisterEvent {

    /**
     * Constructs a new PlayerLoginRegisterEvent with the given global event handler.
     *
     * @param globalEventHandler The global event handler to register the listener.
     */
    public PlayerLoginRegisterEvent(GlobalEventHandler globalEventHandler) {
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            // Handle the PlayerLoginEvent
            Player player = event.getPlayer();

            // Create a new CPlayer for the player and register it in the PlayerRegistry
            new CPlayer(player);

            // Set the base health value of the player to 20 (if needed)
            // player.setBaseStatValue(Stat.HEALTH, 20);
        });
    }
}
