package fr.traquolix.events;

import fr.traquolix.content.blocks.AbstractBlock;
import fr.traquolix.content.blocks.BlockRegistry;
import fr.traquolix.content.blocks.misc.CloudBlock;
import fr.traquolix.identifiers.Identifier;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.instance.block.Block;

public class PlayerMoveOnCloudEvent {

    public PlayerMoveOnCloudEvent(GlobalEventHandler globalEventHandler) {

        // TODO Marche pas trop mal, mais à voir si on peut pas faire mieux / plus agréable à vivre
        globalEventHandler.addListener(PlayerMoveEvent.class, event -> {
            Block block = event.getPlayer().getInstance().getBlock(event.getNewPosition().add(0, -1, 0));
            Block block1 = event.getPlayer().getInstance().getBlock(event.getNewPosition());
            if (block.hasTag(Identifier.getGlobalTag()) || block1.hasTag(Identifier.getGlobalTag())) {
                Identifier identifier;
                if (block.hasTag(Identifier.getGlobalTag())) {
                    identifier = new Identifier(block.getTag(Identifier.getGlobalTag()));
                } else if (block1.hasTag(Identifier.getGlobalTag())) {
                    identifier = new Identifier(block1.getTag(Identifier.getGlobalTag()));
                } else {
                    return;
                }

                AbstractBlock abstractBlock = BlockRegistry.getInstance().getBlock(identifier);

                if (identifier.equals(CloudBlock.identifier)) {
                    event.getPlayer().teleport(event.getNewPosition().add(0, -0.01, 0));
                    event.getPlayer().sendMessage("You are walking on a cloud!");
                }

            }
        });
    }
}
