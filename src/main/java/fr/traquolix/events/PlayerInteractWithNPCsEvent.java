package fr.traquolix.events;

import fr.traquolix.entity.AbstractEntity;
import fr.traquolix.entity.EntityRegistry;
import fr.traquolix.identifiers.Identifier;
import fr.traquolix.player.CPlayer;
import fr.traquolix.player.PlayerRegistry;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerEntityInteractEvent;

import java.util.Objects;

public class PlayerInteractWithNPCsEvent {

    public PlayerInteractWithNPCsEvent(GlobalEventHandler globalEventHandler) {
        globalEventHandler.addListener(PlayerEntityInteractEvent.class, event -> {
            CPlayer cPlayer = PlayerRegistry.getInstance().getCPlayer(event.getPlayer().getUuid());
            if (cPlayer == null) return;
            if (!event.getTarget().hasTag(Identifier.getGlobalTag())) return;
            Identifier identifier = new Identifier(event.getTarget().getTag(Identifier.getGlobalTag()));
            if (!Objects.equals(identifier.getGroup(), "npc")) return;
            EntityRegistry.getInstance().getEntity(identifier).onInteract(cPlayer);
        });
    }
}
