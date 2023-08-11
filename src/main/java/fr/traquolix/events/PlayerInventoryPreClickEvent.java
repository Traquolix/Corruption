package fr.traquolix.events;

import fr.traquolix.GUI.skills.SkillGUI;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.content.items.ItemRegistry;
import fr.traquolix.content.items.types.misc.MainMenuItem;
import fr.traquolix.player.CPlayer;
import fr.traquolix.player.PlayerRegistry;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryPreClickEvent;

public class PlayerInventoryPreClickEvent {

    public PlayerInventoryPreClickEvent(GlobalEventHandler globalEventHandler) {
        globalEventHandler.addListener(InventoryPreClickEvent.class, event -> {
            if (event.getClickedItem().hasTag(Identifier.getGlobalTag())) {
                if (new Identifier(event.getClickedItem().getTag(Identifier.getGlobalTag())).equals(MainMenuItem.identifier)){
                    CPlayer cplayer = PlayerRegistry.getInstance().getCPlayer(event.getPlayer().getUuid());
                    new SkillGUI(cplayer).open(cplayer);
                    event.setCancelled(true);
                }
            }
        });
        }
}
