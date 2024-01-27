package fr.traquolix.events;

import fr.traquolix.content.blocks.AbstractBlock;
import fr.traquolix.content.blocks.BlockRegistry;
import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.locations.cave.generator.CaveGenerator;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class PlayerPlaceCustomBlockEvent {

    public PlayerPlaceCustomBlockEvent(GlobalEventHandler globalEventHandler) {
        globalEventHandler.addListener(PlayerBlockPlaceEvent.class, event -> {
            if (event.getBlockPosition().y() > CaveGenerator.peakStartHeight + 50) {
                event.setCancelled(true);
                return;
            }
            ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
            if (itemStack.hasTag(Identifier.getGlobalTag())) {
                Identifier identifier = new Identifier(itemStack.getTag(Identifier.getGlobalTag()));
                AbstractBlock abstractBlock = BlockRegistry.getInstance().getBlock(identifier);
                abstractBlock.onPlace(event.getPlayer(), event.getBlockPosition());
                event.setCancelled(true);
            } else {
                event.setCancelled(true);
                if (event.getPlayer().getItemInMainHand().amount() == 1) {
                    event.getPlayer().getInventory().setItemInMainHand(ItemStack.of(Material.AIR));
                } else {
                    event.getPlayer().setItemInMainHand(event.getPlayer().getItemInMainHand().withAmount(event.getPlayer().getItemInMainHand().amount() - 1));
                }
                event.getInstance().setBlock(event.getBlockPosition(), event.getBlock().withTag(Identifier.getNaturalTag(), false));
            }
        });
    }
}
