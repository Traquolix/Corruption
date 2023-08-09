package fr.traquolix.events;

import fr.traquolix.Main;
import fr.traquolix.player.CPlayer;
import fr.traquolix.player.PlayerRegistry;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
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

            final Player player = event.getPlayer();
            player.setGameMode(GameMode.CREATIVE);
            event.setSpawningInstance(Main.instance);
            player.setRespawnPoint(new Pos(0, 41, 0));

            // Create a new CPlayer for the player and register it in the PlayerRegistry
            if (PlayerRegistry.getInstance().getCPlayer(player.getUuid()) == null) {
                new CPlayer(player);
            } else {
                PlayerRegistry.getInstance().getCPlayer(player.getUuid()).refreshEquipment();
            }

            // Set the base health value of the player to 20 (if needed)
            // player.setBaseStatValue(Stat.HEALTH, 20);
        });
    }
}
