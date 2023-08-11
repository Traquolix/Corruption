package fr.traquolix.events;

import fr.traquolix.Main;
import fr.traquolix.entity.EntityRegistry;
import fr.traquolix.entity.npc.NPCEntity;
import fr.traquolix.player.CPlayer;
import fr.traquolix.player.PlayerRegistry;
import fr.traquolix.time.TimeManager;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.network.packet.server.play.SpawnPlayerPacket;
import net.minestom.server.network.packet.server.play.TeamsPacket;
import net.minestom.server.scoreboard.Sidebar;
import net.minestom.server.scoreboard.TeamManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

            player.setTeam(MinecraftServer.getTeamManager().getTeam("players"));

            CPlayer cPlayer;
            // Create a new CPlayer for the player and register it in the PlayerRegistry
            if (PlayerRegistry.getInstance().getCPlayer(player.getUuid()) == null) {
                cPlayer = new CPlayer(player);
            } else {
                PlayerRegistry.getInstance().unregisterPlayer(player.getUuid());
                cPlayer = new CPlayer(player);
                PlayerRegistry.getInstance().getCPlayer(player.getUuid()).refreshEquipment();
            }


            EntityRegistry.getInstance().getEntityMap().forEach((id, entity) -> {
                if (entity.getType() == EntityType.PLAYER) {
                    ((NPCEntity) entity).initEntity(cPlayer);
                }
            });

                    // Set the base health value of the player to 20 (if needed)
            // player.setBaseStatValue(Stat.HEALTH, 20);
        });

        globalEventHandler.addListener(PlayerSpawnEvent.class, event -> {
            Sidebar scoreboard = new Sidebar(Component.text("Scoreboard"));
            scoreboard.createLine(new Sidebar.ScoreboardLine(
                    "current_time_line",
                    Component.text("Time : " + (TimeManager.getInstance().getCurrentTime()-1)),
                    0));

            scoreboard.addViewer(event.getPlayer());
        });

    }
}
