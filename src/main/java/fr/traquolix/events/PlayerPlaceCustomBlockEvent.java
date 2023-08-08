package fr.traquolix.events;

import fr.traquolix.content.blocks.AbstractBlock;
import fr.traquolix.content.blocks.BlockRegistry;
import fr.traquolix.identifiers.Identifier;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.item.ItemStack;

public class PlayerPlaceCustomBlockEvent {

    public PlayerPlaceCustomBlockEvent(GlobalEventHandler globalEventHandler) {
        globalEventHandler.addListener(PlayerBlockPlaceEvent.class, event -> {
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
