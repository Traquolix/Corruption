package fr.traquolix.events;

import fr.traquolix.content.blocks.AbstractBlock;
import fr.traquolix.content.blocks.BlockRegistry;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.locations.cave.generator.CaveGenerator;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.item.ItemStack;

public class PlayerPlaceCustomBlockEvent {

    public PlayerPlaceCustomBlockEvent(GlobalEventHandler globalEventHandler) {
        globalEventHandler.addListener(PlayerBlockPlaceEvent.class, event -> {
            if (event.getBlockPosition().y() > CaveGenerator.peakStartHeight + CaveGenerator.getHighestHeight()) {
                event.setCancelled(true);
                return;
            }
            ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
            if (itemStack.hasTag(Identifier.getGlobalTag())) {
                Identifier identifier = new Identifier(itemStack.getTag(Identifier.getGlobalTag()));
                identifier.setGroup("block");
                AbstractBlock abstractBlock = BlockRegistry.getInstance().getBlock(identifier);
                abstractBlock.onPlace(event.getPlayer(), event.getBlockPosition());
                event.setCancelled(true);
            }
        });
    }
}
